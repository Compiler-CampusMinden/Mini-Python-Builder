
#include "errors.h"

#include <stdlib.h>
#include <stdio.h>

void __attribute__((noreturn)) __mpy_fatal_error(__MPyError error) {
    if (error != __MPY_ERROR_USER) { // assume relevant implementation took care of informing user
        fprintf(stderr, "Fatal error occurred! Exiting.\n");
    }
    exit(error);
}

