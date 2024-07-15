#include "test/Classes/TestClassInitHelpers.h"


__MPyObj* func_init_no_params___init__(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("__init__", args, kwargs, 1);
	__MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	__mpy_obj_ref_dec(__mpy_call(__mpy_super, __mpy_tuple_assign(0, self, __mpy_obj_init_tuple(1)), NULL));
	__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_str_static("[INIT_NO_PARAMS] Print from __init__"), __mpy_obj_init_tuple(1)), NULL));

	__mpy_obj_ref_dec(self);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_init_one_param___init__(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("__init__", args, kwargs, 2);
	__MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
	__MPyObj *param1 = __mpy_args_get_positional(&argHelper, 1, "param1");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	__mpy_obj_ref_dec(__mpy_call(__mpy_super, __mpy_tuple_assign(0, self, __mpy_obj_init_tuple(1)), NULL));
	__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_str_static("[INIT_ONE_PARAM] Print from __init__ Param Value : "), __mpy_tuple_assign(1, param1, __mpy_obj_init_tuple(2))), NULL));

	__mpy_obj_ref_dec(self);
	__mpy_obj_ref_dec(param1);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_init_multiple_params___init__(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("__init__", args, kwargs, 3);
	__MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
	__MPyObj *param1 = __mpy_args_get_positional(&argHelper, 1, "param1");
	__MPyObj *param2 = __mpy_args_get_positional(&argHelper, 2, "param2");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	__mpy_obj_ref_dec(__mpy_call(__mpy_super, __mpy_tuple_assign(0, self, __mpy_obj_init_tuple(1)), NULL));
	__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_str_static("[INIT_MULTIPLE_PARAMS] Print from __init__ Param1 : "), __mpy_tuple_assign(1, param1, __mpy_tuple_assign(2, __mpy_obj_init_str_static(" Param2 : "), __mpy_tuple_assign(3, param2, __mpy_obj_init_tuple(4))))), NULL));

	__mpy_obj_ref_dec(self);
	__mpy_obj_ref_dec(param1);
	__mpy_obj_ref_dec(param2);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}
