#define CATCH_CONFIG_MAIN

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

int and_op(int i, int j){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);



    __mpy_obj_ref_dec(a);
    a = __mpy_obj_init_boolean(__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(i), "__bool__"), __mpy_obj_init_tuple(0), NULL)) && __mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(j), "__bool__"), __mpy_obj_init_tuple(0), NULL)));
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    printf("[AND] i : %d --- j : %d\n",i,j);
    print_mpyobj_int(a);

    __mpy_builtins_cleanup();

    return (*(int*)(a->content)) == (i && j) ? 1 : 0;

}

int or_op(int i, int j){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);
    a = __mpy_obj_init_boolean(__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(i), "__bool__"), __mpy_obj_init_tuple(0), NULL)) || __mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(j), "__bool__"), __mpy_obj_init_tuple(0), NULL)));
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    printf("[OR] i : %d --- j : %d\n",i,j);
    print_mpyobj_int(a);

    __mpy_builtins_cleanup();

    return (*(int*)(a->content)) == (i || j) ? 1 : 0;

}

int not_op(int i){
    __MPyObj *a;

	__mpy_builtins_setup();
	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_boolean(!__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(i), "__bool__"), __mpy_obj_init_tuple(0), NULL)));
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

    printf("[NOT] i : %d\n",i);
	print_mpyobj_int(a);

	__mpy_builtins_cleanup();

	return (*(int*)(a->content)) == (!i) ? 1 : 0;
}

TEST_CASE("GENERATE AND OP"){
    auto i = GENERATE(0, 1);
    auto j = GENERATE(0, 1);
    CHECK(and_op(i, j) == 1);
}

TEST_CASE("GENERATE OR OP"){
    auto i = GENERATE(0, 1);
    auto j = GENERATE(0, 1);
    CHECK(or_op(i, j) == 1);
}

TEST_CASE("GENERATE NOT OP"){
    auto i = GENERATE(0,1);
    CHECK(not_op(i) == 1);
}
