// 010-TestCase.cpp

// Let Catch provide main():
#define CATCH_CONFIG_MAIN
//#define CATCH_CONFING_RUNNER

#include <stddef.h>

#include "catch.hpp"
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
#include "literals/str.h"

int declaration_checked_malloc()  {
    __MPyObj *a;

	__mpy_builtins_setup();

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

    print_mpyobj(a);

    __mpy_builtins_cleanup();

	return a != NULL; // This will never be the case since checked_malloc will terminate the program if malloc fails.
}

int declaration_assigned_id()  {
    __MPyObj *a;

	__mpy_builtins_setup();

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

    print_mpyobj(a);

    __mpy_builtins_cleanup();

	return (a->id == 123) ? 1 : 0;
}

int assignment_int_literal(int i){
    __MPyObj *a;

	__mpy_builtins_setup();

    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_int(i);
	__mpy_obj_ref_inc(a);

	print_mpyobj_int(a);

    __mpy_builtins_cleanup();

    return (*(int*)(a->content)) == i ? 1 : 0;
}

int assignment_string_literal(){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);
    a = __mpy_obj_init_str_static("stringVal");
    __mpy_obj_ref_inc(a);

    print_mpyobj_str(a);

    __mpy_obj_ref_dec(a);

    __mpy_builtins_cleanup();

    return strcmp(((__MPyStrContent*)a->content)->string, "stringVal");
}

int assignment_bool_literal(){
    __MPyObj *a;

	__mpy_builtins_setup();
	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_boolean(true);
	__mpy_obj_ref_inc(a);

    print_mpyobj_bool(a);

	__mpy_obj_ref_dec(a);

	__mpy_builtins_cleanup();
	return (((MPyBooleanContent*)a->content)->value) ? 1 : 0;
}

int assignment_tuple_literal(){
// see literals/tuple.c
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);
    a = __mpy_tuple_assign(0, __mpy_obj_init_int(123), __mpy_tuple_assign(1, __mpy_obj_init_str_static("stringVal"), __mpy_tuple_assign(2, __mpy_obj_init_boolean(true), __mpy_obj_init_tuple(3))));
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);



    __mpy_builtins_cleanup();

    return 0;
}

TEST_CASE( "TEST" ){
    REQUIRE(declaration_checked_malloc() == 1);
    CHECK(declaration_assigned_id() == 1);
    CHECK(assignment_int_literal(133) == 1);
    CHECK(assignment_string_literal() == 0); // 0 means the const char* are the same
    CHECK(assignment_bool_literal() == 1);
}

TEST_CASE("GENERATE"){
    auto i = GENERATE(1, 3, 5);
    CHECK(assignment_int_literal(i) == 1);
}
