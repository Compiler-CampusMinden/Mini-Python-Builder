#include "builtin-functions/super.h"

#include <assert.h>
#include <stdlib.h>

#include "callable.h"
#include "literals/tuple.h"
#include "type-hierarchy/object.h"
#include "type-hierarchy/type.h"

__MPyObj* __mpy_func_super(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyObj *self =  __mpy_tuple_get_at(args, 0);

    unsigned int argCnt = __mpy_tuple_size(args);
    // first parameter has been self (i.e. the (child) class initialized, which is not passed in the python code and not intended to be passed to the parent)
    __MPyObj *initArgs = __mpy_obj_init_tuple(argCnt - 1);
    for (unsigned int i = 1; i < argCnt; i++) {
        __mpy_tuple_assign(i - 1, __mpy_tuple_get_at(args, i), initArgs);
    }
    __mpy_obj_ref_dec(args);

    __MPyObj *parentInstance = __mpy_call(__mpy_type_get_parent_type(self->type), initArgs, kwargs);
    __mpy_obj_ref_inc(parentInstance);
    self->parent = parentInstance;

    return __mpy_obj_return(__mpy_obj_init_object());
}
