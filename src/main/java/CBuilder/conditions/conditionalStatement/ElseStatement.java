package CBuilder.conditions.conditionalStatement;

import CBuilder.Statement;
import CBuilder.conditions.IfThenElseStatement;

import java.util.List;

/**
 * The else part of an {@link IfThenElseStatement}.
 *
 * @see IfThenElseStatement How to utilise this to generate code.
 */
public class ElseStatement implements Statement {

    private List<Statement> body;

    public ElseStatement(List<Statement> body) {
        this.body = body;
    }

    public String build() {
        StringBuilder statement = new StringBuilder();

        statement.append("else {\n");

        for (Statement s : body) {
            statement.append("\t" + s.buildStatement());
        }

        statement.append("}");

        return statement.toString();
    }

    @Override
    public String buildStatement() { return null; }
}
