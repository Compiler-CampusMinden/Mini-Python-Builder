package CBuilder.variables;

/**
 * Declaration of a variable.
 * This makes the variable known to the c compiler.
 */
public class VariableDeclaration {

    /**
     * The name of the variable.
     */
    protected String name;

    /**
     * Create a new variable declaration.
     *
     * @param name The name of the variable.
     */
    public VariableDeclaration(String name) {
        this.name = name;
    }

    /**
     * Create the c-code for initializing an object to variable.
     *
     * @return A string with c-code which represents the object initialization for the variable.
     */
    private String initialisation() {
        // FIXME: init none instead of object
        return " = __mpy_obj_init_object();\n" +
                "__mpy_obj_ref_inc(" + name + ")";
    }

    /**
     * Create the c-code for declaring an object to the variable.
     *
     * @param initialize Boolean to chose if object initialization should be included.
     * @return A string with c-code which represents the variable declaration.
     */
    public String build(boolean initialize) {
        String init = "";
        if (initialize) {
            init = initialisation();
        }
        return "__MPyObj *" + name + init + ";\n";
    }

    /**
     * Create the c-code for initialization the variable.
     *
     * @return A string which represents the c-code of the variable initialization.
     */
    public String buildInitialisation() {
        return name + initialisation() + ";\n";
    }

    /**
     * Create the c-code to decrement the reference counter of the variable.
     *
     * @return A string which represents the c-code to decrement the reference counter of the variable
     */
    public String buildRefDec() {
        return "__mpy_obj_ref_dec(" + name + ");\n";
    }

}
