
#include "literals/int.h"

#include <stdlib.h>
#include <stdio.h>
#include <assert.h>
#include <string.h>

#include "literals/boolean.h"
#include "mpy_obj.h"
#include "builtins-setup.h"
#include "literals/str.h"
#include "errors.h"
#include "checks.h"
#include "function-args.h"
#include "type-hierarchy/bound-method.h"

#define MAX_SINGLE_DIGIT_DECIMAL 9
#define POSITION_FACTOR_DECIMAL 10

typedef struct MPyIntContent {
    __mpy_int_c_type value;
    __MPyObj *strMethod;
    __MPyObj *boolMethod;
} MPyIntContent;

__MPyObj *__mpy_int_func_str_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("int.__str__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    __mpy_int_c_type value = ((MPyIntContent*)self->content)->value;
    __mpy_int_c_type valueCntDigits = value;

    size_t digits = 2; // 0 byte + at least one digit
    if (value < 0) {
        digits += 1; // sign
        valueCntDigits = llabs(valueCntDigits);
    }
    while (valueCntDigits > MAX_SINGLE_DIGIT_DECIMAL) {
        digits++;
        valueCntDigits = valueCntDigits / POSITION_FACTOR_DECIMAL;
    }

    char *string = malloc(digits);
    snprintf(string, digits, "%lli", value);

    __mpy_obj_ref_dec(self);
    return __mpy_obj_return(__mpy_obj_init_str_dynamic(string));
}

__MPyObj *__mpy_int_func_bool_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("int.__bool__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    __mpy_int_c_type value = *((__mpy_int_c_type*)self->content);

    __mpy_boolean_c_type truth = value != 0;

    __mpy_obj_ref_dec(self);
    return __mpy_obj_return(__mpy_obj_init_boolean(truth));
}

__MPyObj* __mpy_int_set_attr_impl(__MPyObj *self, const char *name, __MPyObj *value) {
    __MPY_TODO("set attr");
    return NULL;
}

__MPyObj* __mpy_int_get_attr_impl(__MPyObj *self, const char *name) {
    if (strcmp("__str__", name) == 0) {
        return ((MPyIntContent*) self->content)->strMethod;
    }
    if (strcmp("__bool__", name) == 0) {
        return ((MPyIntContent*) self->content)->boolMethod;
    }

    return NULL;
}

void int_cleanup(__MPyObj *self) {
    MPyIntContent *content = self->content;

    __mpy_obj_ref_dec(content->strMethod);
}

__MPyObj* __mpy_obj_init_int(__mpy_int_c_type value) {
    __MPyObj *obj = __mpy_obj_new();
    obj->type = __MPyType_Num;
    obj->attrAccessor = __mpy_int_get_attr_impl;
    obj->attrSetter = __mpy_int_set_attr_impl;
    obj->cleanupAction = int_cleanup;

    obj->content = __mpy_checked_malloc(sizeof(MPyIntContent));
    MPyIntContent *content = (MPyIntContent*) obj->content;
    content->value = value;
    content->strMethod = __mpy_bind_func(__MPyFunc_Int_str, obj);
    content->boolMethod = __mpy_bind_func(__MPyFunc_Int_bool, obj);

    return __mpy_obj_return(obj);
}

