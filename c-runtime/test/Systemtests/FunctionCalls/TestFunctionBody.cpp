#define CATCH_CONFIG_RUNNER

#include <stddef.h>

#include "catch.hpp"
#include "test/test_helpers.h"
#include "test/FunctionCalls/TestFunctionBodyHelpers.h"

__MPyObj *minimalistic_body;

__MPyObj *print_from_func;

__MPyObj *return_local_var;

__MPyObj *return_param_body;

__MPyObj *return_add;

__MPyObj *return_add_conditional;

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

int minimal_body(){
    __MPyObj *a;

    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    minimalistic_body = __mpy_obj_init_func(&func_minimalistic_body);
    __mpy_obj_ref_inc(minimalistic_body);


    __mpy_obj_ref_dec(a);
    a = __mpy_call(minimalistic_body, __mpy_obj_init_tuple(0), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    __mpy_obj_ref_dec(minimalistic_body);

    return (*(int*)(a->content));
}

int print_from_func_body(){
    __MPyObj *a;

    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    print_from_func = __mpy_obj_init_func(&func_print_from_func);
    __mpy_obj_ref_inc(print_from_func);

    __mpy_obj_ref_dec(a);
    a = __mpy_call(print_from_func, __mpy_obj_init_tuple(0), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    __mpy_obj_ref_dec(print_from_func);

    return (*(int*)(a->content));
}

int local_var_body(){
    __MPyObj *a;

    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    return_local_var = __mpy_obj_init_func(&func_return_local_var);
    __mpy_obj_ref_inc(return_local_var);


    __mpy_obj_ref_dec(a);
    a = __mpy_call(return_local_var, __mpy_obj_init_tuple(0), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    __mpy_obj_ref_dec(return_local_var);

    return (*(int*)(a->content));
}

int param_body(){
    __MPyObj *a;

    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    return_param_body = __mpy_obj_init_func(&func_return_param_body);
    __mpy_obj_ref_inc(return_param_body);


    __mpy_obj_ref_dec(a);
    a = __mpy_call(return_param_body, __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1)), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    __mpy_obj_ref_dec(return_param_body);

    return (*(int*)(a->content));
}

int return_add_body(){
    __MPyObj *a;

    a = __mpy_obj_init_object();
    __mpy_obj_ref_inc(a);

    return_add = __mpy_obj_init_func(&func_return_add);
    __mpy_obj_ref_inc(return_add);


    __mpy_obj_ref_dec(a);
    a = __mpy_call(return_add, __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_tuple_assign(1, __mpy_obj_init_int(3), __mpy_obj_init_tuple(2))), NULL);
    __mpy_obj_ref_inc(a);

    __mpy_obj_ref_dec(a);

    __mpy_obj_ref_dec(return_add);

    return (*(int*)(a->content)) == 4 ? 1 : 0;
}

int return_add_conditional_body(){
    __MPyObj *a;

    a = __mpy_obj_init_object();
	__mpy_obj_ref_inc(a);

	return_add_conditional = __mpy_obj_init_func(&func_return_add_conditional);
	__mpy_obj_ref_inc(return_add_conditional);


	__mpy_obj_ref_dec(a);
	a = __mpy_call(return_add_conditional, __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_tuple_assign(1, __mpy_obj_init_int(3), __mpy_obj_init_tuple(2))), NULL);
	__mpy_obj_ref_inc(a);

	__mpy_obj_ref_dec(a);

	__mpy_obj_ref_dec(return_add_conditional);

    return (*(int*)(a->content)) == 4 ? 1 : 0;
}

TEST_CASE("DIFFERENT FUNCTION BODIES"){
    CAPTURE(minimal_body());
    CAPTURE(print_from_func_body()); // CAPTURE prints one additional time.

    CHECK(minimal_body() == 1);
    CHECK(print_from_func_body() == 1);
    CHECK(local_var_body() == 1);
    CHECK(param_body() == 1);
    CHECK(return_add_body() == 1);
    CHECK(return_add_conditional_body() == 1);
}
