#ifndef TEST_FUNCTION_PARAMS_HELPERS_H
#define TEST_FUNCTION_PARAMS_HELPERS_H

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

__MPyObj* func_func1(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_func2(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_func3(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_func4(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_func_throws1(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_throws2(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_throws3(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_throws4(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_throws5(__MPyObj *args, __MPyObj *kwargs);

#endif
