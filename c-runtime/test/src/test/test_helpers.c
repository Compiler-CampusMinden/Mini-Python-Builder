#include "test/test_helpers.h"

#include <stdio.h>

extern void print_mpyobj(__MPyObj *mpyobj){
    printf("mpyobj->mpyobj %p\n", (void*)mpyobj);
    printf("mpyobj->refCount %d\n", mpyobj->refCount);
    printf("mpyobj->id %d\n\n", mpyobj->id);
}

extern void print_mpyobj_int(__MPyObj *mpyobj){
    printf("mpyobj %p\n", (void*)mpyobj);
    printf("mpyobj->refCount %d\n", mpyobj->refCount);
    printf("mpyobj->id %d\n", mpyobj->id);
    printf("mpyobj->content value : %d\n\n", (*(int*)(mpyobj->content)));
}

extern void print_mpyobj_str(__MPyObj *mpyobj){
        printf("mpyobj %p\n", (void*)mpyobj);
        printf("mpyobj->refCount %d\n", mpyobj->refCount);
        printf("mpyobj->id %d\n", mpyobj->id);
        printf("mpyobj->content->string : %s\n\n", (((__MPyStrContent*)mpyobj->content)->string));
}

extern void print_mpyobj_bool(__MPyObj *mpyobj){
        printf("mpyobj %p\n", (void*)mpyobj);
        printf("mpyobj->refCount %d\n", mpyobj->refCount);
        printf("mpyobj->id %d\n", mpyobj->id);
        printf("mpyobj->content->bool : %d\n\n", ((MPyBooleanContent*)mpyobj->content)->value);
}
