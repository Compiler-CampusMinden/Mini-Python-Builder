
#include "builtins-setup.h"

#include <string.h>

#include "builtin-functions/super.h"
#include "literals/boolean.h"
#include "literals/tuple.h"
#include "mpy_obj.h"
#include "builtin-functions/id.h"
#include "builtin-functions/print.h"
#include "builtin-functions/type.h"
#include "builtin-functions/input.h"
#include "simple_hash_map.h"
#include "type-hierarchy/bound-method.h"
#include "type-hierarchy/type.h"
#include "type-hierarchy/object.h"
#include "literals/str.h"
#include "literals/int.h"

__MPyObj *__MPyType_Object;

__MPyObj *__MPyType_Num;

__MPyObj *__MPyType_Tuple;

__MPyObj *__MPyType_Function;

__MPyObj *__MPyType_BoundMethod;

__MPyObj *__MPyType_None;

__MPyObj *__MPyType_Type;

__MPyObj *__MPyType_Str;

__MPyObj *__MPyType_Boolean;

__MPyObj *__MPyFunc_id;

__MPyObj *__MPyFunc_print;

__MPyObj *__MPyFunc_type;

__MPyObj *__MPyFunc_input;

__MPyObj *__MPyFunc_Type_str;

__MPyObj *__MPyFunc_Type_call;

__MPyObj *__MPyFunc_Int_str;

__MPyObj *__MPyFunc_Int_bool;

__MPyObj *__MPyFunc_Int_add;

__MPyObj *__MPyFunc_Int_sub;

__MPyObj *__MPyFunc_Int_mul;

__MPyObj *__MPyFunc_Int_div;

__MPyObj *__MPyFunc_Int_lshift;

__MPyObj *__MPyFunc_Int_rshift;

__MPyObj *__MPyFunc_Int_and;

__MPyObj *__MPyFunc_Int_xor;

__MPyObj *__MPyFunc_Int_or;


__MPyObj *__MPyFunc_Int_eq;

__MPyObj *__MPyFunc_Int_ne;

__MPyObj *__MPyFunc_Int_ge;

__MPyObj *__MPyFunc_Int_le;

__MPyObj *__MPyFunc_Int_gt;

__MPyObj *__MPyFunc_Int_lt;


__MPyObj *__MPyFunc_Tuple_bool;

__MPyObj *__MPyFunc_Str_str;

__MPyObj *__MPyFunc_Str_bool;

__MPyObj *__MPyFunc_Str_add;

__MPyObj *__MPyFunc_Str_int;

// compare strings
__MPyObj *__MPyFunc_Str_eq;
__MPyObj *__MPyFunc_Str_ne;
__MPyObj *__MPyFunc_Str_ge;
__MPyObj *__MPyFunc_Str_le;
__MPyObj *__MPyFunc_Str_gt;
__MPyObj *__MPyFunc_Str_lt;


__MPyObj *__MPyFunc_Boolean_str;

__MPyObj *__MPyFunc_Boolean_bool;

__MPyObj *__MPyFunc_Boolean_int;

// compare boolean
__MPyObj *__MPyFunc_Boolean_eq;
__MPyObj *__MPyFunc_Boolean_ne;


__MPyObj *__MPyFunc_Object_str;

__MPyObj *__MPyFunc_Object_new;

__MPyObj *__MPyFunc_Object_init;

__MPyObj *__mpy_super;

