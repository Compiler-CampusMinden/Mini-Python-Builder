/* (C)2024 */
package Systemtests.LanguageFeatures.Conditions;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.conditions.IfThenElseStatement;
import CBuilder.conditions.conditionalStatement.ElifStatement;
import CBuilder.conditions.conditionalStatement.IfStatement;
import CBuilder.literals.BoolLiteral;
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

public class TestElifStatement {
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
                                List.of(
                                        new ElifStatement(
                                                new BoolLiteral(true),
                                                List.of(
                                                        new Call(
                                                                new Reference("print"),
                                                                List.of(
                                                                        new Expression[] {
                                                                            new StringLiteral(
                                                                                    "Went into Elif"
                                                                                        + " Statement")
                                                                        })))))),
                        "Went into Elif Statement\n"),
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
                                List.of(
                                        new ElifStatement(
                                                new BoolLiteral(false),
                                                List.of(
                                                        new Call(
                                                                new Reference("print"),
                                                                List.of(
                                                                        new Expression[] {
                                                                            new StringLiteral(
                                                                                    "Went into Elif"
                                                                                        + " Statement")
                                                                        })))))),
                        "Went into if Statement\n"));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void elif_statement(
            IfStatement ifStatement,
            Optional<List<ElifStatement>> elifStatement,
            String expected,
            @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_elif_statement(workDirectory, ifStatement, elifStatement);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(
                testClass
                        + "If Statement : "
                        + ifStatement
                        + " Elif Statement : "
                        + elifStatement
                        + " Result : "
                        + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    /**
     * Mini Python source code : <br>
     * <br>
     * if(false/true): <br>
     * print("Went inside if!") <br>
     * <br>
     * elif(true/false): <br>
     * print("Went inside elif!")
     */
    void generate_elif_statement(
            Path output, IfStatement ifStatement, Optional<List<ElifStatement>> elifStatement) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addStatement(new IfThenElseStatement(ifStatement, elifStatement, Optional.empty()));

        builder.writeProgram(output);
    }
}
