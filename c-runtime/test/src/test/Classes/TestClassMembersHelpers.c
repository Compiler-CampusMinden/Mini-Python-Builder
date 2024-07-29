#include "test/Classes/TestClassMembersHelpers.h"


__MPyObj* func_getter_setter___init__(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("__init__", args, kwargs, 2);
	__MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
	__MPyObj *val = __mpy_args_get_positional(&argHelper, 1, "val");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	__mpy_obj_ref_dec(__mpy_call(__mpy_super, __mpy_tuple_assign(0, self, __mpy_obj_init_tuple(1)), NULL));
	__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_str_static("[B] Print from __init__"), __mpy_obj_init_tuple(1)), NULL));
	__mpy_obj_set_attr(self, "x", val);
	__mpy_obj_ref_dec(self);
	__mpy_obj_ref_dec(val);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_getter_setter_getX(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("getX", args, kwargs, 1);
	__MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	retValue = __mpy_obj_get_attr(self, "x");
	goto ret;

	__mpy_obj_ref_dec(self);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}

__MPyObj* func_getter_setter_setX(__MPyObj *args, __MPyObj *kwargs) {
	assert(args != NULL && kwargs != NULL);

	__MPyGetArgsState argHelper = __mpy_args_init("setX", args, kwargs, 2);
	__MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
	__MPyObj *newVal = __mpy_args_get_positional(&argHelper, 1, "newVal");
	__mpy_args_finish(&argHelper);

	__MPyObj *retValue = NULL;

	__mpy_obj_set_attr(self, "x", newVal);
	__mpy_obj_ref_dec(self);
	__mpy_obj_ref_dec(newVal);

	goto ret;
	ret:
	if (retValue == NULL) {
		retValue = __mpy_obj_init_object();
	}
	return __mpy_obj_return(retValue);
}
