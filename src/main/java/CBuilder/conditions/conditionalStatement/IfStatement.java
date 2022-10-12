package CBuilder.conditions.conditionalStatement;

import CBuilder.Expression;
import CBuilder.Statement;
import CBuilder.conditions.IfThenElseStatement;

import java.util.List;

/**
 * An if block (Python/MiniPython) of a conditional statement.
 *
 * @see IfThenElseStatement How to utilise this to generate code.
 */
public class IfStatement extends ConditionalStatement {

    /**
     * Create a new if block.
     *
     * @param condition The condition of the if block.
     * @param body The list of statements in the body of the if block.
     */
    public IfStatement(Expression condition,
                       List<Statement> body) {
        super("if", condition, body);
    }

}
