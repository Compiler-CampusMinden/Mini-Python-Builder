/* (C)2024 */
package Systemtests.LanguageFeatures.Conditions;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.conditions.IfThenElseStatement;
import CBuilder.conditions.conditionalStatement.ElseStatement;
import CBuilder.conditions.conditionalStatement.IfStatement;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.StringLiteral;
import CBuilder.objects.Call;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestElseStatement {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of(
                        new IfStatement(
                                new BoolLiteral(false),
                                List.of(
                                        new Call(
                                                new Reference("print"),
                                                List.of(
                                                        new Expression[] {
                                                            new StringLiteral(
                                                                    "Went into if Statement")
                                                        })))),
                        Optional.of(
                                new ElseStatement(
                                        List.of(
                                                new Call(
                                                        new Reference("print"),
                                                        List.of(
                                                                new Expression[] {
                                                                    new StringLiteral(
                                                                            "Went into else"
                                                                                    + " Statement")
                                                                }))))),
                        "Went into else Statement\n"),
                Arguments.of(
                        new IfStatement(
                                new BoolLiteral(true),
                                List.of(
                                        new Call(
                                                new Reference("print"),
                                                List.of(
                                                        new Expression[] {
                                                            new StringLiteral(
                                                                    "Went into if Statement")
                                                        })))),
                        Optional.of(
                                new ElseStatement(
                                        List.of(
                                                new Call(
                                                        new Reference("print"),
                                                        List.of(
                                                                new Expression[] {
                                                                    new StringLiteral(
                                                                            "Went into else"
                                                                                    + " Statement")
                                                                }))))),
                        "Went into if Statement\n"));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void else_statement(
            IfStatement ifStatement,
            Optional<ElseStatement> elseStatement,
            String expected,
            @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_else_statement(workDirectory, ifStatement, elseStatement);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(
                testClass
                        + "If Statement : "
                        + ifStatement
                        + " Elif Statement : "
                        + elseStatement
                        + " Result : "
                        + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    /**
     * <p> Mini Python source code :
     * <br>
     * <br> if(false/true):
     * <br>     print("Went inside if!")
     * <br>
     * <br> else:
     * <br>     print("Went inside else!")
     * </p>
     */
    void generate_else_statement(
            Path output, IfStatement ifStatement, Optional<ElseStatement> elseStatement) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addStatement(new IfThenElseStatement(ifStatement, Optional.empty(), elseStatement));

        builder.writeProgram(output);
    }
}