void __mpy_builtins_setup() {
    // idea: since the basic parts of the type hierarchy recursively depend on one another
    // (e. g. type inherits from object, but object itself is an instance of the type class)
    // first init the base MpyObj and afterwards initialise the type so all pointers point in the correct location
    //
    // Note the order of init_type and ref_inc: doing it otherwise would lead to problems, since init_type
    // finalises initialisation work needed for ref_inc to work correctly.
    __MPyType_Object = __mpy_obj_new();

    __MPyType_Num = __mpy_obj_new();

    __MPyType_Tuple = __mpy_obj_new();

    __MPyType_Function = __mpy_obj_new();

    __MPyType_BoundMethod = __mpy_obj_new();

    __MPyType_None = __mpy_obj_new();

    __MPyType_Type = __mpy_obj_new();

    __MPyType_Str = __mpy_obj_new();

    __MPyType_Boolean = __mpy_obj_new();

    __MPyFunc_id = __mpy_obj_init_func(&__mpy_func_id);
    __mpy_obj_ref_inc(__MPyFunc_id);

    __MPyFunc_print = __mpy_obj_init_func(&__mpy_func_print);
    __mpy_obj_ref_inc(__MPyFunc_print);

    __MPyFunc_type = __mpy_obj_init_func(&__mpy_func_type);
    __mpy_obj_ref_inc(__MPyFunc_type);

    __MPyFunc_input = __mpy_obj_init_func(&__mpy_func_input);
    __mpy_obj_ref_inc(__MPyFunc_input);

    __MPyFunc_Type_str = __mpy_obj_init_func(&__mpy_type_func_str_impl);
    __mpy_obj_ref_inc(__MPyFunc_Type_str);

    __MPyFunc_Type_call = __mpy_obj_init_func(&__mpy_type_func_call_impl);
    __mpy_obj_ref_inc(__MPyFunc_Type_call);

#define init_int_func(purpose) \
    __MPyFunc_Int_ ## purpose = __mpy_obj_init_func(&__mpy_int_func_ ## purpose ## _impl); \
    __mpy_obj_ref_inc(__MPyFunc_Int_ ## purpose);

    init_int_func(str);

    init_int_func(bool);

    init_int_func(add);

    init_int_func(sub);

    init_int_func(mul);

    init_int_func(div);

    init_int_func(lshift);

    init_int_func(rshift);

    init_int_func(and);

    init_int_func(or);

    init_int_func(xor);


    init_int_func(eq);

    init_int_func(ne);

    init_int_func(ge);

    init_int_func(le);

    init_int_func(gt);

    init_int_func(lt);


#undef init_int_func

    __MPyFunc_Tuple_bool = __mpy_obj_init_func(&__mpy_tuple_func_bool_impl);
    __mpy_obj_ref_inc(__MPyFunc_Tuple_bool);

    __MPyFunc_Str_str = __mpy_obj_init_func(&__mpy_str_func_str_impl);
    __mpy_obj_ref_inc(__MPyFunc_Str_str);

    __MPyFunc_Str_bool = __mpy_obj_init_func(&__mpy_str_func_bool_impl);
    __mpy_obj_ref_inc(__MPyFunc_Str_bool);

    __MPyFunc_Str_add = __mpy_obj_init_func(&__mpy_str_func_add_impl);
    __mpy_obj_ref_inc(__MPyFunc_Str_add);

    __MPyFunc_Str_int = __mpy_obj_init_func(&__mpy_str_func_int_impl);
    __mpy_obj_ref_inc(__MPyFunc_Str_int);

// comparing string
    __MPyFunc_Str_eq = __mpy_obj_init_func(&__mpy_str_func_eq_impl);
    __mpy_obj_ref_inc(__MPyFunc_Str_eq);
    __MPyFunc_Str_ne = __mpy_obj_init_func(&__mpy_str_func_ne_impl);
    __mpy_obj_ref_inc(__MPyFunc_Str_ne);
    __MPyFunc_Str_ge = __mpy_obj_init_func(&__mpy_str_func_ge_impl);
    __mpy_obj_ref_inc(__MPyFunc_Str_ge);
    __MPyFunc_Str_le = __mpy_obj_init_func(&__mpy_str_func_le_impl);
    __mpy_obj_ref_inc(__MPyFunc_Str_le);
    __MPyFunc_Str_gt = __mpy_obj_init_func(&__mpy_str_func_gt_impl);
    __mpy_obj_ref_inc(__MPyFunc_Str_gt);
    __MPyFunc_Str_lt = __mpy_obj_init_func(&__mpy_str_func_lt_impl);
    __mpy_obj_ref_inc(__MPyFunc_Str_lt);

    __MPyFunc_Boolean_bool = __mpy_obj_init_func(&__mpy_boolean_func_bool_impl);
    __mpy_obj_ref_inc(__MPyFunc_Boolean_bool);

    __MPyFunc_Boolean_str = __mpy_obj_init_func(&__mpy_boolean_func_str_impl);
    __mpy_obj_ref_inc(__MPyFunc_Boolean_str);

    __MPyFunc_Boolean_int = __mpy_obj_init_func(&__mpy_boolean_func_int_impl);
    __mpy_obj_ref_inc(__MPyFunc_Boolean_int);

// comparing boolean
    __MPyFunc_Boolean_eq = __mpy_obj_init_func(&__mpy_boolean_func_eq_impl);
    __mpy_obj_ref_inc(__MPyFunc_Boolean_eq);
    __MPyFunc_Boolean_ne = __mpy_obj_init_func(&__mpy_boolean_func_ne_impl);
    __mpy_obj_ref_inc(__MPyFunc_Boolean_ne);

    __MPyFunc_Object_str = __mpy_obj_init_func(&__mpy_object_func_str_impl);
    __mpy_obj_ref_inc(__MPyFunc_Object_str);

    __MPyFunc_Object_new = __mpy_obj_init_func(&__mpy_object_func_new_impl);
    __mpy_obj_ref_inc(__MPyFunc_Object_new);

    __MPyFunc_Object_init = __mpy_obj_init_func(&__mpy_object_func_init_impl);
    __mpy_obj_ref_inc(__MPyFunc_Object_init);

    __MPyHashMap *typeObjectAttrs = __mpy_hash_map_init(&__mpy_hash_map_str_key_cmp);
    __mpy_obj_ref_inc(__MPyFunc_Object_str);
    __mpy_hash_map_put(typeObjectAttrs, "__str__", __MPyFunc_Object_str);
    __mpy_obj_ref_inc(__MPyFunc_Object_new);
    __mpy_hash_map_put(typeObjectAttrs, "__new__", __MPyFunc_Object_new);
    __mpy_obj_ref_inc(__MPyFunc_Object_init);
    __mpy_hash_map_put(typeObjectAttrs, "__init__", __MPyFunc_Object_init);
    __mpy_obj_init_type_builtin("object", __MPyType_Object, typeObjectAttrs, NULL);
    __mpy_obj_ref_inc(__MPyType_Object);

    __mpy_obj_init_type_builtin("num", __MPyType_Num, __mpy_hash_map_init(&__mpy_hash_map_str_key_cmp), __MPyType_Object);
    __mpy_obj_ref_inc(__MPyType_Num);

    __mpy_obj_init_type_builtin("tuple", __MPyType_Tuple, __mpy_hash_map_init(&__mpy_hash_map_str_key_cmp), __MPyType_Object);
    __mpy_obj_ref_inc(__MPyType_Tuple);

    __mpy_obj_init_type_builtin("function", __MPyType_Function, __mpy_hash_map_init(&__mpy_hash_map_str_key_cmp), __MPyType_Object);
    __mpy_obj_ref_inc(__MPyType_Function);

    __mpy_obj_init_type_builtin("bound_method", __MPyType_BoundMethod, __mpy_hash_map_init(&__mpy_hash_map_str_key_cmp), __MPyType_Object);
    __mpy_obj_ref_inc(__MPyType_BoundMethod);

    __mpy_obj_init_type_builtin("none", __MPyType_None, __mpy_hash_map_init(&__mpy_hash_map_str_key_cmp), __MPyType_Object);
    __mpy_obj_ref_inc(__MPyType_None);

    __MPyHashMap *typeTypeAttrs = __mpy_hash_map_init(&__mpy_hash_map_str_key_cmp);
    __mpy_obj_ref_inc(__MPyFunc_Type_call);
    __mpy_hash_map_put(typeTypeAttrs, "__call__", __MPyFunc_Type_call);
    __mpy_obj_ref_inc(__MPyFunc_Type_str);
    __mpy_hash_map_put(typeTypeAttrs, "__str__", __MPyFunc_Type_str);
    __mpy_obj_init_type_builtin("type", __MPyType_Type, typeTypeAttrs, __MPyType_Object);
    __mpy_obj_ref_inc(__MPyType_Type);

    __mpy_obj_init_type_builtin("str", __MPyType_Str, __mpy_hash_map_init(&__mpy_hash_map_str_key_cmp), __MPyType_Object);
    __mpy_obj_ref_inc(__MPyType_Str);

    // FIXME(florian): this should inherit from integral, a common parent type for ints and bools
    // (cf. https://docs.python.org/3/reference/datamodel.html#the-standard-type-hierarchy)
    __mpy_obj_init_type_builtin("bool", __MPyType_Boolean, __mpy_hash_map_init(&__mpy_hash_map_str_key_cmp), __MPyType_Object);
    __mpy_obj_ref_inc(__MPyType_Boolean);

    __mpy_super = __mpy_obj_init_func(__mpy_func_super);
    __mpy_obj_ref_inc(__mpy_super);
}

void __mpy_builtins_cleanup() {
    __mpy_obj_ref_dec(__MPyType_Object);

    __mpy_obj_ref_dec(__MPyType_Num);

    __mpy_obj_ref_dec(__MPyType_Tuple);

    __mpy_obj_ref_dec(__MPyType_Function);

    __mpy_obj_ref_dec(__MPyType_BoundMethod);

    __mpy_obj_ref_dec(__MPyType_None);

    __mpy_obj_ref_dec(__MPyType_Type);

    __mpy_obj_ref_dec(__MPyType_Str);

    __mpy_obj_ref_dec(__MPyType_Boolean);

    __mpy_obj_ref_dec(__MPyFunc_id);

    __mpy_obj_ref_dec(__MPyFunc_print);

    __mpy_obj_ref_dec(__MPyFunc_type);

    __mpy_obj_ref_dec(__MPyFunc_input);

    __mpy_obj_ref_dec(__MPyFunc_Type_str);

    __mpy_obj_ref_dec(__MPyFunc_Type_call);

    __mpy_obj_ref_dec(__MPyFunc_Int_str);

    __mpy_obj_ref_dec(__MPyFunc_Str_str);

    __mpy_obj_ref_dec(__MPyFunc_Str_add);

    __mpy_obj_ref_inc(__MPyFunc_Boolean_bool);

    __mpy_obj_ref_inc(__MPyFunc_Boolean_str);

    __mpy_obj_ref_dec(__MPyFunc_Object_str);

    __mpy_obj_ref_dec(__MPyFunc_Object_new);

    __mpy_obj_ref_dec(__MPyFunc_Object_init);

    __mpy_obj_ref_dec(__mpy_super);
}
