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

/*
test_comparison_ops:
	g++-13 --version
	g++-13 -I $(INCLUDEDIR) -o $(TESTDIR)/Systemtests/ComparisonOps/TestComparisonOps.out -w -fpermissive $(TESTDIR)/Systemtests/ComparisonOps/TestComparisonOps.c* $(C_FILES)
*/
int test(){
    __MPyObj *a;
    __MPyObj *b;

	__mpy_builtins_setup();

	if (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(0), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
		__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_str_static("Somehow went here"), __mpy_obj_init_tuple(1)), NULL));
	}else if (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
		__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_str_static("elif!"), __mpy_obj_init_tuple(1)), NULL));
	}else {
		__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_call(__mpy_obj_get_attr(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(123), "__sub__"), __mpy_tuple_assign(0, __mpy_obj_init_int(22), __mpy_obj_init_tuple(1)), NULL), "__lt__"), __mpy_tuple_assign(0, __mpy_obj_init_int(0), __mpy_obj_init_tuple(1)), NULL), __mpy_obj_init_tuple(1)), NULL));
	}


	__mpy_builtins_cleanup();

	return 0;
}

TEST_CASE("wuppie"){
    CHECK(test() == 0);
}
