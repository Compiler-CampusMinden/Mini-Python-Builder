
#ifndef TYPE_HIERARCHY_TYPE_H
#define TYPE_HIERARCHY_TYPE_H

#include "mpy_obj.h"
#include "simple_hash_map.h"

__MPyObj* __mpy_obj_init_type_builtin(const char *name, __MPyObj *self, __MPyHashMap *attributes, __MPyObj *parentType);

__MPyObj* __mpy_obj_init_type(const char *name, __MPyObj *parentType);

__MPyObj *__mpy_type_set_attr_impl(__MPyObj *self, const char *name, __MPyObj *value);

__MPyObj *__mpy_type_get_attr_impl(__MPyObj *self, const char *name);

__MPyObj *__mpy_type_func_str_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj *__mpy_type_func_call_impl(__MPyObj *args, __MPyObj *kwargs);

__MPyObj* __mpy_type_get_parent_type(__MPyObj *self);

const char* __mpy_type_name(__MPyObj *self);

#endif
