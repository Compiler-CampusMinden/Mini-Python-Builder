#ifndef TEST_CLASS_INIT_HELPERS_H
#define TEST_CLASS_INIT_HELPERS_H

#include <stddef.h>

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

__MPyObj* func_init_no_params___init__(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_init_one_param___init__(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_init_multiple_params___init__(__MPyObj *args, __MPyObj *kwargs);

#endif
