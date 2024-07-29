#include "test/FunctionCalls/TestFunctionRecursionHelpers.h"

__MPyObj *recursion;

__MPyObj* func_recursion(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("recursion", args, kwargs, 1);
	__MPyObj *x = __mpy_args_get_positional(&argHelper, 0, "x");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	if (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_call(__mpy_obj_get_attr(x, "__gt__"), __mpy_tuple_assign(0, __mpy_obj_init_int(0), __mpy_obj_init_tuple(1)), NULL), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
		__mpy_obj_ref_dec(x);
	x = __mpy_call(__mpy_obj_get_attr(x, "__sub__"), __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1)), NULL);
	__mpy_obj_ref_inc(x);
		__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, x, __mpy_obj_init_tuple(1)), NULL));
		retValue = __mpy_call(recursion, __mpy_tuple_assign(0, x, __mpy_obj_init_tuple(1)), NULL);
	goto ret;
	}retValue = x;
	goto ret;

	__mpy_obj_ref_dec(x);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}
