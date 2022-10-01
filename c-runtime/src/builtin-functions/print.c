#include "builtin-functions/print.h"

#include <assert.h>
#include <stdio.h>

#include "errors.h"
#include "literals/str.h"
#include "literals/tuple.h"
#include "mpy_obj.h"
#include "type-hierarchy/function.h"
#include "type-hierarchy/object.h"

__MPyObj* __mpy_func_print(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    // FIXME check for empty kwargs (or use them correctly)
    __MPY_NOTE("(bug) ignoring kwargs");
    if (kwargs != NULL) {
        __mpy_obj_ref_dec(kwargs);
    }

    unsigned int argNr = 0;
    if (args != NULL) {
        argNr = __mpy_tuple_size(args);
    }

    for (unsigned int i = 0; i < argNr; i++) {
        __MPyObj *obj = __mpy_tuple_get_at(args, i);
        __mpy_obj_ref_inc(obj);

        __MPyObj *str = __mpy_call(__mpy_obj_get_attr(obj, "__str__"), __mpy_obj_init_tuple(0), NULL);

        const char *end = " ";
        if (i + 1 == argNr) {
            end = "\n";
        }
        printf("%s%s", __mpy_str_as_c_str(str), end);

        __mpy_obj_ref_dec(str);
        __mpy_obj_ref_dec(obj);
    }

    __mpy_obj_ref_dec(args);
    // FIXME return None
    return __mpy_obj_return(__mpy_obj_init_object());
}

