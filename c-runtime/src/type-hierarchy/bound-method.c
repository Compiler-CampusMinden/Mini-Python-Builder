#include "type-hierarchy/bound-method.h"

#include "builtins-setup.h"
#include "checks.h"

void cleanup_bound_method(__MPyObj *self) {
    __MPyBoundMethodContent *content = self->content;

    (void)content;
    __mpy_obj_ref_dec(content->func);
    __mpy_obj_ref_dec(content->instance);
}

// NOLINTNEXTLINE(bugprone-easily-swappable-parameters)
__MPyObj* __mpy_bind_func(__MPyObj *func, __MPyObj *instance) {
    __MPyObj *obj = __mpy_obj_new();
    obj->type = __MPyType_BoundMethod;
    obj->content = __mpy_checked_malloc(sizeof(__MPyBoundMethodContent));
    obj->cleanupAction = cleanup_bound_method;

    __MPyBoundMethodContent *content = obj->content;
    __mpy_obj_ref_inc(func);
    content->func = func;
    // FIXME(florian): this creates a cyclic reference ://///
    // Idea: save a list of cyclic referencees for each object
    // and add this method to this list
    // When calling ref_dec on the object (not this method),
    // count the number of cyclic referenceees with ref_count = 1 (for the simple case of those not being abel to have cyclic referenceees)
    // and compare with refCount. If equal -> deallocated even though refCount >0
    //
    // Note: currently the reference handling is somehow broken, causing
    // the bound method to be freed to early if the reference cycle is broken.
    // (although that may only occurr if the reference cycle is broken by simply not
    // increment/decrementing the count for `instance`)
    __mpy_obj_ref_inc(instance);
    content->instance = instance;

    return __mpy_obj_return(obj);
}
