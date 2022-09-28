#include "type-hierarchy/object.h"

#include <string.h>
#include <stdlib.h>
#include <assert.h>

#include "mpy_obj.h"
#include "builtins-setup.h"
#include "simple_hash_map.h"
#include "checks.h"
#include "errors.h"
#include "type-hierarchy/type.h"
#include "literals/boolean.h"
#include "literals/str.h"
#include "literals/tuple.h"
#include "function-args.h"

__MPyObj* __mpy_object_func_str_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("object.__str__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    unsigned int id = self->id;
    size_t digits = 1; // at least one digit
    while (id > 9) {
        digits++;
        id = id / 10;
    }

    const char *stringStart = "<";
    const char *typeName = __mpy_type_name(self->type);
    const char *stringObject = " object ";
    const char *stringEnd = ">";

    size_t strLen = strlen(stringStart) + strlen(typeName) + strlen(stringObject) + strlen(stringEnd) + digits;
    char *string = __mpy_checked_malloc(sizeof(char) * strLen);
    snprintf(string, strLen, "%s%s%s%u%s", stringStart, typeName, stringObject, self->id, stringEnd);

    return __mpy_obj_return(__mpy_obj_init_str_dynamic(string));
}

void cleanup_attr_entry(const char *name, __MPyObj* obj) {
    // key is char*, so not eligible for cleanup

    // but the value, obj, is
    __mpy_obj_ref_dec(obj);
}

void cleanup_object(__MPyObj *self) {
    __MPyHashMap *map = (__MPyHashMap*) self->content;

    __mpy_hash_map_iter(map, (void (*)(void*,void*)) cleanup_attr_entry);
    __mpy_hash_map_clear(map);

    if (self->parent != NULL) {
        __mpy_obj_ref_dec(self->parent);
    }
}

__MPyObj *__mpy_object_set_attr_impl(__MPyObj *self, const char *name, __MPyObj *value) {
    __MPyObj *previousValue = __mpy_hash_map_put(self->content, (void*) name, value);
    __mpy_obj_ref_inc(value);

    if (previousValue != NULL) {
        __mpy_obj_ref_dec(previousValue);
    }

    return self;
}

__MPyObj *__mpy_object_get_attr_impl(__MPyObj *self, const char *name) {
    return __mpy_hash_map_get(self->content, (void*) name);
}

__MPyObj *__mpy_obj_init_object() {
    __MPyObj *obj = __mpy_obj_new();
    obj->type = __MPyType_Object;
    obj->content = __mpy_hash_map_init(&__mpy_hash_map_str_key_cmp);
    obj->cleanupAction = cleanup_object;
    obj->attrSetter = __mpy_object_set_attr_impl;
    obj->attrAccessor = __mpy_object_get_attr_impl;

    return __mpy_obj_return(obj);
}

// TODO finish implementation of this method and use it
// for object initialisation in call.c instead of directly calling __call__ there
__MPyObj* __mpy_object_func_new_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    // cls (pos 0, name cls) needs to be the class (type) of the object
    // rest is ignored (for now)
    __MPyGetArgsState argHelper = __mpy_args_init("type.__new__", args, kwargs, 1);
    __MPyObj *cls = __mpy_args_get_positional(&argHelper, 0, "cls");
    __mpy_args_finish(&argHelper);

    __MPyObj *fnCall = __mpy_obj_get_attr(cls, "__call__");
    __MPyObj *obj = __mpy_call(fnCall, __mpy_tuple_assign(0, cls, __mpy_obj_init_tuple(1)), NULL);

    __mpy_obj_ref_dec(cls);
    return __mpy_obj_return(obj);
}

__MPyObj *__mpy_object_func_init_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("object.__init__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    __mpy_obj_ref_dec(self);

    return __mpy_obj_return(__mpy_obj_init_object());
}

__MPyObj *__mpy_object_func_bool_impl(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("object.__init__", args, kwargs, 1);
    __MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
    __mpy_args_finish(&argHelper);

    // TODO(florian) https://docs.python.org/3/reference/datamodel.html#object.__bool__
    // cpython 3 tries to call __len__ first and only afterwards returns true
    // (although in cpython this method likely is part of the call/callable mechanics and not even implemented as aprt of the object cflass...)

    __mpy_obj_ref_dec(self);

    return __mpy_obj_return(__mpy_obj_init_boolean(true));
}
