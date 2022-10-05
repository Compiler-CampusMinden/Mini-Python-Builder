#include "builtin-functions/input.h"

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>

#include "errors.h"
#include "function-args.h"
#include "literals/str.h"

// NOLINTNEXTLINE(bugprone-easily-swappable-parameters)
__MPyObj* __mpy_func_input(__MPyObj *args, __MPyObj *kwargs) {
    assert(args != NULL && kwargs != NULL);

    __MPyGetArgsState argHelper = __mpy_args_init("input", args, kwargs, 0);
    __mpy_args_finish(&argHelper);

    char *line = NULL;
    size_t capacity = 0;
    size_t lineLen = 0;

    lineLen = getline(&line, &capacity, stdin);
    if (lineLen == (size_t) -1) {
        fprintf(stderr, "Failed to read from stdin");
        __mpy_fatal_error(__MPY_ERROR_EXTERNAL);
    }

    // now strip delimiter, i.e. newline:
    //
    // getline returns number of characters read,
    // including delimiter ('\n') but excluding null byte
    //
    // check for windows style line ending '\r\n' first
    if (lineLen > 1 && line[lineLen - 2] == '\r') {
        line[lineLen - 2] = '\0';
    // otherwise single char line ending
    } else if (lineLen > 0) {
        line[lineLen - 1] = '\0';
    }
    // if string is empty, there's nothing to remove, so no else

    __MPyObj *lineStr = __mpy_obj_init_str_dynamic(line);
    return __mpy_obj_return(lineStr);
}
