#ifndef TYPE_HIERARCHY_BOUND_METHOD_H
#define TYPE_HIERARCHY_BOUND_METHOD_H

#include "mpy_obj.h"
#include "callable.h"

typedef struct __MPyBoundMethodContent {
    __MPyObj *func;
    __MPyObj *instance;
} __MPyBoundMethodContent;

__MPyObj* __mpy_bind_func(__MPyObj *func, __MPyObj *instance);

#endif
