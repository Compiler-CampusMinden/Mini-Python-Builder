package CBuilder;

/**
 * Something that returns the C representation of a MiniPython object, e. g. a function call or a literal.
 */
public interface Expression extends Statement {

    /**
     * @return C-code that evaluates to a MiniPython object.
     */
    String buildExpression();

}
