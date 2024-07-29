#ifndef TEST_FUNCTION_RECURSION_HELPERS_H
#define TEST_FUNCTION_RECURSION_HELPERS_H

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

extern __MPyObj *recursion;

__MPyObj* func_recursion(__MPyObj *args, __MPyObj *kwargs);

#endif
