package CBuilder;

/**
 * Something that returns the C representation of a MiniPython object, e. g. a function call or a
 * literal.
 */
public interface Expression extends Statement {

    /**
     * Create the c-code of the representing expression.
     *
     * @return A String with the c-code that evaluates to the containing MiniPython object.
     */
    String buildExpression();
}
