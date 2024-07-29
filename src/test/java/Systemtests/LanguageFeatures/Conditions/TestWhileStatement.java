/* (C)2024 */
package Systemtests.LanguageFeatures.Conditions;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.conditions.conditionalStatement.WhileStatement;
import CBuilder.literals.IntLiteral;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestWhileStatement {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of(
                        new Call(
                                new AttributeReference("__gt__", new Reference("a")),
                                List.of(new Expression[] {new IntLiteral(1)})),
                        List.of(
                                new Call(
                                        new Reference("print"),
                                        List.of(new Expression[] {new Reference("a")})),
                                new Assignment(
                                        new Reference("a"),
                                        new Call(
                                                new AttributeReference(
                                                        "__sub__", new Reference("a")),
                                                List.of(new Expression[] {new IntLiteral(1)})))),
                        "3\n2\n"));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void while_statement(
            Expression condition,
            List<Statement> statementList,
            String expected,
            @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_while_statement(workDirectory, condition, statementList);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + "Condition : " + condition + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    /**
     * Mini Python source code : <br>
     * <br>
     * a = 3 <br>
     * <br>
     * while(a > 1): <br>
     * print(a) <br>
     * a = a - 1
     */
    void generate_while_statement(
            Path output, Expression condition, List<Statement> statementList) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addVariable(new VariableDeclaration("a"));
        builder.addStatement(new Assignment(new Reference("a"), new IntLiteral(3)));

        builder.addStatement(new WhileStatement(condition, statementList));

        builder.writeProgram(output);
    }
}
