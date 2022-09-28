package CBuilder.variables;

/**
 * Declaration of a variable.
 *
 * This makes the variable known to the c compiler.
 */
public class VariableDeclaration {

    protected String name;

    public VariableDeclaration(String name) {
        this.name = name;
    }

    private String initialisation() {
        // FIXME: init none instead of object
        return " = __mpy_obj_init_object();\n" +
                "__mpy_obj_ref_inc(" + name + ")";
    }

    public String build(boolean initialize) {
        String init = "";
        if (initialize) {
            init = initialisation();
        }
        return "__MPyObj *" + name + init + ";\n";
    }

    public String build() {
        return build(true);
    }

    public String buildInitialisation() {
        return name + initialisation() + ";\n";
    }

    public String buildRefDec() {
        return "__mpy_obj_ref_dec(" + name + ");\n";
    }

}
