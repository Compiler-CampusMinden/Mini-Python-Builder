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

    /**
     * The containing if block.
     */
    private IfStatement ifStatement;

    /**
     * A list of the containing elif blocks.
     */
    private Optional<List<ElifStatement>> elifStatements;

    /**
     * The containing else block.
     */
    private Optional<ElseStatement> elseStatement;

    /**
     * Create a new if/elif/else block.
     *
     * @param ifStatement The if statement.
     * @param elifStatements An optional list of elif statements.
     * @param elseStatement An optional else statement.
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
