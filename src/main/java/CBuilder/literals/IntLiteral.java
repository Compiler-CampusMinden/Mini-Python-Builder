package CBuilder.literals;

/**
 * A number.
 */
public class IntLiteral implements Literal {

    /**
     * The value itself.
     */
    long value;

    /**
     * Create a new literal with the specified value.
     * @param value The new value.
     */
    public IntLiteral(long value) {
        this.value = value;
    }

    /**
     * @return The C representation of a MiniPython object with the initially specified value.
     */
    public String buildExpression() {
        return "__mpy_obj_init_int(" + value + ")";
    }

    public String buildStatement() {
        return buildExpression() + ";\n";
    }
}
