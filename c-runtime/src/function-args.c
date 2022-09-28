
#include <stddef.h>
#include <assert.h>
#include <stdbool.h>

#include "literals/tuple.h"
#include "function-args.h"
#include "errors.h"

__MPyGetArgsState __mpy_args_init(const char *funcName, __MPyObj *args, __MPyObj *kwargs, unsigned int countPositionalArgs) {
    assert(funcName != NULL && args != NULL && kwargs != NULL);

    return (__MPyGetArgsState) {
        .funcName = funcName,
        .args = args,
        .kwargs = kwargs,
        .encounteredKwarg = false,
        .countPositionalArgs = countPositionalArgs,
    };
}

__MPyObj *__mpy_args_get_positional(__MPyGetArgsState *state, unsigned int position, const char *name) {
    assert(state != NULL && name != NULL);

    unsigned int argCnt = __mpy_tuple_size(state->args);
    if (position < argCnt) {
        __MPyObj *arg = __mpy_tuple_get_at(state->args, position);
        __mpy_obj_ref_inc(arg);
        return arg;
    }

    // handling kwargs needs to set the `.encounteredKwarg` field:
    // The first time a positional argument is passed as a kwarg, all other arguments need to be passed as kwargs too
    //      (since when calling methods, the positional arguments MUST come first).
    fprintf(stderr, "failed to find argument '%s'\n", name);
    __MPY_TODO("check kwargs");
    return NULL; // can never happen with the TODO above
}

void __mpy_args_finish(__MPyGetArgsState *state) {
    assert(state != NULL);

    unsigned int argCnt = __mpy_tuple_size(state->args);
    // the only interesting case here is receiving more args than expected,
    // since missing args are more difficult to detect (a param may have been passed as keyword instead).
    // Therefore this relies on __mpy_get_arg to detect all missing args.
    if (argCnt > state->countPositionalArgs) {
        fprintf(stderr, "TypeError: %s() takes %i positional arguments but %i were given\n", state->funcName, state->countPositionalArgs, argCnt);
        __mpy_fatal_error(__MPY_ERROR_USER);
    }

    // TODO check that kwargs is empty (i. e. all kwargs have been used)
    __MPY_NOTE("(bug) ignoring unexpected keyword arguments");
    
    __mpy_obj_ref_dec(state->args);
    __mpy_obj_ref_dec(state->kwargs);
}
