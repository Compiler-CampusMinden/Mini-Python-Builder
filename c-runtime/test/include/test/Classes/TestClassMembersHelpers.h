#ifndef TEST_CLASS_MEMBERS_HELPERS_H
#define TEST_CLASS_MEMBERS_HELPERS_H

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

__MPyObj* func_getter_setter___init__(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_getter_setter_getX(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* func_getter_setter_setX(__MPyObj *args, __MPyObj *kwargs);

#endif
