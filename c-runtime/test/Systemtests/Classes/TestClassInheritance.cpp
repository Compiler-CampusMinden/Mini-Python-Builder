#define CATCH_CONFIG_RUNNER

#include <stddef.h>

#include "catch.hpp"
#include "test/test_helpers.h"
#include "test/Classes/TestClassInheritanceHelpers.h"

__MPyObj *A;

__MPyObj *B;

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

int inheritance(){
    __MPyObj *child;

    child = __mpy_obj_init_object();
    __mpy_obj_ref_inc(child);


    A = __mpy_obj_init_type("A", __MPyType_Object);
    __mpy_obj_ref_inc(A);
    {
        __MPyObj *__init__;
        __init__ = __mpy_obj_init_func(&func_A___init__);
        __mpy_obj_ref_inc(__init__);
        __mpy_obj_set_attr(A, "__init__", __init__);
        __mpy_obj_ref_dec(__init__);
    }
    B = __mpy_obj_init_type("B", A);
    __mpy_obj_ref_inc(B);
    {
        __MPyObj *__init__;
        __init__ = __mpy_obj_init_func(&func_B___init__);
        __mpy_obj_ref_inc(__init__);
        __mpy_obj_set_attr(B, "__init__", __init__);
        __mpy_obj_ref_dec(__init__);
    }

    __mpy_obj_ref_dec(child);
    child = __mpy_call(B, __mpy_obj_init_tuple(0), NULL);
    __mpy_obj_ref_inc(child);

    __mpy_obj_ref_dec(child);


    __mpy_obj_ref_dec(A);
    __mpy_obj_ref_dec(B);

    return 0;
}

TEST_CASE("CLASS INHERITANCE"){
    CHECK_NOTHROW(inheritance());
}
