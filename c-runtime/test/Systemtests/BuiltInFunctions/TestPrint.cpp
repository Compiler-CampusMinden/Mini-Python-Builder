/*
    Since print returns void only the return value of the program can be checked.
    A successful test indicates that calling print with an literal/function call worked.
    Calling print(type(mpyobj)) should result in a crash for instance.
*/
#define CATCH_CONFIG_MAIN

#include <stddef.h>

#include "catch_amalgamated.hpp"
#include "assert.h"
#include "mpy_aliases.h"
#include "mpy_obj.h"
#include "builtins-setup.h"
#include "function-args.h"
#include "literals/tuple.h"
#include "literals/int.h"
#include "literals/boolean.h"
#include "literals/str.h"
#include "type-hierarchy/object.h"
#include "type-hierarchy/type.h"
#include "test/test_helpers.h"

int print_int(){

	__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_int(133), __mpy_obj_init_tuple(1)), NULL));

	return 0;
}

int print_string(){

    __mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_str_static("Test"), __mpy_obj_init_tuple(1)), NULL));

	return 0;
}

int print_bool(){

    __mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_boolean(true), __mpy_obj_init_tuple(1)), NULL));

	return 0;
}

int print_expression(){

	__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1), "__add__"), __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1)), NULL), __mpy_obj_init_tuple(1)), NULL));

	return 0;
}

// Throws error see TODO(./src/function-args.c:36)
int print_type(){

	//__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_call(type, __mpy_tuple_assign(0, __mpy_obj_init_int(123), __mpy_obj_init_tuple(1)), NULL), __mpy_obj_init_tuple(1)), NULL));
    __mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_call(type, __mpy_tuple_assign(0, print, __mpy_obj_init_tuple(1)), NULL), __mpy_obj_init_tuple(1)), NULL));
	return 0;
}


TEST_CASE("TEST PRINT")
{
	__mpy_builtins_setup();

    CHECK(print_int() == 0);
    CHECK(print_string() == 0);
    CHECK(print_bool() == 0);
    CHECK(print_expression() == 0);
    //CHECK(print_type() == 0);

    __mpy_builtins_cleanup();
}
