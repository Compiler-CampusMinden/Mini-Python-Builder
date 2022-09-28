#include "type-hierarchy/type.h"

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <assert.h>

#include "type-hierarchy/bound-method.h"
#include "mpy_obj.h"
#include "errors.h"
#include "checks.h"
#include "builtins-setup.h"
#include "literals/str.h"
#include "type-hierarchy/object.h"
#include "literals/tuple.h"
#include "function-args.h"
#include "simple_hash_map.h"

typedef struct __MPyTypeContent {
    const char *name;
    bool builtin;
    __MPyHashMap *attributes; //str->__MPyObj*
    /**
     * The parent *type* of this class, e. g. another type object.
     *
     * TODO maybe calling this 'super' makes it easier to differntiate
     * between this parent and the parent field of __MPyObj?
     */
    __MPyObj *parent; // no multiple inheritance for now
} __MPyTypeContent;

void cleanup_attribute_entry(const char *_key, __MPyObj* value) {
    (void)_key;
    __mpy_obj_ref_dec(value);
}

void __mpy_obj_cleanup_type(__MPyObj *self) {
    __MPyTypeContent *content = (__MPyTypeContent*) self->content;

    if (content->attributes != NULL) {
        __mpy_hash_map_iter(content->attributes, (void (*)(void*,void*)) cleanup_attribute_entry);
        __mpy_hash_map_clear(content->attributes);
        free(content->attributes);
    }
}

__MPyObj* __mpy_obj_init_type_builtin(const char *name, __MPyObj *self, __MPyHashMap *attributes, __MPyObj *parentType) {
    self->type = __MPyType_Type;
    self->cleanupAction = __mpy_obj_cleanup_type;
    self->attrAccessor = __mpy_type_get_attr_impl;
    self->attrSetter = __mpy_type_set_attr_impl;
    self->parent = __mpy_obj_init_object(); // this 'parent' is an instance of this classes parent type (i.e. *always* object)

    self->content = __mpy_checked_malloc(sizeof(__MPyTypeContent));
    __MPyTypeContent *content = (__MPyTypeContent*) self->content;
    content->name = name;
    content->builtin = true;
    content->attributes = attributes;
    content->parent = parentType; // while this 'parent' is the parent type object of the type this instance represents (i. e. dynamic and determined by the caller)

    return __mpy_obj_return(self);
}

__MPyObj* __mpy_obj_init_type(const char *name, __MPyObj *parentType) {
    __MPyObj *self = __mpy_obj_new();
    __mpy_obj_init_type_builtin(name, self, __mpy_hash_map_init(&__mpy_hash_map_str_key_cmp), parentType);

    __MPyTypeContent *content = (__MPyTypeContent*) self->content;
    content->builtin = false;

    return __mpy_obj_return(self);
}

const char* __mpy_type_name(__MPyObj *self) {
    assert(self != NULL && self->type == __MPyType_Type);

    return ((__MPyTypeContent*)self->content)->name;
}

__MPyObj *__mpy_type_set_attr_impl(__MPyObj *self, const char *name, __MPyObj *value) {
    __MPyTypeContent *content = (__MPyTypeContent*) self->content;

    if (content->builtin) {
        fprintf(stderr, "TypeError: can't set attributes of builtin type '%s'\n", content->name);
        __mpy_fatal_error(__MPY_ERROR_USER);
    } 

    __MPyObj *previousValue = __mpy_hash_map_put(content->attributes, (void*) name, value);
    __mpy_obj_ref_inc(value);

    if (previousValue != NULL) {
        __mpy_obj_ref_dec(previousValue);
    }

    return self;
}

__MPyObj *__mpy_type_get_attr_impl(__MPyObj *self, const char *name) {
    __MPyTypeContent *content = (__MPyTypeContent*) self->content;
    __MPyObj *attr = __mpy_hash_map_get(content->attributes, (void*) name);
    if (attr != NULL) {
        return __mpy_obj_return(attr);
    }

    return NULL;
}

__MPyObj *__mpy_type_func_str_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("type.__str__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    __mpy_obj_ref_dec(self);
    return __mpy_obj_return(__mpy_obj_init_str_static(((__MPyTypeContent*)self->content)->name));
}

// NOLINTNEXTLINE(bugprone-easily-swappable-parameters)
void bind_method(void *_attrName, void *_attrValue, void *_newInstance) {
    const char *attrName = _attrName;
    __MPyObj *attrValue = _attrValue;
    __MPyObj *newInstance = _newInstance;

    if (attrValue->type == __MPyType_Function) {
        (void)*(newInstance->attrSetter)(newInstance, attrName, __mpy_bind_func(attrValue, newInstance));
    }
}

__MPyObj *__mpy_type_func_call_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("type.__call__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    assert(self->type == __MPyType_Type);
    __MPyTypeContent *content = self->content;

    __MPyObj *instance = __mpy_obj_init_object();
    instance->type = self;
    /* instance->parent = parentInstance; */

    __mpy_hash_map_iter_data(content->attributes, bind_method, instance);

    __mpy_obj_ref_dec(self);
    return instance;
}

__MPyObj* __mpy_type_get_parent_type(__MPyObj *self) {
    assert(self->type == __MPyType_Type);
    __MPyTypeContent *content = self->content;
    assert(content->parent != NULL);
    return content->parent;
}

/** maybe not needed, since the builder could also simply use the internal methods,
 * since we do not have to necessarily expose type creation for mini-python
 */
/* __MPyObj* __mpy_type_func_init_impl(__MPyObj *args, __MPyObj *kwargs) { */
/*     assert(args != NULL && kwargs != NULL); */

/*     __MPyGetArgsState argHelper = __mpy_args_init("type.__init__", args, kwargs, 2); */
/*     __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self"); */
/*     __MPyObj *name = __mpy_args_get_positional(&argHelper, 0, "name"); */
/*     __mpy_args_finish(&argHelper); */

    
/* } */
