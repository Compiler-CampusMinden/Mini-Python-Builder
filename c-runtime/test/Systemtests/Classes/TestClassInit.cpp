#define CATCH_CONFIG_MAIN

#include <stddef.h>

#include "catch_amalgamated.hpp"
#include "test/test_helpers.h"
#include "test/Classes/TestClassInitHelpers.h"

__MPyObj *init_no_params;

__MPyObj *init_one_param;

__MPyObj *init_multiple_params;

int init_class_no_params(){
    __MPyObj *x;

    __mpy_builtins_setup();

    __mpy_builtins_setup();
    x = __mpy_obj_init_object();
	__mpy_obj_ref_inc(x);


	init_no_params = __mpy_obj_init_type("init_no_params", __MPyType_Object);
	__mpy_obj_ref_inc(init_no_params);
	{
		__MPyObj *__init__;
		__init__ = __mpy_obj_init_func(&func_init_no_params___init__);
		__mpy_obj_ref_inc(__init__);
		__mpy_obj_set_attr(init_no_params, "__init__", __init__);
		__mpy_obj_ref_dec(__init__);
	}

	__mpy_obj_ref_dec(x);
	x = __mpy_call(init_no_params, __mpy_obj_init_tuple(0), NULL);
	__mpy_obj_ref_inc(x);

	__mpy_obj_ref_dec(x);

    print_mpyobj(x);

	__mpy_obj_ref_dec(init_no_params);

    __mpy_builtins_cleanup();

	return 0;
}

int init_class_one_param(){
    __MPyObj *x;

    __mpy_builtins_setup();
    x = __mpy_obj_init_object();
	__mpy_obj_ref_inc(x);


	init_one_param = __mpy_obj_init_type("init_one_param", __MPyType_Object);
	__mpy_obj_ref_inc(init_one_param);
	{
		__MPyObj *__init__;
		__init__ = __mpy_obj_init_func(&func_init_one_param___init__);
		__mpy_obj_ref_inc(__init__);
		__mpy_obj_set_attr(init_one_param, "__init__", __init__);
		__mpy_obj_ref_dec(__init__);
	}

	__mpy_obj_ref_dec(x);
	x = __mpy_call(init_one_param, __mpy_tuple_assign(0, __mpy_obj_init_str_static("test"), __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(x);

	__mpy_obj_ref_dec(x);


	__mpy_obj_ref_dec(init_one_param);

	print_mpyobj(x);

    __mpy_builtins_cleanup();

	return 0;
}

int init_class_multiple_params(){
    __MPyObj *x;

    __mpy_builtins_setup();
	x = __mpy_obj_init_object();
	__mpy_obj_ref_inc(x);


	init_multiple_params = __mpy_obj_init_type("init_multiple_params", __MPyType_Object);
	__mpy_obj_ref_inc(init_multiple_params);
	{
		__MPyObj *__init__;
		__init__ = __mpy_obj_init_func(&func_init_multiple_params___init__);
		__mpy_obj_ref_inc(__init__);
		__mpy_obj_set_attr(init_multiple_params, "__init__", __init__);
		__mpy_obj_ref_dec(__init__);
	}

	__mpy_obj_ref_dec(x);
	x = __mpy_call(init_multiple_params, __mpy_tuple_assign(0, __mpy_obj_init_str_static("test"), __mpy_tuple_assign(1, __mpy_obj_init_int(123), __mpy_obj_init_tuple(2))), NULL);
	__mpy_obj_ref_inc(x);

	__mpy_obj_ref_dec(x);

	__mpy_obj_ref_dec(init_multiple_params);

    print_mpyobj(x);

    __mpy_builtins_cleanup();

	return 0;
}

TEST_CASE("CLASS INIT"){
    CHECK_NOTHROW(init_class_no_params());
    CHECK_NOTHROW(init_class_one_param());
    CHECK_NOTHROW(init_class_multiple_params());
}
