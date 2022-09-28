
#include <stdbool.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>

#include "literals/boolean.h"
#include "mpy_obj.h"
#include "builtins-setup.h"
#include "checks.h"
#include "errors.h"
#include "function-args.h"
#include "type-hierarchy/bound-method.h"

typedef struct __MPyStrContent {
    bool isStatic;
    const char *string;
    __MPyObj *strMethod;
    __MPyObj *boolMethod;
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
