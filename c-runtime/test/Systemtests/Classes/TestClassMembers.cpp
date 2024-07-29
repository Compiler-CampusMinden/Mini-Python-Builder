#define CATCH_CONFIG_MAIN

#include "catch_amalgamated.hpp"
#include "test/test_helpers.h"
#include "test/Classes/TestClassMembersHelpers.h"

__MPyObj *getter_setter;

int getX_from_class(){
    __MPyObj *myClass;
    __MPyObj *returnValueGetter;

    __mpy_builtins_setup();
    myClass = __mpy_obj_init_object();
	__mpy_obj_ref_inc(myClass);

    returnValueGetter = __mpy_obj_init_object();
    __mpy_obj_ref_inc(returnValueGetter);

	getter_setter = __mpy_obj_init_type("getter_setter", __MPyType_Object);
	__mpy_obj_ref_inc(getter_setter);
	{
		__MPyObj *__init__;
		__init__ = __mpy_obj_init_func(&func_getter_setter___init__);
		__mpy_obj_ref_inc(__init__);
		__mpy_obj_set_attr(getter_setter, "__init__", __init__);
		__mpy_obj_ref_dec(__init__);
	}
	{
		__MPyObj *getX;
		getX = __mpy_obj_init_func(&func_getter_setter_getX);
		__mpy_obj_ref_inc(getX);
		__mpy_obj_set_attr(getter_setter, "getX", getX);
		__mpy_obj_ref_dec(getX);
	}
	{
		__MPyObj *setX;
		setX = __mpy_obj_init_func(&func_getter_setter_setX);
		__mpy_obj_ref_inc(setX);
		__mpy_obj_set_attr(getter_setter, "setX", setX);
		__mpy_obj_ref_dec(setX);
	}

	__mpy_obj_ref_dec(myClass);
	myClass = __mpy_call(getter_setter, __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(myClass);

	__mpy_obj_ref_dec(returnValueGetter);
    returnValueGetter = __mpy_call(__mpy_obj_get_attr(myClass, "getX"), __mpy_obj_init_tuple(0), NULL);
    __mpy_obj_ref_inc(returnValueGetter);

	__mpy_obj_ref_dec(myClass);
    __mpy_obj_ref_dec(returnValueGetter);

	__mpy_obj_ref_dec(getter_setter);

    print_mpyobj_int(returnValueGetter);

    __mpy_builtins_cleanup();

	return (*(int*)(returnValueGetter->content));
}

int setX_from_class(){
    __MPyObj *myClass;
    __MPyObj *returnValueGetter;

    __mpy_builtins_setup();
    myClass = __mpy_obj_init_object();
	__mpy_obj_ref_inc(myClass);

    returnValueGetter = __mpy_obj_init_object();
    __mpy_obj_ref_inc(returnValueGetter);

	getter_setter = __mpy_obj_init_type("getter_setter", __MPyType_Object);
	__mpy_obj_ref_inc(getter_setter);
	{
		__MPyObj *__init__;
		__init__ = __mpy_obj_init_func(&func_getter_setter___init__);
		__mpy_obj_ref_inc(__init__);
		__mpy_obj_set_attr(getter_setter, "__init__", __init__);
		__mpy_obj_ref_dec(__init__);
	}
	{
		__MPyObj *getX;
		getX = __mpy_obj_init_func(&func_getter_setter_getX);
		__mpy_obj_ref_inc(getX);
		__mpy_obj_set_attr(getter_setter, "getX", getX);
		__mpy_obj_ref_dec(getX);
	}
	{
		__MPyObj *setX;
		setX = __mpy_obj_init_func(&func_getter_setter_setX);
		__mpy_obj_ref_inc(setX);
		__mpy_obj_set_attr(getter_setter, "setX", setX);
		__mpy_obj_ref_dec(setX);
	}

	__mpy_obj_ref_dec(myClass);
	myClass = __mpy_call(getter_setter, __mpy_tuple_assign(0, __mpy_obj_init_int(133), __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(myClass);

	__mpy_obj_ref_dec(returnValueGetter);
    returnValueGetter = __mpy_call(__mpy_obj_get_attr(myClass, "getX"), __mpy_obj_init_tuple(0), NULL);
    __mpy_obj_ref_inc(returnValueGetter);

    print_mpyobj_int(returnValueGetter);

    __mpy_obj_ref_dec(__mpy_call(__mpy_obj_get_attr(myClass, "setX"), __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1)), NULL));

	__mpy_obj_ref_dec(returnValueGetter);
    returnValueGetter = __mpy_call(__mpy_obj_get_attr(myClass, "getX"), __mpy_obj_init_tuple(0), NULL);
    __mpy_obj_ref_inc(returnValueGetter);

	__mpy_obj_ref_dec(myClass);
    __mpy_obj_ref_dec(returnValueGetter);

	__mpy_obj_ref_dec(getter_setter);

    print_mpyobj_int(returnValueGetter);

   __mpy_builtins_cleanup();

	return (*(int*)(returnValueGetter->content));
}

TEST_CASE("CLASS MEMBERS"){
    CHECK(getX_from_class() == 1);
    CHECK(setX_from_class() == 1);
}
