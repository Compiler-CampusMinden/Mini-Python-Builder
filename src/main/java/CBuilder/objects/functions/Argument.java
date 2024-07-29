package CBuilder.objects.functions;

/** A mini-python (positional) function argument. */
public class Argument {

    /** The name of the argument. */
    private final String name;

    /** The position of the argument. */
    private final int position;

    /**
     * Create a new argument for the given name and position
     *
     * @param name The name of the argument.
     * @param position The position of the argument in the parameter list of the function.
     */
    public Argument(String name, int position) {
        this.name = name;
        this.position = position;
    }

    /**
     * Create c-code for declaring and initialising a variable for this argument in a function.
     *
     * @return Code extracting this argument from a function's argument extractor.
     */
    public String buildArgExtraction() {
        return "__MPyObj *"
                + name
                + " = __mpy_args_get_positional(&argHelper, "
                + position
                + ", \""
                + name
                + "\");\n";
    }

    /**
     * Create the c-code for cleaning up the argument object.
     *
     * @return A string which contains the c-code for cleaning up the mini-python object of this
     *     argument.
     */
    public String buildArgCleanup() {
        return "__mpy_obj_ref_dec(" + name + ");\n";
    }
}
