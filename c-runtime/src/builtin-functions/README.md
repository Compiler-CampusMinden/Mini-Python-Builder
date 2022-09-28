
# Builtins

Manual implementation of builtin functions in C.

# Implementation Notes

Take great care to correctly implement the object handling by following the rules outlined below:

- parameters are refCount increased at the start of the function and decreased just before returning
- if a parameter is returned: the ref count is not decreased, instead it is prepared for returning it (`__mpy_obj_return`)
- new objects: 
    - if returned: prepare for returning it (`__mpy_obj_return`)
    - if not-returned: decrease refCount once 

The `TestWithCToolchains` runs `valgrind` on a generated program,
so it's pretty easy to spot errors if the program generation accounts for the builtin you want to test.
Simply adjust the `generateProgram` method.