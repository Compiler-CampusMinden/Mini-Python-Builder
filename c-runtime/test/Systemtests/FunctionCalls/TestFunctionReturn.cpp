#define CATCH_CONFIG_MAIN

#include <stddef.h>

#include "catch_amalgamated.hpp"
#include "test/test_helpers.h"
#include "test/FunctionCalls/TestFunctionReturnHelpers.h"

__MPyObj *return_expression;

__MPyObj *return_tuple_literal_throws;

__MPyObj *return_void_function_call_print;

int return_expr(){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    return_expression = __mpy_obj_init_func(&func_return_expression);
    __mpy_obj_ref_inc(return_expression);


    __mpy_obj_ref_dec(a);
    a = __mpy_call(return_expression, __mpy_obj_init_tuple(0), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    __mpy_obj_ref_dec(return_expression);

    __mpy_builtins_cleanup();
    return (*(int*)(a->content));
}

int return_tuple_literal(){
    __MPyObj *a;

    __mpy_builtins_setup();
	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	return_tuple_literal_throws = __mpy_obj_init_func(&func_return_tuple_literal_throws);
	__mpy_obj_ref_inc(return_tuple_literal_throws);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(return_tuple_literal_throws, __mpy_obj_init_tuple(0), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

    print_mpyobj(a);

	__mpy_obj_ref_dec(return_tuple_literal_throws);

    __mpy_builtins_cleanup();
	return 1;
}

int return_print(){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	return_void_function_call_print = __mpy_obj_init_func(&func_return_void_function_call_print);
	__mpy_obj_ref_inc(return_void_function_call_print);


	__mpy_obj_ref_dec(a);
	a = __mpy_call(return_void_function_call_print, __mpy_obj_init_tuple(0), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

    print_mpyobj(a);

    __mpy_obj_ref_dec(return_void_function_call_print);

    __mpy_builtins_cleanup();
    return 1;
}

TEST_CASE("RETURN VALUES"){
    CHECK(return_expr() == 1);              //This is actually return_mpyobj when the expression is evaluated... Also if you call func or return string, bool, int literal ...
    CHECK(return_tuple_literal() == 1);     // Returning does not throw an error but trying to print the "returned" values will result in a crashing program. See TestFunctionReturn.java
    CHECK(return_print() == 1);             // ""
}                                       // Testing return of literals is already done in TestFunctionParams.cpp
