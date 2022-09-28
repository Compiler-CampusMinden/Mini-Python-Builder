#include "checks.h"

#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <assert.h>

#include "mpy_obj.h"
#include "errors.h"
#include "literals/tuple.h"

void *__mpy_checked_malloc(size_t size) {
    void *mem = malloc(size);
    if (mem == NULL) {
        fprintf(stderr, "ERROR: Out of Memory");
        __mpy_fatal_error(__MPY_ERROR_EXTERNAL);
    }

    return mem;
}


