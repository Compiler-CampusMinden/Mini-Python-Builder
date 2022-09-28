#include "builtin-functions/type.h"

#include <stdio.h>
#include <assert.h>

#include "mpy_obj.h"
#include "errors.h"
#include "literals/tuple.h"
#include "literals/str.h"
#include "type-hierarchy/function.h"
#include "type-hierarchy/object.h"
#include "function-args.h"

__MPyObj* __mpy_func_type(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("type", args, kwargs, 1);
    __MPyObj *obj = __mpy_args_get_positional(&argHelper, 0, "object");
    __mpy_args_finish(&argHelper);

    __MPyObj *typeObj = obj->type;
    __mpy_obj_ref_inc(typeObj);

    __mpy_obj_ref_dec(obj);
    return __mpy_obj_return(typeObj);
}

