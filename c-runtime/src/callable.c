
#include "callable.h"

#include <assert.h>

#include "errors.h"
#include "type-hierarchy/object.h"
#include "builtins-setup.h"
#include "type-hierarchy/type.h"
#include "type-hierarchy/bound-method.h"
#include "literals/tuple.h"
#include "mpy_obj.h"

__MPyObj* inject_self_into_args(__MPyObj *args, __MPyObj *self) {
    // not sure if this is the cleanest approach, but hey:
    // to make newObj available as self to __init__ copy all other arguments to one position later
    unsigned int argCnt = __mpy_tuple_size(args);
    __MPyObj *initArgs = __mpy_obj_init_tuple(argCnt + 1);
    __mpy_tuple_assign(0, self, initArgs);

    for (unsigned int i = 1; i <= argCnt; i++) {
        __mpy_tuple_assign(i, __mpy_tuple_get_at(args, i - 1), initArgs);
    }

    __mpy_obj_ref_dec(args);

    return initArgs;
}

__MPyObj* __mpy_call(__MPyObj *callable, __MPyObj *args, __MPyObj *kwargs) {
    assert(callable != NULL && args != NULL);
    // TODO kwargs should always be != NULL (but theres no dict yet)
    if (kwargs == NULL) {
        __MPY_NOTE("(bug) using object() for kwargs")
        kwargs = __mpy_obj_init_object();
    }

    if (callable->type == __MPyType_Function) {
        __mpy_func_call *func = callable->content;
        assert(func != NULL);
        return (*func)(args, kwargs);
    }

    if (callable->type == __MPyType_BoundMethod) {
        __MPyBoundMethodContent* boundMethod = (__MPyBoundMethodContent*) callable->content;
        __MPyObj *instance = boundMethod->instance;
        __MPyObj *func = boundMethod->func;

        __MPyObj *callArgs = inject_self_into_args(args, instance);
        __MPY_NOTE("(bug) not injecting self into kwargs");

        return __mpy_call(func, callArgs, kwargs);
    }

    if (callable->type == __MPyType_Type) {
        __MPyObj *fnNew = __mpy_obj_get_attr(callable, "__new__");

        __MPyObj *newObj = __mpy_call(fnNew, __mpy_tuple_assign(0, callable, __mpy_obj_init_tuple(1)), NULL);

        // make newObj non-temporary for the call to init
        // so it is not cleaned up there
        __mpy_obj_ref_inc(newObj);

        /* __MPyObj *initArgs = inject_self_into_args(args, newObj); */
        __MPY_NOTE("(bug) not injecting self into kwargs");

        __MPyObj *fnInit = __mpy_obj_get_attr(newObj, "__init__");
        __mpy_obj_ref_dec(__mpy_call(fnInit, args, kwargs));

        return __mpy_obj_return(newObj);
    }

    fprintf(stderr, "TypeError: '%s' object is not callable.\n", __mpy_type_name(callable->type));
    __mpy_fatal_error(__MPY_ERROR_USER);
}

