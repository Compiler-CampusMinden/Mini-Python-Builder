/* (C)2024 */
package Systemtests.LanguageFeatures.Conditions;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.conditions.IfThenElseStatement;
import CBuilder.conditions.conditionalStatement.IfStatement;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import CBuilder.objects.Call;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestIfStatement {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of(
                        new BoolLiteral(false),
                        List.of(
                                new Call(
                                        new Reference("print"),
                                        List.of(
                                                new Expression[] {
                                                    new StringLiteral("Went into if Statement")
                                                }))),
                        ""),
                Arguments.of(
                        new BoolLiteral(true),
                        List.of(
                                new Call(
                                        new Reference("print"),
                                        List.of(
                                                new Expression[] {
                                                    new StringLiteral("Went into if Statement")
                                                }))),
                        "Went into if Statement\n"),
                Arguments.of(
                        new IntLiteral(0),
                        List.of(
                                new Call(
                                        new Reference("print"),
                                        List.of(
                                                new Expression[] {
                                                    new StringLiteral("Went into if Statement")
                                                }))),
                        ""),
                Arguments.of(
                        new IntLiteral(1),
                        List.of(
                                new Call(
                                        new Reference("print"),
                                        List.of(
                                                new Expression[] {
                                                    new StringLiteral("Went into if Statement")
                                                }))),
                        "Went into if Statement\n"));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void if_statement(
            Expression condition,
            List<Statement> statementList,
            String expected,
            @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_if_statement(workDirectory, condition, statementList);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + "Condition : " + condition + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    /**
     * Mini Python source code : <br>
     * <br>
     * if(false/true, 0, 1): <br>
     * print("Went into if Statement") <br>
     */
    void generate_if_statement(Path output, Expression condition, List<Statement> statementList) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addStatement(
                new IfThenElseStatement(
                        new IfStatement(condition, statementList),
                        Optional.empty(),
                        Optional.empty()));

        builder.writeProgram(output);
    }
}
