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

void exit(int status) {
    throw std::runtime_error("Error");
}

int cast_bool_to_int(bool input){
    __MPyObj *a;

	__mpy_builtins_setup();
	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_boolean(input);
	__mpy_obj_ref_inc(a);

	a = __mpy_call(__mpy_obj_get_attr(a, "__int__"), __mpy_obj_init_tuple(0), NULL);

	__mpy_obj_ref_dec(a);

    print_mpyobj_int(a);

	__mpy_builtins_cleanup();

    return (*(int*)(a->content));
}

int cast_string_to_int(const char* input){
    __MPyObj *a;

	__mpy_builtins_setup();
	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_str_static(input);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(__mpy_obj_get_attr(a, "__int__"), __mpy_obj_init_tuple(0), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

    print_mpyobj_int(a);

	__mpy_builtins_cleanup();

    return (*(int*)(a->content));
}

TEST_CASE("TEST CAST BOOL TO INT")
{
    auto [input, expected_output] = GENERATE(table<bool, int>({
        { true, 1 },
        { false, 0 },
        {10, 1},
    }));

    CAPTURE(input);
    CHECK(cast_bool_to_int(input) == expected_output);
}

TEST_CASE("TEST CAST STRING TO INT")
{
    auto [input, expected_output] = GENERATE(table<const char*, int>({
        { "1", 1 },
        { "0", 0 },
        { "10", 10},
    }));

    CAPTURE(input);
    CHECK(cast_string_to_int(input) == expected_output);
}

TEST_CASE("TEST CAST STRING TO INT ERROR") {
    REQUIRE_THROWS_AS(cast_string_to_int("false"), std::runtime_error);
}

/*
And

int cast_int_to_int(int i){
}

*/
