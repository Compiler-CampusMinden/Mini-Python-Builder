package CBuilder.conditions.conditionalStatement;

import CBuilder.Expression;
import CBuilder.Statement;
import CBuilder.conditions.IfThenElseStatement;

import java.util.List;

/**
 * The if part of an {@link IfThenElseStatement}.
 *
 * @see IfThenElseStatement How to utilise this to generate code.
 */
public class IfStatement extends ConditionalStatement {

    public IfStatement(Expression condition,
                       List<Statement> body) {
        super("if", condition, body);
    }

}
