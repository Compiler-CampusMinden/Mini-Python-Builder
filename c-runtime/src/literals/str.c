#include "literals/str.h"

#include <stdbool.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>

#include "literals/boolean.h"
#include "literals/int.h"
#include "mpy_obj.h"
#include "builtins-setup.h"
#include "checks.h"
#include "errors.h"
#include "function-args.h"
#include "type-hierarchy/bound-method.h"
#include "type-hierarchy/type.h"

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

__MPyObj *__mpy_str_func_str_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("str.__str__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    return __mpy_obj_return(self);
}

__MPyObj *__mpy_str_func_bool_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("str.__bool__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    __MPyStrContent *content = self->content;

    __mpy_boolean_c_type truth = strlen(content->string);

    __mpy_obj_ref_dec(self);
    return __mpy_obj_return(__mpy_obj_init_boolean(truth));
}

__MPyObj* __mpy_str_func_add_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("str.__add__", args, kwargs, 2);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __MPyObj *other = __mpy_args_get_positional(&argHelper, 1, "self");
    __mpy_args_finish(&argHelper);

    if (self->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__add__ cannot be called on type '%s'.\n", __mpy_type_name(self->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    if (other->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__add__ cannot concat type '%s'.\n", __mpy_type_name(other->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }

    const char *valueSelf = ((__MPyStrContent*)self->content)->string;
    const char *valueOther = ((__MPyStrContent*)other->content)->string;

    size_t lenSelf = strlen(valueSelf);
    size_t concatLen = lenSelf + strlen(valueOther);
    char *concat = __mpy_checked_malloc(concatLen + 1); // +1 for 0-byte

    strcpy(concat, valueSelf);
    strcpy(concat + lenSelf, valueOther);

    __MPyObj *result = __mpy_obj_init_str_dynamic(concat);

    __mpy_obj_ref_dec(self);
    __mpy_obj_ref_dec(other);
    return __mpy_obj_return(result);
}

