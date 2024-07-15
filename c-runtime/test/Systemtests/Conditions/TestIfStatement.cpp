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

int if_statement(int i, const char* expected){
    __MPyObj *a;

    __mpy_builtins_setup();
    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);
    a = __mpy_obj_init_str_static("Skipped if!");
    __mpy_obj_ref_inc(a);
    if (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(i), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
        __mpy_obj_ref_dec(a);
    a = __mpy_obj_init_str_static("Went inside if!");
    __mpy_obj_ref_inc(a);
    }
    __mpy_obj_ref_dec(a);

    printf("[IF STATEMENT] If condition : %d, expected : %s\n",i, expected);
    print_mpyobj_str(a);

    __mpy_builtins_cleanup();

    return strcmp(((__MPyStrContent*)a->content)->string, expected);
}
/*
TEST_CASE("GENERATE IF CONDITION"){
    auto i = GENERATE(0, 1);
    auto j = GENERATE("Skipped if!", "Went inside if!");
    CHECK(if_statement(i, j) == 0);
}*/

TEST_CASE("IF CONDITION") {
    auto i = 0;
    SECTION("if(false)") {
        CHECK(if_statement(i, "Skipped if!") == 0);
    }
    i = 1;
    SECTION("if(true)") {
        CHECK(if_statement(i,"Went inside if!") == 0);
    }
}
