#define CATCH_CONFIG_RUNNER

#include <stddef.h>

#include "catch.hpp"
#include "test/test_helpers.h"
#include "test/FunctionCalls/TestFunctionParamsHelpers.h"

__MPyObj *func1;

__MPyObj *func2;

__MPyObj *func3;

__MPyObj *func4;

int main( int argc, char* argv[] ) {
  // global setup...
  __mpy_builtins_setup();

  int result = 0;

  try{
    result = Catch::Session().run( argc, argv );
  }
  catch (const std::runtime_error& error){
      printf("Hello World");
  }

   __mpy_builtins_cleanup();
  // global clean-up... Why is this working ? ....

  return result;
}

int empty_params(const char* expected){
    __MPyObj *a;

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	func1 = __mpy_obj_init_func(&func_func1);
	__mpy_obj_ref_inc(func1);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(func1, __mpy_obj_init_tuple(0), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	__mpy_obj_ref_dec(func1);

	print_mpyobj_str(a);

    return strcmp(((__MPyStrContent*)a->content)->string, expected);
}

int one_param(__MPyObj *param1, __MPyObj *expected, int isNumeric){
    __MPyObj *a;

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	func2 = __mpy_obj_init_func(&func_func2);
	__mpy_obj_ref_inc(func2);


	__mpy_obj_ref_dec(a);
	a = __mpy_call(func2, __mpy_tuple_assign(0, param1, __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	__mpy_obj_ref_dec(func2);

    if(!isNumeric){
        print_mpyobj_str(a);
        return strcmp(((__MPyStrContent*)a->content)->string, ((__MPyStrContent*)expected->content)->string);
    }

    print_mpyobj_int(a);
    return (*(int*)(a->content)) == (*(int*)(expected->content)) ? 1 : 0;
}

int one_param_print_call(){
    __MPyObj *a;

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	func4 = __mpy_obj_init_func(&func_func4);
	__mpy_obj_ref_inc(func4);


	__mpy_obj_ref_dec(a);
	a = __mpy_call(func4, __mpy_tuple_assign(0, __mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_str_static("Wuppie"), __mpy_obj_init_tuple(1)), NULL), __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	__mpy_obj_ref_dec(func4);

	return 0;
}

int multiple_params(__MPyObj *param1, __MPyObj *param2, __MPyObj *expected, int isNumeric){
    __MPyObj *a;

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	func3 = __mpy_obj_init_func(&func_func3);
	__mpy_obj_ref_inc(func3);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(func3, __mpy_tuple_assign(0, param1, __mpy_tuple_assign(1, param2, __mpy_obj_init_tuple(2))), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	__mpy_obj_ref_dec(func3);

    if(!isNumeric){
        print_mpyobj_str(a);
        return strcmp(((__MPyStrContent*)a->content)->string, ((__MPyStrContent*)expected->content)->string);
    }

    print_mpyobj_int(a);
    return (*(int*)(a->content)) == (*(int*)(expected->content)) ? 1 : 0;
}

TEST_CASE("TEST FUNCTION EMPTY PARAMS")
{
    CHECK(empty_params("Function called !") == 0);
    CHECK(one_param_print_call() == 0); // This does not crash return value is irrelevant.
}

TEST_CASE("TEST FUNCTION ONE PARAM")
{
    auto [input, expected_output, isNumeric] = GENERATE(table<__MPyObj *, __MPyObj *, int>({
        { __mpy_obj_init_int(123), __mpy_obj_init_int(123), 1},
        { __mpy_obj_init_boolean(true), __mpy_obj_init_boolean(true), 1},
        { __mpy_obj_init_str_static("String"), __mpy_obj_init_str_static("String"), 0},
    }));

    __MPyObj *param1;
    __MPyObj *expected;

    param1 = __mpy_obj_init_object();
    __mpy_obj_ref_inc(param1);

    __mpy_obj_ref_dec(param1);
    param1 = input;
    __mpy_obj_ref_inc(param1);

    expected = __mpy_obj_init_object();
    __mpy_obj_ref_inc(expected);

    __mpy_obj_ref_dec(expected);
    expected = expected_output;
    __mpy_obj_ref_inc(expected);

    CAPTURE(input);
    if(!isNumeric)
        CHECK(one_param(param1, expected, isNumeric) == 0);
    else
        CHECK(one_param(param1, expected, isNumeric) == 1);
}

TEST_CASE("TEST FUNCTION MULTIPLE PARAMS")
{
        auto [input1, input2, expected_output, isNumeric] = GENERATE(table<__MPyObj *, __MPyObj *, __MPyObj *, int>({
            { __mpy_obj_init_int(1), __mpy_obj_init_int(2), __mpy_obj_init_int(3), 1},
            { __mpy_obj_init_str_static("Hello"), __mpy_obj_init_str_static(" World !"), __mpy_obj_init_str_static("Hello World !"), 0},
        }));

        __MPyObj *param1;
        __MPyObj *param2;
        __MPyObj *expected;

        param1 = __mpy_obj_init_object();
        __mpy_obj_ref_inc(param1);

        __mpy_obj_ref_dec(param1);
        param1 = input1;
        __mpy_obj_ref_inc(param1);

        param2 = __mpy_obj_init_object();
        __mpy_obj_ref_inc(param2);

        __mpy_obj_ref_dec(param2);
        param2 = input2;
        __mpy_obj_ref_inc(param2);

        expected = __mpy_obj_init_object();
        __mpy_obj_ref_inc(expected);

        __mpy_obj_ref_dec(expected);
        expected = expected_output;
        __mpy_obj_ref_inc(expected);

        CAPTURE(input1);
        CAPTURE(input2);

        if(!isNumeric)
            CHECK(multiple_params(param1, param2, expected, isNumeric) == 0);
        else
            CHECK(multiple_params(param1, param2, expected, isNumeric) == 1);
}
