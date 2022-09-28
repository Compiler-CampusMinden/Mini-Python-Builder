package CBuilder.conditions.conditionalStatement;

import CBuilder.Expression;
import CBuilder.Statement;
import CBuilder.conditions.IfThenElseStatement;

import java.util.List;

/**
 * The elif (Python/MiniPython) / else part of an {@link IfThenElseStatement}.
 *
 * @see IfThenElseStatement How to utilise this to generate code.
 */
public class ElifStatement extends ConditionalStatement {
    public ElifStatement(Expression condition,
                         List<Statement> body) {
        super("else if", condition, body);
    }
}
