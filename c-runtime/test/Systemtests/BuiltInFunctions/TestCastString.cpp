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

int cast_bool_to_string(bool input, const char* expected){
    __MPyObj *a;

	__mpy_builtins_setup();
	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_boolean(input);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(__mpy_obj_get_attr(a, "__str__"), __mpy_obj_init_tuple(0), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	print_mpyobj_str(a);

	__mpy_builtins_cleanup();

	return strcmp(((__MPyStrContent*)a->content)->string, expected);
}

int cast_int_to_string(int input, const char* expected){
    __MPyObj *a;

    __mpy_builtins_setup();
	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_int(input);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(__mpy_obj_get_attr(a, "__str__"), __mpy_obj_init_tuple(0), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	print_mpyobj_str(a);

	__mpy_builtins_cleanup();

	return strcmp(((__MPyStrContent*)a->content)->string, expected);
}

int cast_string_to_string(const char* input, const char* expected){
    __MPyObj *a;

	__mpy_builtins_setup();
	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_str_static(input);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(__mpy_obj_get_attr(a, "__str__"), __mpy_obj_init_tuple(0), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	print_mpyobj_str(a);

	__mpy_builtins_cleanup();
	return strcmp(((__MPyStrContent*)a->content)->string, expected);
}

TEST_CASE("TEST CAST BOOL TO STRING")
{
    auto [input, expected_output] = GENERATE(table<bool, const char*>({
        { true, "True" },
        { false, "False" },
        { 10, "True" },
        { 0, "False" },
    }));

    CAPTURE(input);
    CHECK(cast_bool_to_string(input, expected_output) == 0);
}

TEST_CASE("TEST CAST INT TO STRING")
{
    auto [input, expected_output] = GENERATE(table<int, const char*>({
        { 123, "123" },
        { 0, "0" },
        { -10, "-10" },
        { 1236789, "1236789" },
    }));

    CAPTURE(input);
    CHECK(cast_int_to_string(input, expected_output) == 0);
}

TEST_CASE("TEST CAST STRING TO STRING")
{
    auto [input, expected_output] = GENERATE(table<const char*, const char*>({
        { "123", "123" },
        { "0", "0" },
        { "-10", "-10" },
        { "1236789", "1236789" },
    }));

    CAPTURE(input);
    CHECK(cast_string_to_string(input, expected_output) == 0);
}
