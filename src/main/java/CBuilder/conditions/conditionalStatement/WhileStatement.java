package CBuilder.conditions.conditionalStatement;

import CBuilder.Expression;
import CBuilder.Statement;

import java.util.List;

public class WhileStatement extends ConditionalStatement implements Statement {

    public WhileStatement(Expression condition,
                          List<Statement> body) {
        super("while", condition, body);
    }

    @Override
    public String buildStatement() {
        return this.build();
    }
}
