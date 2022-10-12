package CBuilder.literals;

/**
 * A simple integer literal.
 */
public class IntLiteral implements Literal {

    /**
     * The containing integer value.
     */
    long value;

    /**
     * Create a new integer literal with the specified value.
     *
     * @param value The integer value to use.
     */
    public IntLiteral(long value) {
        this.value = value;
    }

    @Override
    public String buildExpression() {
        return "__mpy_obj_init_int(" + value + ")";
    }

    @Override
    public String buildStatement() {
        return buildExpression() + ";\n";
    }
}
