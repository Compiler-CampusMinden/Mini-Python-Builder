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

// Throws error see TODO(./src/function-args.c:36)

int test_type(){
    __MPyObj *a;

	__mpy_builtins_setup();
	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(type, __mpy_tuple_assign(0, a, __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

    print_mpyobj_int(a);

	__mpy_builtins_cleanup();

	return 0;
}

TEST_CASE("TEST PRINT")
{
    //CHECK(test_type() == 0);
}
