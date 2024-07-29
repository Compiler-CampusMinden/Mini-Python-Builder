package CBuilder.conditions.conditionalStatement;

import CBuilder.Expression;
import CBuilder.Statement;
import CBuilder.conditions.IfThenElseStatement;
import java.util.List;

/**
 * An elif block (Python/MiniPython) of a conditional statement.
 *
 * @see IfThenElseStatement How to utilise this to generate code.
 */
public class ElifStatement extends ConditionalStatement {

    /**
     * Create a new elif block.
     *
     * @param condition The condition of the elif block.
     * @param body The list of statements in the body of the elif block.
     */
    public ElifStatement(Expression condition, List<Statement> body) {
        super("else if", condition, body);
    }
}
