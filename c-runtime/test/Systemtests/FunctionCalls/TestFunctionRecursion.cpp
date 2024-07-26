#define CATCH_CONFIG_MAIN

#include <stddef.h>

#include "catch_amalgamated.hpp"
#include "test/test_helpers.h"
#include "test/FunctionCalls/TestFunctionRecursionHelpers.h"

int rec(){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    recursion = __mpy_obj_init_func(&func_recursion);
    __mpy_obj_ref_inc(recursion);


    __mpy_obj_ref_dec(a);
    a = __mpy_call(recursion, __mpy_tuple_assign(0, __mpy_obj_init_int(5), __mpy_obj_init_tuple(1)), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    __mpy_obj_ref_dec(recursion);

    print_mpyobj_int(a);

    __mpy_builtins_cleanup();

    return (*(int*)(a->content)) == 0 ? 1 : 0;
}

TEST_CASE("FUNCTION RECURSION"){
    CHECK(rec() == 1);
}