__MPyObj *__mpy_str_func_int_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("str.__int__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    const char *value = ((__MPyStrContent*)self->content)->string;

    char *error_pointer;
    long long int_value;

    int_value = strtol(value,&error_pointer,0);

    if (*error_pointer != 0) {
    fprintf(stderr, "ParseError: in function str.__int__ with string '%s' at position '%s'.\n", value, error_pointer);
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    return __mpy_obj_return(__mpy_obj_init_int(int_value));
}

__MPyObj *__mpy_str_func_eq_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("str.__eq__", args, kwargs, 2);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __MPyObj *other = __mpy_args_get_positional(&argHelper, 1, "self");
    __mpy_args_finish(&argHelper);

    if (self->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__eq__ cannot be called on type '%s'.\n", __mpy_type_name(self->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    if (other->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__eq__ cannot concat type '%s'.\n", __mpy_type_name(other->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    const char *valueSelf = ((__MPyStrContent*)self->content)->string;
    const char *valueOther = ((__MPyStrContent*)other->content)->string;
    if (strcmp(valueSelf, valueOther) == 0) {
        return __mpy_obj_return(__mpy_obj_init_boolean(true));
    }
    return __mpy_obj_return(__mpy_obj_init_boolean(false));
}

__MPyObj *__mpy_str_func_ne_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("str.__ne__", args, kwargs, 2);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __MPyObj *other = __mpy_args_get_positional(&argHelper, 1, "self");
    __mpy_args_finish(&argHelper);

    if (self->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__ne__ cannot be called on type '%s'.\n", __mpy_type_name(self->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    if (other->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__ne__ cannot concat type '%s'.\n", __mpy_type_name(other->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    const char *valueSelf = ((__MPyStrContent*)self->content)->string;
    const char *valueOther = ((__MPyStrContent*)other->content)->string;
    if (strcmp(valueSelf, valueOther) != 0) {
        return __mpy_obj_return(__mpy_obj_init_boolean(true));
    }
    return __mpy_obj_return(__mpy_obj_init_boolean(false));
}

__MPyObj *__mpy_str_func_ge_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("str.__ge__", args, kwargs, 2);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __MPyObj *other = __mpy_args_get_positional(&argHelper, 1, "self");
    __mpy_args_finish(&argHelper);

    if (self->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__ge__ cannot be called on type '%s'.\n", __mpy_type_name(self->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    if (other->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__ge__ cannot concat type '%s'.\n", __mpy_type_name(other->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    const char *valueSelf = ((__MPyStrContent*)self->content)->string;
    const char *valueOther = ((__MPyStrContent*)other->content)->string;
    if (strcmp(valueSelf, valueOther) >= 0) {
        return __mpy_obj_return(__mpy_obj_init_boolean(true));
    }
    return __mpy_obj_return(__mpy_obj_init_boolean(false));
}

__MPyObj *__mpy_str_func_le_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("str.__le__", args, kwargs, 2);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __MPyObj *other = __mpy_args_get_positional(&argHelper, 1, "self");
    __mpy_args_finish(&argHelper);

    if (self->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__le__ cannot be called on type '%s'.\n", __mpy_type_name(self->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    if (other->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__le__ cannot concat type '%s'.\n", __mpy_type_name(other->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    const char *valueSelf = ((__MPyStrContent*)self->content)->string;
    const char *valueOther = ((__MPyStrContent*)other->content)->string;
    if (strcmp(valueSelf, valueOther) <= 0) {
        return __mpy_obj_return(__mpy_obj_init_boolean(true));
    }
    return __mpy_obj_return(__mpy_obj_init_boolean(false));
}

__MPyObj *__mpy_str_func_gt_impl(__MPyObj *args, __MPyObj *kwargs){
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("str.__gt__", args, kwargs, 2);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __MPyObj *other = __mpy_args_get_positional(&argHelper, 1, "self");
    __mpy_args_finish(&argHelper);

    if (self->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__gt__ cannot be called on type '%s'.\n", __mpy_type_name(self->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    if (other->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__gt__ cannot concat type '%s'.\n", __mpy_type_name(other->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    const char *valueSelf = ((__MPyStrContent*)self->content)->string;
    const char *valueOther = ((__MPyStrContent*)other->content)->string;
    if (strcmp(valueSelf, valueOther) > 0) {
        return __mpy_obj_return(__mpy_obj_init_boolean(true));
    }
    return __mpy_obj_return(__mpy_obj_init_boolean(false));
}

__MPyObj *__mpy_str_func_lt_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("str.__lt__", args, kwargs, 2);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __MPyObj *other = __mpy_args_get_positional(&argHelper, 1, "self");
    __mpy_args_finish(&argHelper);

    if (self->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__lt__ cannot be called on type '%s'.\n", __mpy_type_name(self->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    if (other->type != __MPyType_Str) {
        fprintf(stderr, "TypeError: str.__lt__ cannot concat type '%s'.\n", __mpy_type_name(other->type));
        __mpy_fatal_error(__MPY_ERROR_USER);
    }
    const char *valueSelf = ((__MPyStrContent*)self->content)->string;
    const char *valueOther = ((__MPyStrContent*)other->content)->string;
    if (strcmp(valueSelf, valueOther) < 0) {
        return __mpy_obj_return(__mpy_obj_init_boolean(true));
    }
    return __mpy_obj_return(__mpy_obj_init_boolean(false));
}

__MPyObj* __mpy_str_set_attr_impl(__MPyObj *self, const char *name, __MPyObj *value) {
    __MPY_TODO("set attr");
    return NULL;
}

__MPyObj* __mpy_str_get_attr_impl(__MPyObj *self, const char *name) {
    if (strcmp("__str__", name) == 0) {
        return ((__MPyStrContent*)self->content)->strMethod;
    }
    if (strcmp("__bool__", name) == 0) {
        return ((__MPyStrContent*)self->content)->boolMethod;
    }
    if (strcmp("__add__", name) == 0) {
        return ((__MPyStrContent*)self->content)->addMethod;
    }
    if (strcmp("__int__", name) == 0) {
        return ((__MPyStrContent*)self->content)->intMethod;
    }

    //comparing
    if (strcmp("__eq__", name) == 0) {
        return ((__MPyStrContent*)self->content)->eqMethod;
    }
    if (strcmp("__ne__", name) == 0) {
        return ((__MPyStrContent*)self->content)->neMethod;
    }
    if (strcmp("__ge__", name) == 0) {
        return ((__MPyStrContent*)self->content)->geMethod;
    }
    if (strcmp("__le__", name) == 0) {
        return ((__MPyStrContent*)self->content)->leMethod;
    }
    if (strcmp("__gt__", name) == 0) {
        return ((__MPyStrContent*)self->content)->gtMethod;
    }
    if (strcmp("__lt__", name) == 0) {
        return ((__MPyStrContent*)self->content)->ltMethod;
    }
    return NULL;
}

void __mpy_obj_cleanup_str(__MPyObj *self) {
    __MPyStrContent *content = (__MPyStrContent*) self->content;
    if (!content->isStatic) {
        // we know the string is dynamically allocated if isStatic == false
        // therefore the cast below is ok
        free((char*) content->string);
    }
}

__MPyObj* __mpy_obj_init_str_static(const char *string) {
    __MPyObj *self = __mpy_obj_new();
    self->type = __MPyType_Str;
    self->cleanupAction = __mpy_obj_cleanup_str;
    self->attrAccessor = __mpy_str_get_attr_impl;
    self->attrSetter = __mpy_str_set_attr_impl;

    self->content = __mpy_checked_malloc(sizeof(__MPyStrContent));
    __MPyStrContent *content = (__MPyStrContent*) self->content;
    content->isStatic = true;
    content->string = string;

    content->boolMethod = __mpy_bind_func(__MPyFunc_Str_bool, self);
    content->strMethod = __mpy_bind_func(__MPyFunc_Str_str, self);
    content->addMethod = __mpy_bind_func(__MPyFunc_Str_add, self);
    content->intMethod = __mpy_bind_func(__MPyFunc_Str_int, self);

// comparing
    content->eqMethod = __mpy_bind_func(__MPyFunc_Str_eq, self);
    content->neMethod = __mpy_bind_func(__MPyFunc_Str_ne, self);
    content->geMethod = __mpy_bind_func(__MPyFunc_Str_ge, self);
    content->leMethod = __mpy_bind_func(__MPyFunc_Str_le, self);
    content->gtMethod = __mpy_bind_func(__MPyFunc_Str_gt, self);
    content->ltMethod = __mpy_bind_func(__MPyFunc_Str_lt, self);

    return __mpy_obj_return(self);
}

__MPyObj* __mpy_obj_init_str_dynamic(char *string) {
    __MPyObj *self = __mpy_obj_init_str_static(string);
    ((__MPyStrContent*)self->content)->isStatic = false;

    return self;
}

const char* __mpy_str_as_c_str(__MPyObj *self) {
    // FIXME type check
    __MPY_NOTE("(bug) str_as_c_str: missing typecheck");
    return ((__MPyStrContent*)self->content)->string;
}
