
#include "mpy_obj.h"

/**
 * @file
 *
 * Ease extracting arguments for 'regular' functions that use positional and/or keyword arguments.
 *
 * Usage:
 *
 * ```c
 *   // initialise the state
 *   __MPyGetArgsState argHelper = __mpy_args_init("YOUR_NAME", args, kwargs, CNT_ARGS);
 *
 *   // query the positional parameters (must happen in order of the parameters)
 *   __MPyObj *arg0 = __mpy_args_get_positional(&argHelper, 0, "arg0");
 *   __MPyObj *arg1 = __mpy_args_get_positional(&argHelper, 1, "arg1");
 *
 *   // now query any keyword-only arguments
 *   TODO
 *
 *   // finally: make sure there haven't been extraneous parameters
 *   // (and clean up the args and kwargs data structures)
 *   __mpy_args_finish(&argHelper)
 * ```
 */

/**
 * The internal state for extracting parameters.
 *
 * @see Initialised with __mpy_args_init
 */
typedef struct __MPyGetArgsState {
    const char *funcName;
    __MPyObj *args;
    __MPyObj *kwargs;
    bool encounteredKwarg;
    unsigned int countPositionalArgs;
} __MPyGetArgsState;

/**
 * Initialises argument extraction.
 *
 * Note: Calling this function transfers ownership of
 * both args and kwargs to the returned state struct.
 * Their state may be arbitrarily modified by the argument handling functions in this header
 * and therefore should not be manually used.
 * Since __mpy_args_finish takes care of their ref-counters,
 * manually decreasing them is not needed.
 *
 * @param funcName Name of the function the arguments are extracted for.
 * @param args The args datastructure the function received.
 * @param kwargs The kwargs datastructure the function received.
 * @param countPositionalArgs The total number of positional arguments.
 * @return The extraction state ready for usage.
 */
__MPyGetArgsState __mpy_args_init(const char *funcName, __MPyObj *args, __MPyObj *kwargs, unsigned int countPositionalArgs);

/**
 * Tries to retrieve a positional argument.
 *
 * Note: Must be called in order of the declaration of the arguments
 * (i. e. first retrieve the first argument, then the second, then the third).
 *
 * Since exceptions are currently not supported, it simply exits the program with an error code.
 *
 * @param state The extraction state.
 * @param position The position of the argument.
 * @param argName The name of the argument.
 * @return The arguments value.
 */
__MPyObj *__mpy_args_get_positional(__MPyGetArgsState *state, unsigned int position, const char *argName);

/**
 * Finish argument retrieval after extracting all expected arguments.
 *
 * Note: Do not use state after calling this method.
 *
 * Note: Decreases the ref-count of both args and kwargs. Generally,
 * both should not be manually decreased.
 *
 * Performs final checks after all expected arguments have been retrieved.
 * Currently this means checking that all positional arguments in args have been used
 * and that no keyword arguments were unused.
 * 
 * @param state The extraction state.
 */
void __mpy_args_finish(__MPyGetArgsState *state);

