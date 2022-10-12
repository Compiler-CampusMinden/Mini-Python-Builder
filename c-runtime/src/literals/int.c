
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
#include "type-hierarchy/type.h"

#define MAX_SINGLE_DIGIT_DECIMAL 9
#define POSITION_FACTOR_DECIMAL 10

typedef struct MPyIntContent {
    __mpy_int_c_type value;
    __MPyObj *strMethod;
    __MPyObj *boolMethod;
    __MPyObj *addMethod;
    __MPyObj *subMethod;
    __MPyObj *mulMethod;
    __MPyObj *divMethod;
    __MPyObj *lshiftMethod;
    __MPyObj *rshiftMethod;
    __MPyObj *andMethod;
    __MPyObj *orMethod;
    __MPyObj *xorMethod;

    __MPyObj *eqMethod;
    __MPyObj *neMethod;
    __MPyObj *geMethod;
    __MPyObj *leMethod;
    __MPyObj *gtMethod;
    __MPyObj *ltMethod;
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

#define int_binary_calc(name, calc) \
__MPyObj *__mpy_int_func_ ## name ## _impl(__MPyObj *args, __MPyObj *kwargs) { \
    assert(args != NULL && kwargs != NULL); \
 \
    __MPyGetArgsState argHelper = __mpy_args_init("int." #name, args, kwargs, 2); \
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self"); \
    __MPyObj *other = __mpy_args_get_positional(&argHelper, 1, "other"); \
    __mpy_args_finish(&argHelper); \
 \
    if (self->type != __MPyType_Num) { \
        fprintf(stderr, "TypeError: int." #name " cannot be called on type '%s'.\n", __mpy_type_name(self->type)); \
        __mpy_fatal_error(__MPY_ERROR_USER); \
    } \
    if (other->type != __MPyType_Num) { \
        fprintf(stderr, "TypeError: int." #name " cannot add type '%s'.\n", __mpy_type_name(other->type)); \
        __mpy_fatal_error(__MPY_ERROR_USER); \
    } \
 \
    __mpy_int_c_type valueSelf = ((MPyIntContent*)self->content)->value; \
    __mpy_int_c_type valueOther = ((MPyIntContent*)other->content)->value; \
 \
    __MPyObj *result = calc; \
 \
    __mpy_obj_ref_dec(self); \
    __mpy_obj_ref_dec(other); \
    return __mpy_obj_return(result); \
} \

#define int_binary_op(name, op) int_binary_calc(name, __mpy_obj_init_int(valueSelf op valueOther))
#define int_binary_compare_op(name, op) int_binary_calc(name, __mpy_obj_init_boolean(valueSelf op valueOther))
#define int_binary_op_unsigned(name, op) int_binary_calc(name, __mpy_obj_init_int((unsigned long long) valueSelf op (unsigned long long) valueOther))

int_binary_op(add, +)
int_binary_op(sub, -)
int_binary_op(mul, *)
int_binary_op(div, /)
int_binary_calc(lshift, __mpy_obj_init_int(valueSelf * (1U << (unsigned long long) valueOther)))
int_binary_calc(rshift, __mpy_obj_init_int(valueSelf / (1U << (unsigned long long) valueOther)))
int_binary_op_unsigned(and, &)
int_binary_op_unsigned(or, |)
int_binary_op_unsigned(xor, ^)

int_binary_compare_op(eq, ==)
int_binary_compare_op(ne, !=)
int_binary_compare_op(ge, >=)
int_binary_compare_op(le, <=)
int_binary_compare_op(gt, >)
int_binary_compare_op(lt, <)

#undef int_binary_calc
#undef int_binary_op
#undef int_binary_op_unsigned

__MPyObj* __mpy_int_set_attr_impl(__MPyObj *self, const char *name, __MPyObj *value) {
    __MPY_TODO("set attr");
    return NULL;
}

__MPyObj* __mpy_int_get_attr_impl(__MPyObj *self, const char *name) {
#define builtin_method(purpose) \
    if (strcmp("__" #purpose "__", name) == 0) { \
        return ((MPyIntContent*) self->content)->purpose ## Method; \
    }

    builtin_method(str);
    builtin_method(bool);

    builtin_method(add);
    builtin_method(sub);
    builtin_method(mul);
    builtin_method(div);
    builtin_method(lshift);
    builtin_method(rshift);
    builtin_method(and);
    builtin_method(or);
    builtin_method(xor);

    builtin_method(eq);
    builtin_method(ne);
    builtin_method(ge);
    builtin_method(le);
    builtin_method(gt);
    builtin_method(lt);

#undef builtin_method

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

#define bind_builtin(purpose) content->purpose ## Method = __mpy_bind_func(__MPyFunc_Int_ ## purpose, obj);
    bind_builtin(str);
    bind_builtin(bool);
    bind_builtin(add);
    bind_builtin(sub);
    bind_builtin(mul);
    bind_builtin(div);
    bind_builtin(lshift);
    bind_builtin(rshift);
    bind_builtin(and);
    bind_builtin(or);
    bind_builtin(xor);

    bind_builtin(eq);
    bind_builtin(ne);
    bind_builtin(ge);
    bind_builtin(le);
    bind_builtin(gt);
    bind_builtin(lt);

#undef bind_builtin

    return __mpy_obj_return(obj);
}

