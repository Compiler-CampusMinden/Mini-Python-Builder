#ifndef MPY_OBJ_H
#define MPY_OBJ_H

#include "stdbool.h"

/**
 * @file 
 */

/**
 * Representation of a Mini-Python object.
 *
 * Keeps track of references to it and automatically deallocates content when not used anymore.
 * Warning: Take a close look at the functions below for how to use this struct.
 */

struct __MPyObj;
typedef struct __MPyObj __MPyObj;

typedef void (*__mpy_obj_cleanup_action)(__MPyObj *self);

/**
 * @param self Instance to query the attribute from.
 * @param name Name of the attribute to get.
 * @return The value of the attribute or NULL if the object does not have an attribute named name.
 */
typedef __MPyObj* (*__mpy_object_get_attr) (__MPyObj *self, const char *name);

/**
 * @param self Instance to modify the attribute on.
 * @param name Name of the attribute to set.
 * @param value New value of the attribute.
 * @return self to allow chaining with this method.
 */
typedef __MPyObj* (*__mpy_object_set_attr) (__MPyObj *self, const char *name, __MPyObj *value);

struct __MPyObj {
    /**
     * unique id of the object
     */
    unsigned int id; 
    /**
     * pointer to the content of the object - managed by type specific implementation
     */
    void *content; 
    /**
     * pointer to the refCount - *not* managed by type specific implementation
     */
    unsigned int refCount; 
    /**
     * pointer to the objects type
     */
    __MPyObj *type; 
    /**
     * allow an object to temporarily keep its content longer than refCount implies 
     * by setting this to true instead of decrementing refCount
     */
    bool temporary; 
    /**
     * Allow builtin objects with additional allocations inside content to deallocate them.
     */
    __mpy_obj_cleanup_action cleanupAction;
    /**
     * Access an attribute of this object.
     */
    __mpy_object_get_attr attrAccessor;
    /**
     * Set an attribute of this object.
     */
    __mpy_object_set_attr attrSetter;
    /**
     * The parent classes instance.
     *
     * May be null, since object has no base type.
     *
     * Note that this is *not* the type of the parent class, but an instance of the parent class.
     */
    __MPyObj *parent;
};

/**
 * This function *must* be called before returning a __MPyObj from a function.
 */
__MPyObj* __mpy_obj_return(__MPyObj*);

/**
 * Initialises a new __MPyObj.
 */
__MPyObj* __mpy_obj_new();

/**
 * Increment the ref count if the object is used in another context.
 */
void __mpy_obj_ref_inc(__MPyObj *obj);

/**
 * Decrement ref count if an object is not used anymore.
 *
 * Performs de-allocation of memory allocated for the object.
 */
void __mpy_obj_ref_dec(__MPyObj *obj);

__MPyObj* __mpy_obj_get_attr(__MPyObj *self, const char* name);

__MPyObj* __mpy_obj_set_attr(__MPyObj *self, const char *name, __MPyObj *value);

#endif
