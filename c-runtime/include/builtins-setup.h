#ifndef BUILTINS_SETUP_H
#define BUILTINS_SETUP_H

#include "type-hierarchy/function.h"

extern __MPyObj *__MPyType_Object;

extern __MPyObj *__MPyType_Num;

extern __MPyObj *__MPyType_Tuple;

extern __MPyObj *__MPyType_Function;

extern __MPyObj *__MPyType_BoundMethod;

extern __MPyObj *__MPyType_None;

extern __MPyObj *__MPyType_Type;

extern __MPyObj *__MPyType_Str;

extern __MPyObj *__MPyType_Boolean;

extern __MPyObj *__MPyFunc_id;

extern __MPyObj *__MPyFunc_print;

extern __MPyObj *__MPyFunc_type;

extern __MPyObj *__MPyFunc_input;

extern __MPyObj *__MPyFunc_Type_str;

extern __MPyObj *__MPyFunc_Int_str;

extern __MPyObj *__MPyFunc_Int_bool;

extern __MPyObj *__MPyFunc_Tuple_bool;

extern __MPyObj *__MPyFunc_Str_str;

extern __MPyObj *__MPyFunc_Str_bool;

extern __MPyObj *__MPyFunc_Boolean_str;

extern __MPyObj *__MPyFunc_Boolean_bool;

extern __MPyObj *__MPyFunc_Object_str;

extern __MPyObj *__MPyFunc_Object_new;

extern __MPyObj *__MPyFunc_Object_init;

extern __MPyObj *__mpy_super;

void __mpy_builtins_setup();

void __mpy_builtins_cleanup();

#endif


