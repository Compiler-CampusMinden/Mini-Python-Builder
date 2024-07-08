package CBuilder.conditions.conditionalStatement;

import CBuilder.Statement;
import CBuilder.conditions.IfThenElseStatement;
import java.util.List;

/**
 * An else block (Python/MiniPython) of a conditional statement.
 *
 * @see IfThenElseStatement How to utilise this
 */
public class ElseStatement implements Statement {

    private List<Statement> body;

    public ElseStatement(List<Statement> body) {
        this.body = body;
    }

    /**
     * Generate the c-code of the representing else block.
     *
     * @return A string which containing the c-code representation of the else block.
     */
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
    public String buildStatement() {
        return null;
    }
}
