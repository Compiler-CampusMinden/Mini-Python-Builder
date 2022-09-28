#ifndef LITERALS_STR_H
#define LITERALS_STR_H

#include "mpy_obj.h"

__MPyObj* __mpy_obj_init_str_static(const char *string);

__MPyObj* __mpy_obj_init_str_dynamic(char *string);

const char* __mpy_str_as_c_str(__MPyObj *self);

__MPyObj *__mpy_str_func_str_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj *__mpy_str_func_bool_impl(__MPyObj *args, __MPyObj *kwargs);

#endif

