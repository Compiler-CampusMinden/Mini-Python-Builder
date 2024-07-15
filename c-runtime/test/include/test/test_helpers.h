#ifndef TEST_HELPERS_H
#define TEST_HELPERS_H

#include "type-hierarchy/object.h"
#include "literals/int.h"
#include "mpy_obj.h"
#include "checks.h"
#include "type-hierarchy/type.h"

typedef bool __mpy_boolean_c_type;

typedef struct __MPyStrContent {
    bool isStatic;
    const char *string;
    __MPyObj *strMethod;
    __MPyObj *boolMethod;
    __MPyObj *addMethod;
    __MPyObj *intMethod;
    __MPyObj *eqMethod;
    __MPyObj *neMethod;
    __MPyObj *geMethod;
    __MPyObj *leMethod;
    __MPyObj *gtMethod;
    __MPyObj *ltMethod;
} __MPyStrContent;

typedef struct MPyBooleanContent {
    __mpy_boolean_c_type value;
    __MPyObj *strMethod;
    __MPyObj *boolMethod;
    __MPyObj *intMethod;
    __MPyObj *eqMethod;
    __MPyObj *neMethod;
} MPyBooleanContent;

extern void print_mpyobj(__MPyObj *mpyobj);

extern void print_mpyobj_int(__MPyObj *mpyobj);

extern void print_mpyobj_str(__MPyObj *mpyobj);

extern void print_mpyobj_bool(__MPyObj *mpyobj);

#endif
