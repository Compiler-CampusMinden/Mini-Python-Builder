#include "mpy_obj.h"

#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include <assert.h>

#include "builtins-setup.h"
#include "errors.h"
#include "checks.h"
#include "type-hierarchy/type.h"

static unsigned int __MPyObjCount = 0;

__MPyObj* __mpy_obj_return(__MPyObj *obj) {
    assert(obj != NULL);

    obj->temporary = true;
    return obj; // return obj to allow chaining
}

__MPyObj* __mpy_obj_new() {
#ifdef MINI_PYTHON_DEBUG
    fprintf(stderr, "DEBUG: __mpy_obj_new: creating new object with id '%d'\n", __MPyObjCount);
#endif

    // FIXME create None implementation and use that in builder....
    __MPyObj *obj =  __mpy_checked_malloc(sizeof(__MPyObj));
    obj->id =  __MPyObjCount++;
    obj->content = NULL;
    obj->refCount = 1;
    obj->temporary = false;
    obj->cleanupAction = NULL;
    obj->parent = NULL;

    return obj;
}

void __mpy_obj_ref_inc(__MPyObj *obj) {
    assert(obj != NULL);

    if (obj->temporary) {
#ifdef MINI_PYTHON_DEBUG
        fprintf(stderr, "DEBUG: __mpy_obj_ref_inc(%d): leaving refCount at '%d' for temporary object\n", obj->id, obj->refCount);
#endif
        obj->temporary = false;
    } else {
#ifdef MINI_PYTHON_DEBUG
        fprintf(stderr, "DEBUG: __mpy_obj_ref_inc(%d): increasing refCount from '%d' to '%d'\n", obj->id, obj->refCount, obj->refCount + 1);
#endif
        obj->refCount += 1;
    }
}

void __mpy_obj_ref_dec(__MPyObj *obj) {
    assert(obj != NULL);

#ifdef MINI_PYTHON_DEBUG
    fprintf(stderr, "DEBUG: __mpy_obj_ref_dec(%d): decreasing refCount from '%d' to '%d'\n", obj->id, obj->refCount, obj->refCount - 1);
#endif
    obj->refCount -= 1;
    obj->temporary = false;

    if (obj->refCount == 0) {
        if (obj->parent != NULL) {
            // assumption: no recursive parent relationships possible
            // if that assumption does not hold here, we infinetely recurse now
            __mpy_obj_ref_dec(obj->parent);
        }

        if (obj->cleanupAction != NULL) {
#ifdef MINI_PYTHON_DEBUG
            fprintf(stderr, "DEBUG: __mpy_obj_ref_dec(%d): running custom cleanup action\n", obj->id);
#endif
            obj->cleanupAction(obj);
        }

        if (obj->content != NULL) {
#ifdef MINI_PYTHON_DEBUG
            fprintf(stderr, "DEBUG: __mpy_obj_ref_dec(%d): freeing content\n", obj->id);
#endif
            free(obj->content);
            obj->content = NULL;
        }

#ifdef MINI_PYTHON_DEBUG
        fprintf(stderr, "DEBUG: __mpy_obj_ref_dec(%d): freeing self\n", obj->id);
#endif
        free(obj);
    }
}

__MPyObj* __mpy_obj_get_attr_rec(__MPyObj *self, const char *name, const char *typeName) {
    __MPyObj *attr = (self->attrAccessor)(self, name);
    if (attr != NULL) {
        return attr;
    }

    __MPyObj *typeAttr = (self->type->attrAccessor)(self->type, name);
    if (typeAttr != NULL) {
        return typeAttr;
    }

    // yes, although setting always sets on the object itself,
    // and not on any parent or stuff (try it, create a class with some method,
    // another method that modifies self.<methodName> and a subclass overriding that method
    // and try calling the method after using the modifier method....)
    // we need to look up the class attributes and parent for attributes initialized in the parent,
    // static functions and non-overriden functions
    
    // note: the recursion (or iteration, if recursion turns out to be suboptimal)
    // may need to cache the looked up classes, since in theory python3's MRO allows recursive inheritance
    // relationships
    // (this does currently not apply, since multiple inheritance is unsupported)
    if (self->parent != NULL) {
        return __mpy_obj_get_attr_rec(self->parent, name, typeName);
    }

    fprintf(stderr, "AttributeError: '%s' object has no attribute '%s'.\n", typeName, name);
    __mpy_fatal_error(__MPY_ERROR_USER);
}

__MPyObj* __mpy_obj_get_attr(__MPyObj *self, const char* name) {
    assert(self != NULL);
    return __mpy_obj_get_attr_rec(self, name, __mpy_type_name(self->type));
}

__MPyObj* __mpy_obj_set_attr(__MPyObj *self, const char *name, __MPyObj *value) {
    assert(self != NULL && name != NULL && value != NULL);

    return (self->attrSetter)(self, name, value);
}

/*************************
 * END: MPyObj.c *
 *************************/
