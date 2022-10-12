#ifndef LITERALS_NUM_H
#define LITERALS_NUM_H

#include "mpy_obj.h"

typedef signed long long int __mpy_int_c_type;

__MPyObj* __mpy_obj_init_int(__mpy_int_c_type value);

__MPyObj* __mpy_int_func_str_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_bool_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_add_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_sub_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_mul_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_div_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_lshift_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_rshift_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_and_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_or_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_xor_impl(__MPyObj *args, __MPyObj *kwargs);


__MPyObj* __mpy_int_func_eq_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_ne_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_ge_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_le_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_gt_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_int_func_lt_impl(__MPyObj *args, __MPyObj *kwargs);

#endif
