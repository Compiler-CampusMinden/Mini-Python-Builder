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

int add_op(int i, int j){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);
    a = __mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(i), "__add__"), __mpy_tuple_assign(0, __mpy_obj_init_int(j), __mpy_obj_init_tuple(1)), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    printf("[ADD] i : %d --- j : %d\n",i,j);
    print_mpyobj_int(a);

    __mpy_builtins_cleanup();

    return (*(int*)(a->content)) == (i+j) ? 1 : 0;
}

int sub_op(int i, int j){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);
    a = __mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(i), "__sub__"), __mpy_tuple_assign(0, __mpy_obj_init_int(j), __mpy_obj_init_tuple(1)), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    printf("[SUB] i : %d --- j : %d\n",i,j);
    print_mpyobj_int(a);

    __mpy_builtins_cleanup();

    return (*(int*)(a->content)) == (i-j) ? 1 : 0;
}

int mul_op(int i, int j){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);
    a = __mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(i), "__mul__"), __mpy_tuple_assign(0, __mpy_obj_init_int(j), __mpy_obj_init_tuple(1)), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    printf("[MUL] i : %d --- j : %d\n",i,j);
    print_mpyobj_int(a);

    __mpy_builtins_cleanup();

    return (*(int*)(a->content)) == (i*j) ? 1 : 0;
}

int div_op(int i, int j){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);
    a = __mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(i), "__div__"), __mpy_tuple_assign(0, __mpy_obj_init_int(j), __mpy_obj_init_tuple(1)), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    printf("[DIV] i : %d --- j : %d\n",i,j);
    print_mpyobj_int(a);

    __mpy_builtins_cleanup();

    return (*(int*)(a->content)) == (i/j) ? 1 : 0;
}


TEST_CASE("GENERATE ADD OP"){
    auto i = GENERATE(1, 3, 5);
    auto j = GENERATE(2, 4, 6);
    CHECK(add_op(i, j) == 1);
}

TEST_CASE("GENERATE SUB OP"){
    auto i = GENERATE(1, 3, 5);
    auto j = GENERATE(2, 4, 6);
    CHECK(sub_op(i, j) == 1);
}

TEST_CASE("GENERATE MUL OP"){
    auto i = GENERATE(1, 3, 5);
    auto j = GENERATE(2, 4, 6);
    CHECK(mul_op(i, j) == 1);
}

TEST_CASE("GENERATE DIV OP"){
    auto i = GENERATE(3, 8, 12);
    auto j = GENERATE(2, 4, 6);
    CHECK(div_op(i, j) == 1);
}
