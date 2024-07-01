/* (C)2024 */
package Systemtests.LanguageFeatures.FunctionCalls;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.keywords.bool.AndKeyword;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import CBuilder.literals.TupleLiteral;
import CBuilder.objects.Call;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import CBuilder.objects.functions.ReturnStatement;
import CBuilder.variables.VariableDeclaration;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestFunctionReturn {
    // TODO cannot return tuple literal from function without runtime exception
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    @ParameterizedTest
    @MethodSource("sources_equals")
    void function_equals(
            String funcName,
            List<Statement> body,
            List<Argument> positionalArgs,
            List<VariableDeclaration> localVariables,
            List<Expression> args,
            String expected,
            @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_function(workDirectory, funcName, body, positionalArgs, localVariables, args);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("sources_fails")
    void function_fails(
            String funcName,
            List<Statement> body,
            List<Argument> positionalArgs,
            List<VariableDeclaration> localVariables,
            List<Expression> args,
            @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result = "";

        generate_function(workDirectory, funcName, body, positionalArgs, localVariables, args);

        makeProgram(workDirectory);

        assertThrows(RuntimeException.class, () -> getProgramOutput(workDirectory));

        System.out.println(testClass + " Result : " + result);
    }

    void generate_function(
            Path output,
            String funcName,
            List<Statement> body,
            List<Argument> positionalArgs,
            List<VariableDeclaration> localVariables,
            List<Expression> args) {
        ProgramBuilder builder = new ProgramBuilder();

        // builder.addVariable(new VariableDeclaration("a"));
        // builder.addStatement(new Assignment(new Reference("a"), value));

        builder.addFunction(new Function(funcName, body, positionalArgs, localVariables));

        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new Call(
                                            new Reference("function1"),
                                            args) // This call works, printing the value crashes!
                                })));

        builder.writeProgram(output);
    }

    /**
     * <p> Mini Python source code for tests from top to bottom :
     * <br>
     * <br> def function1():
     * <br>     return ""
     * <br>
     * <br> def function1():
     * <br>     return 123
     * <br>
     * <br> def function1():
     * <br>     return False
     * <br>
     * <br> def function1(param1):
     * <br>     return param1
     * <br>
     * <br> def function1():
     * <br>     return (True and True)
     * <br>
     * <br> print(function1())
     * </p>
     */
    private static Stream<Arguments> sources_equals() {
        return Stream.of(
                Arguments.of(
                        "function1",
                        List.of(new ReturnStatement(new StringLiteral(""))),
                        List.of(),
                        List.of(),
                        List.of(),
                        "\n"),
                Arguments.of(
                        "function1",
                        List.of(new ReturnStatement(new IntLiteral(123))),
                        List.of(),
                        List.of(),
                        List.of(),
                        "123\n"),
                Arguments.of(
                        "function1",
                        List.of(new ReturnStatement(new BoolLiteral(false))),
                        List.of(),
                        List.of(),
                        List.of(),
                        "False\n"),
                Arguments.of(
                        "function1",
                        List.of(new ReturnStatement(new Reference("param1"))),
                        List.of(new Argument("param1", 0)),
                        List.of(),
                        List.of(new BoolLiteral(true)),
                        "True\n"),
                Arguments.of(
                        "function1",
                        List.of(
                                new ReturnStatement(
                                        new AndKeyword(
                                                new BoolLiteral(true), new BoolLiteral(true)))),
                        List.of(),
                        List.of(),
                        List.of(),
                        "True\n"));
    }

    private static Stream<Arguments> sources_fails() {
        return Stream.of(
                Arguments.of(
                        "function1",
                        List.of(new ReturnStatement(new TupleLiteral(List.of()))),
                        List.of(),
                        List.of(),
                        List.of()),
                Arguments.of(
                        "function1",
                        List.of(
                                new ReturnStatement(
                                        new Call(
                                                new Reference("print"),
                                                List.of(new Expression[] {})))),
                        List.of(),
                        List.of(),
                        List.of()));
    }
}
