package CBuilder.objects.functions;

import CBuilder.Reference;

/**
 * A mini-python (positional) function argument.
 */
public class Argument {

    private final String name;
    private final int position;

    /**
     * @param name The name of the argument.
     * @param position The position of the argument in the parameter list of the function.
     */
    public Argument(String name, int position) {
        this.name = name;
        this.position = position;
    }

    /**
     * Returns C code for declaring and initialising a variable for this argument in a function.
     *
     * @return Code extracting this argument from a function's argument extractor.
     */
    public String buildArgExtraction() {
        return "__MPyObj *" + name + " = __mpy_args_get_positional(&argHelper, " + position + ", \"" + name + "\");\n";
    }

    /**
     * @return C code for cleaning up the mini-python object of this argument.
     */
    public String buildArgCleanup() {
        return "__mpy_obj_ref_dec(" + name + ");\n";
    }

}
