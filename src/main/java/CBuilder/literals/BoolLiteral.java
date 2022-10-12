package CBuilder.literals;

/**
 * A simple boolean literal.
 */
public class BoolLiteral implements Literal {

    /**
     * The containing boolean value.
     */
    boolean value;

    /**
     * Create a new boolean literal.
     *
     * @param value The boolean value to use.
     */
    public BoolLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public String buildExpression() {
        return "__mpy_obj_init_boolean(" + value + ")";
    }

    @Override
    public String buildStatement() {
        return buildExpression() + ";\n";
    }

}
