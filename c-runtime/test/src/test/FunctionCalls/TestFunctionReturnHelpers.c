#include "test/FunctionCalls/TestFunctionReturnHelpers.h"

__MPyObj* func_return_expression(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("return_expression", args, kwargs, 0);
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = __mpy_obj_init_boolean(__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(true), "__bool__"), __mpy_obj_init_tuple(0), NULL)) && __mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(true), "__bool__"), __mpy_obj_init_tuple(0), NULL)));
	goto ret;


	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_return_tuple_literal_throws(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("return_tuple_literal_throws", args, kwargs, 0);
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = __mpy_obj_init_tuple(0);
	goto ret;


	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_return_void_function_call_print(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("return_void_function_call_print", args, kwargs, 0);
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = __mpy_call(print, __mpy_obj_init_tuple(0), NULL);
	goto ret;


	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}
