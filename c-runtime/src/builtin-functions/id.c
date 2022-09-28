#include "builtin-functions/id.h"

#include <stdio.h>
#include <assert.h>

#include "mpy_obj.h"
#include "errors.h"
#include "function-args.h"
#include "literals/int.h"
#include "literals/tuple.h"

__MPyObj* __mpy_func_id(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("id", args, kwargs, 1);
    __MPyObj *obj = __mpy_args_get_positional(&argHelper, 0, "obj");
    __mpy_args_finish(&argHelper);

    __MPyObj *retVal = __mpy_obj_init_int(obj->id);

    __mpy_obj_ref_dec(obj);
    return __mpy_obj_return(retVal);
}

