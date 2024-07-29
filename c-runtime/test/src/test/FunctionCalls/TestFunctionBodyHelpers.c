#include "test/FunctionCalls/TestFunctionBodyHelpers.h"

__MPyObj* func_minimalistic_body(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("minimalistic_body", args, kwargs, 0);
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = __mpy_obj_init_int(1);
	goto ret;


	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_print_from_func(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("print_from_func", args, kwargs, 0);
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_str_static("Print from function body"), __mpy_obj_init_tuple(1)), NULL));
	retValue = __mpy_obj_init_int(1);
	goto ret;


	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_return_local_var(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("return_local_var", args, kwargs, 0);
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	__MPyObj *y = __mpy_obj_init_object();
	__mpy_obj_ref_inc(y);
	__mpy_obj_ref_dec(y);
	y = __mpy_obj_init_int(1);
	__mpy_obj_ref_inc(y);
	retValue = y;
	goto ret;


	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_return_param_body(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("return_param_body", args, kwargs, 1);
	__MPyObj *x = __mpy_args_get_positional(&argHelper, 0, "x");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = x;
	goto ret;

	__mpy_obj_ref_dec(x);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_return_add(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("return_add", args, kwargs, 2);
	__MPyObj *x = __mpy_args_get_positional(&argHelper, 0, "x");
	__MPyObj *y = __mpy_args_get_positional(&argHelper, 1, "y");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = __mpy_call(__mpy_obj_get_attr(x, "__add__"), __mpy_tuple_assign(0, y, __mpy_obj_init_tuple(1)), NULL);
	goto ret;

	__mpy_obj_ref_dec(x);
	__mpy_obj_ref_dec(y);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_return_add_conditional(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("return_add_conditional", args, kwargs, 2);
	__MPyObj *x = __mpy_args_get_positional(&argHelper, 0, "x");
	__MPyObj *y = __mpy_args_get_positional(&argHelper, 1, "y");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	if (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_call(__mpy_obj_get_attr(x, "__ge__"), __mpy_tuple_assign(0, __mpy_obj_init_int(0), __mpy_obj_init_tuple(1)), NULL), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
		retValue = __mpy_call(__mpy_obj_get_attr(x, "__add__"), __mpy_tuple_assign(0, y, __mpy_obj_init_tuple(1)), NULL);
	goto ret;
	}
	__mpy_obj_ref_dec(x);
	__mpy_obj_ref_dec(y);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}
