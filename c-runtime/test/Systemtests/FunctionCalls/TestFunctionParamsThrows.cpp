#define CATCH_CONFIG_MAIN

#include <stddef.h>

#include "catch_amalgamated.hpp"
#include "test/test_helpers.h"
#include "test/FunctionCalls/TestFunctionParamsHelpers.h"

__MPyObj *func_throws1;
__MPyObj *throws2;
__MPyObj *throws3;
__MPyObj *throws4;
__MPyObj *throws5;

void exit(int status);

int no_param_but_provided(){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    func_throws1 = __mpy_obj_init_func(&func_func_throws1);
    __mpy_obj_ref_inc(func_throws1);

    __mpy_obj_ref_dec(a);
    a = __mpy_call(func_throws1, __mpy_tuple_assign(0, __mpy_obj_init_int(123), __mpy_obj_init_tuple(1)), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    __mpy_obj_ref_dec(func_throws1);

    __mpy_builtins_cleanup();

    return 0;
}

int one_param_but_none_provided(){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	throws2 = __mpy_obj_init_func(&func_throws2);
	__mpy_obj_ref_inc(throws2);


	__mpy_obj_ref_dec(a);
	a = __mpy_call(throws2, __mpy_obj_init_tuple(0), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	__mpy_obj_ref_dec(throws2);

    __mpy_builtins_cleanup();

	return 0;
}

int one_param_but_two_provided(){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	throws3 = __mpy_obj_init_func(&func_throws3);
	__mpy_obj_ref_inc(throws3);


	__mpy_obj_ref_dec(a);
	a = __mpy_call(throws3, __mpy_tuple_assign(0, __mpy_obj_init_str_static("String1"), __mpy_tuple_assign(1, __mpy_obj_init_str_static("String2"), __mpy_obj_init_tuple(2))), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	__mpy_obj_ref_dec(throws3);

    __mpy_builtins_cleanup();
    return 0;
}

int one_param_but_void_function_provided(){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	throws4 = __mpy_obj_init_func(&func_throws4);
	__mpy_obj_ref_inc(throws4);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(print, __mpy_tuple_assign(0, __mpy_call(throws4, __mpy_tuple_assign(0, __mpy_call(print, __mpy_obj_init_tuple(0), NULL), __mpy_obj_init_tuple(1)), NULL), __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	__mpy_obj_ref_dec(throws4);

    __mpy_builtins_cleanup();
	return 0;
}

int two_params_but_one_provided(){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	throws5 = __mpy_obj_init_func(&func_throws5);
	__mpy_obj_ref_inc(throws5);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(throws5, __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	__mpy_obj_ref_dec(throws5);

    __mpy_builtins_cleanup();
	return 0;
}

TEST_CASE("TEST FUNCTION THROWS")
{
    CHECK_THROWS_AS(no_param_but_provided(), std::runtime_error);
    CHECK_THROWS_AS(one_param_but_none_provided(), std::runtime_error);
    CHECK_THROWS_AS(one_param_but_two_provided(), std::runtime_error);
    CHECK_THROWS_AS(one_param_but_void_function_provided(), std::runtime_error); // This belongs to built in print testing!
    CHECK_THROWS_AS(two_params_but_one_provided(), std::runtime_error);
}

void exit(int status) {
    throw std::runtime_error("");
}
