#include "literals/boolean.h"

#include <assert.h>
#include <string.h>

#include "builtins-setup.h"
#include "type-hierarchy/type.h"
#include "checks.h"
#include "errors.h"
#include "function-args.h"
#include "literals/str.h"
#include "literals/int.h"
#include "type-hierarchy/bound-method.h"

typedef struct MPyBooleanContent {
    __mpy_boolean_c_type value;
    __MPyObj *strMethod;
    __MPyObj *boolMethod;
    __MPyObj *intMethod;
    __MPyObj *eqMethod;
    __MPyObj *neMethod;
} MPyBooleanContent;

__MPyObj* __mpy_boolean_set_attr_impl(__MPyObj *self, const char *name, __MPyObj *value) {
    (void)self;
    (void)name;
    (void)value;
    __MPY_TODO("set attr");
}

__MPyObj* __mpy_boolean_get_attr_impl(__MPyObj *self, const char *name) {
    if (strcmp("__str__", name) == 0) {
        return ((MPyBooleanContent*) self->content)->strMethod;
    }
    if (strcmp("__bool__", name) == 0) {
        return ((MPyBooleanContent*) self->content)->boolMethod;
    }
    if (strcmp("__int__", name) == 0) {
        return ((MPyBooleanContent*) self->content)->intMethod;
    }

// compare
    if (strcmp("__eq__", name) == 0) {
        return ((MPyBooleanContent*) self->content)->eqMethod;
    }
    if (strcmp("__ne__", name) == 0) {
        return ((MPyBooleanContent*) self->content)->neMethod;
    }
    return NULL;
}

void boolean_cleanup(__MPyObj *self) {
    MPyBooleanContent *content = self->content;

    __mpy_obj_ref_dec(content->strMethod);
    __mpy_obj_ref_dec(content->boolMethod);
    __mpy_obj_ref_dec(content->intMethod);
    __mpy_obj_ref_dec(content->eqMethod);
    __mpy_obj_ref_dec(content->neMethod);
}

__MPyObj* __mpy_obj_init_boolean(__mpy_boolean_c_type value) {
    __MPyObj *obj = __mpy_obj_new();
    obj->type = __MPyType_Boolean;
    obj->attrAccessor = __mpy_boolean_get_attr_impl;
    obj->attrSetter = __mpy_boolean_set_attr_impl;
    obj->cleanupAction = boolean_cleanup;

    obj->content = __mpy_checked_malloc(sizeof(MPyBooleanContent));
    MPyBooleanContent *content = (MPyBooleanContent*) obj->content;
    content->value = value;
    content->strMethod = __mpy_bind_func(__MPyFunc_Boolean_str, obj);
    content->boolMethod = __mpy_bind_func(__MPyFunc_Boolean_bool, obj);
    content->intMethod = __mpy_bind_func(__MPyFunc_Boolean_int, obj);

    content->eqMethod = __mpy_bind_func(__MPyFunc_Boolean_eq, obj);
    content->neMethod = __mpy_bind_func(__MPyFunc_Boolean_ne, obj);
    return __mpy_obj_return(obj);
}

__MPyObj* __mpy_boolean_func_str_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("bool.__str__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    __mpy_boolean_c_type value = ((MPyBooleanContent*)self->content)->value;

    char *string = NULL;
    if (value) {
        string = malloc(strlen("True") + 1);
        strcpy(string, "True");
    } else {
        string = malloc(strlen("False") + 1);
        strcpy(string, "False");
    }

    __mpy_obj_ref_dec(self);
    return __mpy_obj_return(__mpy_obj_init_str_dynamic(string));
}

__MPyObj* __mpy_boolean_func_bool_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("bool.__bool__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    // since bools are immutable: simply return self

    return __mpy_obj_return(self);
}

__MPyObj* __mpy_boolean_func_int_impl(__MPyObj *args, __MPyObj *kwargs) {
    __MPyGetArgsState argHelper = __mpy_args_init("bool.__int__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    __mpy_boolean_c_type value = ((MPyBooleanContent*)self->content)->value;
    return __mpy_obj_return(__mpy_obj_init_int(value));
}

__MPyObj* __mpy_boolean_func_eq_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("bool.__eq__", args, kwargs, 2);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __MPyObj *other = __mpy_args_get_positional(&argHelper, 1, "self");
    __mpy_args_finish(&argHelper);

    if (self->type != __MPyType_Boolean) {
        fprintf(stderr, "TypeError: bool.__eq__ cannot be called on type '%s'.\n", __mpy_type_name(self->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    if (other->type != __MPyType_Boolean) {
        fprintf(stderr, "TypeError: bool.__eq__ cannot concat type '%s'.\n", __mpy_type_name(other->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }

    __mpy_boolean_c_type valueSelf = ((MPyBooleanContent*)self->content)->value;
    __mpy_boolean_c_type valueOther = ((MPyBooleanContent*)other->content)->value;

    if (valueSelf == valueOther) {
        return __mpy_obj_return(__mpy_obj_init_boolean(true));
    }
    return __mpy_obj_return(__mpy_obj_init_boolean(false));
}

__MPyObj* __mpy_boolean_func_ne_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("bool.__ne__", args, kwargs, 2);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __MPyObj *other = __mpy_args_get_positional(&argHelper, 1, "self");
    __mpy_args_finish(&argHelper);

    if (self->type != __MPyType_Boolean) {
        fprintf(stderr, "TypeError: bool.__ne__ cannot be called on type '%s'.\n", __mpy_type_name(self->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    if (other->type != __MPyType_Boolean) {
        fprintf(stderr, "TypeError: bool.__ne__ cannot concat type '%s'.\n", __mpy_type_name(other->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }

    __mpy_boolean_c_type valueSelf = ((MPyBooleanContent*)self->content)->value;
    __mpy_boolean_c_type valueOther = ((MPyBooleanContent*)other->content)->value;

    if (valueSelf == valueOther) {
        return __mpy_obj_return(__mpy_obj_init_boolean(false));
    }
    return __mpy_obj_return(__mpy_obj_init_boolean(true));
}

__mpy_boolean_c_type __mpy_boolean_raw(__MPyObj *self) {
    assert(self != NULL);
    assert(self->type == __MPyType_Boolean);

    return ((MPyBooleanContent*)self->content)->value;
}
