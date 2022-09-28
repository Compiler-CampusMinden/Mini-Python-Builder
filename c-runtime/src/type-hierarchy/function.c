#include "type-hierarchy/function.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include "mpy_obj.h"
#include "errors.h"
#include "checks.h"
#include "builtins-setup.h"
#include "type-hierarchy/object.h"
#include "type-hierarchy/type.h"
#include "literals/tuple.h"

// TODO:
// - could default args be put here?
__MPyObj* __mpy_obj_init_func(__mpy_func_call function) {
    __MPyObj *obj = __mpy_obj_new();
    obj->type = __MPyType_Function;
    obj->content = __mpy_checked_malloc(sizeof(__mpy_func_call));
    *(__mpy_func_call*)obj->content = function;

    return __mpy_obj_return(obj);
}

