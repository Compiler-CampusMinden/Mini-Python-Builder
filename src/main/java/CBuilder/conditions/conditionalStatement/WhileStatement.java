package CBuilder.conditions.conditionalStatement;

import CBuilder.Expression;
import CBuilder.Statement;
import java.util.List;

/** An while loop with condition and statements. */
public class WhileStatement extends ConditionalStatement implements Statement {

    /**
     * Create a new while loop.
     *
     * @param condition The condition of the while loop.
     * @param body The list of statements in the body of the while loop.
     */
    public WhileStatement(Expression condition, List<Statement> body) {
        super("while", condition, body);
    }

    @Override
    public String buildStatement() {
        return this.build();
    }
}
