#ifndef TEST_FUNCTION_RETURN_HELPERS_H
#define TEST_FUNCTION_RETURN_HELPERS_H

#include<stddef.h>

#include "assert.h"
#include "mpy_aliases.h"
#include "mpy_obj.h"
#include "builtins-setup.h"
#include "literals/tuple.h"
#include "literals/int.h"
#include "function-args.h"
#include "literals/boolean.h"
#include "literals/str.h"
#include "type-hierarchy/object.h"
#include "type-hierarchy/type.h"

__MPyObj* func_return_expression(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_return_tuple_literal_throws(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_return_void_function_call_print(__MPyObj *args, __MPyObj *kwargs);

#endif
