package CBuilder.conditions;

import CBuilder.Statement;
import CBuilder.conditions.conditionalStatement.ElifStatement;
import CBuilder.conditions.conditionalStatement.ElseStatement;
import CBuilder.conditions.conditionalStatement.IfStatement;

import java.util.List;
import java.util.Optional;

/**
 * A MiniPython if (/else if/else) block.
 */
public class IfThenElseStatement implements Statement {

    private IfStatement ifStatement;
    private Optional<List<ElifStatement>> elifStatements;
    private Optional<ElseStatement> elseStatement;

    /**
     * Create a new if/elif/else block.
     *
     * @param ifStatement The if condition & body.
     * @param elifStatements Optional elif conditions & bodies.
     * @param elseStatement Optional else condition & body.
     */
    public IfThenElseStatement(IfStatement ifStatement, Optional<List<ElifStatement>> elifStatements, Optional<ElseStatement> elseStatement) {
        this.ifStatement = ifStatement;
        this.elifStatements = elifStatements;
        this.elseStatement = elseStatement;
    }

    @Override
    public String buildStatement() {
        StringBuilder compoundStatement = new StringBuilder();

        compoundStatement.append(ifStatement.build());

        for (ElifStatement s : elifStatements.orElse(List.of())) {
            compoundStatement.append(s.build());
        }

        elseStatement.ifPresent(statement -> compoundStatement.append(statement.build()));

        return compoundStatement.toString();
    }
}
