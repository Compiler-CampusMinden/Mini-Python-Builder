#ifndef TYPE_HIERARCHY_TUPLE_H
#define TYPE_HIERARCHY_TUPLE_H

#include "mpy_obj.h"

__MPyObj* __mpy_obj_init_tuple(unsigned int size);

__MPyObj* __mpy_tuple_assign(unsigned int pos, __MPyObj *value, __MPyObj *self);

unsigned int __mpy_tuple_size(__MPyObj *self);

__MPyObj* __mpy_tuple_get_at(__MPyObj *self, unsigned int pos);

void __mpy_tuple_foreach(__MPyObj *self, void (*action) (__MPyObj* obj));

__MPyObj *__mpy_tuple_func_bool_impl(__MPyObj *args, __MPyObj *kwargs);

#endif

