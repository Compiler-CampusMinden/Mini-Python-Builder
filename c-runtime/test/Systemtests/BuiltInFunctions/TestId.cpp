/*
    The Tests defined here only work if the assignment of the id to an mpyobj is not random.
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

int test_id_string(){
    __MPyObj *a;
    __MPyObj *b;

	__mpy_builtins_setup();

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	b = __mpy_obj_init_object();
	__mpy_obj_ref_inc(b);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_str_static("123");
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(b);
	b = __mpy_call(id, __mpy_tuple_assign(0, a, __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(b);

	__mpy_obj_ref_dec(a);
	__mpy_obj_ref_dec(b);

	print_mpyobj_int(b);

	__mpy_builtins_cleanup();

	return (*(int*)(b->content) == 63 ? 1 : 0);
}

int test_id_int(){
    __MPyObj *a;
    __MPyObj *b;

	__mpy_builtins_setup();

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	b = __mpy_obj_init_object();
	__mpy_obj_ref_inc(b);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_int(123);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(b);
	b = __mpy_call(id, __mpy_tuple_assign(0, a, __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(b);

	__mpy_obj_ref_dec(a);
	__mpy_obj_ref_dec(b);

	print_mpyobj_int(b);

	__mpy_builtins_cleanup();

	return (*(int*)(b->content) == 158 ? 1 : 0);
}

int test_id_bool(){
    __MPyObj *a;
    __MPyObj *b;

	__mpy_builtins_setup();

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	b = __mpy_obj_init_object();
	__mpy_obj_ref_inc(b);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_boolean(true);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(b);
	b = __mpy_call(id, __mpy_tuple_assign(0, a, __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(b);

	__mpy_obj_ref_dec(a);
	__mpy_obj_ref_dec(b);

	print_mpyobj_int(b);

	__mpy_builtins_cleanup();

	return (*(int*)(b->content) == 260 ? 1 : 0);
}

int test_id_call(){
    __MPyObj *a;

	__mpy_builtins_setup();

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(id, __mpy_tuple_assign(0, __mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1), "__add__"), __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1)), NULL), __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	print_mpyobj_int(a);

	__mpy_builtins_cleanup();

	return (*(int*)(a->content) == 390 ? 1 : 0);
}

TEST_CASE("TEST ID OF mpy_obj")
{
    CHECK(test_id_string() == 1);
    CHECK(test_id_int() == 1);
    CHECK(test_id_bool() == 1);
    CHECK(test_id_call() == 1);

}
