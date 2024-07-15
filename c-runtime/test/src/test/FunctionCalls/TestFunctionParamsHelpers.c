#include "test/FunctionCalls/TestFunctionParamsHelpers.h"

__MPyObj* func_func1(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("func1", args, kwargs, 0);
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = __mpy_obj_init_str_static("Function called !");
	goto ret;


	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_func2(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("func2", args, kwargs, 1);
	__MPyObj *param1 = __mpy_args_get_positional(&argHelper, 0, "param1");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = param1;
	goto ret;

	__mpy_obj_ref_dec(param1);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_func3(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("func3", args, kwargs, 2);
	__MPyObj *param1 = __mpy_args_get_positional(&argHelper, 0, "param1");
	__MPyObj *param2 = __mpy_args_get_positional(&argHelper, 1, "param2");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = __mpy_call(__mpy_obj_get_attr(param1, "__add__"), __mpy_tuple_assign(0, param2, __mpy_obj_init_tuple(1)), NULL);
	goto ret;

	__mpy_obj_ref_dec(param1);
	__mpy_obj_ref_dec(param2);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_func4(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("func4", args, kwargs, 1);
	__MPyObj *param1 = __mpy_args_get_positional(&argHelper, 0, "param1");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = param1;
	goto ret;

	__mpy_obj_ref_dec(param1);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_func_throws1(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("func_throws1", args, kwargs, 0);
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = __mpy_obj_init_str_static("");
	goto ret;


	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_throws2(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("throws2", args, kwargs, 1);
	__MPyObj *param1 = __mpy_args_get_positional(&argHelper, 0, "param1");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = param1;
	goto ret;

	__mpy_obj_ref_dec(param1);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_throws3(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("throws3", args, kwargs, 1);
	__MPyObj *param1 = __mpy_args_get_positional(&argHelper, 0, "param1");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = param1;
	goto ret;

	__mpy_obj_ref_dec(param1);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_throws4(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("throws4", args, kwargs, 1);
	__MPyObj *param1 = __mpy_args_get_positional(&argHelper, 0, "param1");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = param1;
	goto ret;

	__mpy_obj_ref_dec(param1);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_throws5(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("throws5", args, kwargs, 2);
	__MPyObj *param1 = __mpy_args_get_positional(&argHelper, 0, "param1");
	__MPyObj *param2 = __mpy_args_get_positional(&argHelper, 1, "param2");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = __mpy_call(__mpy_obj_get_attr(param1, "__add__"), __mpy_tuple_assign(0, param2, __mpy_obj_init_tuple(1)), NULL);
	goto ret;

	__mpy_obj_ref_dec(param1);
	__mpy_obj_ref_dec(param2);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}
