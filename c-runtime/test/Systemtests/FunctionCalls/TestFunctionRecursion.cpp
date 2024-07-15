#define CATCH_CONFIG_RUNNER

#include <stddef.h>

#include "catch.hpp"
#include "test/test_helpers.h"
#include "test/FunctionCalls/TestFunctionRecursionHelpers.h"

int main( int argc, char* argv[] ) {
  // global setup...
  __mpy_builtins_setup();

  int result = 0;

  try{
    result = Catch::Session().run( argc, argv );
  }
  catch (const std::runtime_error& error){
      printf("");
  }

   __mpy_builtins_cleanup();
  // global clean-up... Why is this working ? ....

  return result;
}

int rec(){
    __MPyObj *a;

    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    recursion = __mpy_obj_init_func(&func_recursion);
    __mpy_obj_ref_inc(recursion);


    __mpy_obj_ref_dec(a);
    a = __mpy_call(recursion, __mpy_tuple_assign(0, __mpy_obj_init_int(5), __mpy_obj_init_tuple(1)), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    __mpy_obj_ref_dec(recursion);

    print_mpyobj_int(a);

    return (*(int*)(a->content)) == 0 ? 1 : 0;
}

TEST_CASE("FUNCTION RECURSION"){
    CHECK(rec() == 1);
}
