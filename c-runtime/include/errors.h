#ifndef ERRORS_H
#define ERRORS_H

#include <stdio.h>

#ifdef DEBUG
#define __MPY_NOTE(msg) fprintf(stderr, "NOTE(%s:%i): %s\n", __FILE__, __LINE__, msg);
#else
#define __MPY_NOTE(msg)
#endif

#define __MPY_TODO(msg) fprintf(stderr, "TODO(%s:%i): %s\n", __FILE__, __LINE__, msg);\
    __mpy_fatal_error(__MPY_ERROR_UNIMPLEMENTED);

#define __MPY_BUG(msg) fprintf(stderr, "BUG(%s:%i): %s\n", __FILE__, __LINE__, msg);\
    __mpy_fatal_error(__MPY_ERROR_INTERNAL);

typedef enum __MPyError {
    __MPY_ERROR_NO_ERROR = 0,
    __MPY_ERROR_USER = 1, // programmer error
    __MPY_ERROR_INTERNAL = 2, // something that cannot happen happened
    __MPY_ERROR_EXTERNAL = 3, // some external component, e. g. malloc, failed
    __MPY_ERROR_UNIMPLEMENTED = 4,
} __MPyError;

 __attribute__((noreturn)) void __mpy_fatal_error(__MPyError error);

#endif
