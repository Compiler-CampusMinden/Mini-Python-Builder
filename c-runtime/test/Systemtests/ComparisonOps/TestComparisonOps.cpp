#define CATCH_CONFIG_RUNNER

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

int main( int argc, char* argv[] ) {
  // global setup...
  __mpy_builtins_setup();

  int result = 0;

  try{
    result = Catch::Session().run( argc, argv );
  }
  catch (const std::runtime_error& error){
      printf("Hello World");
  }

   __mpy_builtins_cleanup();
  // global clean-up... Why is this working ? ....

  return result;
}

int eq_op(int i, int j){
    __MPyObj *a;

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(i), "__eq__"), __mpy_tuple_assign(0, __mpy_obj_init_int(j), __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

    printf("[EQ] i : %d --- j : %d\n",i,j);

    print_mpyobj_int(a);


	return (*(int*)(a->content)) == (i == j) ? 1 : 0;
}

int ne_op(int i, int j){
    __MPyObj *a;

	a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);
	a = __mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(i), "__ne__"), __mpy_tuple_assign(0, __mpy_obj_init_int(j), __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

    printf("[NE] i : %d --- j : %d\n",i,j);

    print_mpyobj_int(a);

	return (*(int*)(a->content)) == (i != j) ? 1 : 0;
}

TEST_CASE("GENERATE EQ OP"){
    auto i = GENERATE(1, 3, 5);
    auto j = GENERATE(2, 4, 5);
    CHECK(eq_op(i, j) == 1);
}

TEST_CASE("GENERATE NE OP"){
    auto i = GENERATE(1, 3, 5);
    auto j = GENERATE(2, 4, 5);
    CHECK(ne_op(i, j) == 1);
}
