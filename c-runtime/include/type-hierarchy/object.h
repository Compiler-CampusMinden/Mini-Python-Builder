#ifndef TYPE_HIERARCHY_OBJECT_H
#define TYPE_HIERARCHY_OBJECT_H

#include "mpy_obj.h"

__MPyObj *__mpy_obj_init_object();

__MPyObj* __mpy_object_func_str_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_object_func_new_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj *__mpy_object_func_init_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj *__mpy_object_func_bool_impl(__MPyObj *args, __MPyObj *kwargs);

#endif
