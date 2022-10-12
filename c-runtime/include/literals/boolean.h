#ifndef LITERALS_BOOLEAN_H
#define LITERALS_BOOLEAN_H

#include "mpy_obj.h"

typedef bool __mpy_boolean_c_type;

__MPyObj* __mpy_obj_init_boolean(__mpy_boolean_c_type value);

__MPyObj* __mpy_boolean_func_str_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_boolean_func_bool_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_boolean_func_int_impl(__MPyObj *args, __MPyObj *kwargs);

__mpy_boolean_c_type __mpy_boolean_raw(__MPyObj *self);

// compare
__MPyObj* __mpy_boolean_func_eq_impl(__MPyObj *args, __MPyObj *kwargs);
__MPyObj* __mpy_boolean_func_ne_impl(__MPyObj *args, __MPyObj *kwargs);

#endif /* LITERALS_BOOLEAN_H */

