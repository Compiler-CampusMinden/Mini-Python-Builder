#define CATCH_CONFIG_MAIN

#include <stddef.h>

#include "catch_amalgamated.hpp"
#include "test/test_helpers.h"
#include "test/Classes/TestClassInheritanceHelpers.h"

__MPyObj *A;

__MPyObj *B;

int inheritance(){
    __MPyObj *child;

    __mpy_builtins_setup();
    child = __mpy_obj_init_object();
    __mpy_obj_ref_inc(child);


    A = __mpy_obj_init_type("A", __MPyType_Object);
    __mpy_obj_ref_inc(A);
    {
        __MPyObj *__init__;
        __init__ = __mpy_obj_init_func(&func_A___init__);
        __mpy_obj_ref_inc(__init__);
        __mpy_obj_set_attr(A, "__init__", __init__);
        __mpy_obj_ref_dec(__init__);
    }
    B = __mpy_obj_init_type("B", A);
    __mpy_obj_ref_inc(B);
    {
        __MPyObj *__init__;
        __init__ = __mpy_obj_init_func(&func_B___init__);
        __mpy_obj_ref_inc(__init__);
        __mpy_obj_set_attr(B, "__init__", __init__);
        __mpy_obj_ref_dec(__init__);
    }

    __mpy_obj_ref_dec(child);
    child = __mpy_call(B, __mpy_obj_init_tuple(0), NULL);
    __mpy_obj_ref_inc(child);

    __mpy_obj_ref_dec(child);


    __mpy_obj_ref_dec(A);
    __mpy_obj_ref_dec(B);

    __mpy_builtins_cleanup();

    return 0;
}

TEST_CASE("CLASS INHERITANCE"){
    CHECK_NOTHROW(inheritance());
}
