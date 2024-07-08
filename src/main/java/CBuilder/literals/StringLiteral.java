package CBuilder.literals;

/** A simple string literal. */
public class StringLiteral implements Literal {

    /** The containing string value. */
    String value;

    /**
     * Create a new string literal with the specified value.
     *
     * @param value The string value to use.
     */
    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public String buildExpression() {
        return "__mpy_obj_init_str_static(\"" + value + "\")";
    }

    @Override
    public String buildStatement() {
        return "__mpy_obj_ref_dec" + buildExpression() + ");\n";
    }
}
