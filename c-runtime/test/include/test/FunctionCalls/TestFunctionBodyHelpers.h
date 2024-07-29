#ifndef TEST_FUNCTION_BODY_HELPERS_H
#define TEST_FUNCTION_BODY_HELPERS_H

#include<stddef.h>

#include "assert.h"
#include "mpy_aliases.h"
#include "mpy_obj.h"
#include "builtins-setup.h"
#include "function-args.h"
#include "literals/tuple.h"
#include "literals/int.h"
#include "literals/boolean.h"
#include "literals/str.h"
#include "type-hierarchy/object.h"
#include "type-hierarchy/type.h"

__MPyObj* func_minimalistic_body(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_print_from_func(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_return_local_var(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_return_param_body(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_return_add(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_return_add_conditional(__MPyObj *args, __MPyObj *kwargs);

#endif
