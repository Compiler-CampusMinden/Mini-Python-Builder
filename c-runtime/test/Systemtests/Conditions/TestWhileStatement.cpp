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


int while_statement(){
    __MPyObj *a;
    int counter = 0;

    __mpy_builtins_setup();
	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_obj_init_int(3);
	__mpy_obj_ref_inc(a);

	while (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_call(__mpy_obj_get_attr(a, "__gt__"), __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1)), NULL), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
		__mpy_obj_ref_dec(a);
	    a = __mpy_call(__mpy_obj_get_attr(a, "__sub__"), __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1)), NULL);
	    __mpy_obj_ref_inc(a);
	    counter = counter +1;
	}
	__mpy_obj_ref_dec(a);

    printf("Visited while loop : %d times", counter);
    print_mpyobj_int(a);

	__mpy_builtins_cleanup();

    return (counter == 2) ? 1 : 0;
}

TEST_CASE( "TEST WHILE STATEMENT" ){
    REQUIRE(while_statement() == 1);
}
