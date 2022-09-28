#ifndef LITERALS_NUM_H
#define LITERALS_NUM_H

#include "mpy_obj.h"

typedef signed long long int __mpy_int_c_type;

__MPyObj* __mpy_obj_init_int(__mpy_int_c_type value);

__MPyObj *__mpy_int_func_str_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj *__mpy_int_func_bool_impl(__MPyObj *args, __MPyObj *kwargs);

#endif
