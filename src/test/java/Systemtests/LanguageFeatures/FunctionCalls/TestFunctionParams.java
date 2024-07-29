/* (C)2024 */
package Systemtests.LanguageFeatures.FunctionCalls;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import CBuilder.objects.functions.ReturnStatement;
import CBuilder.variables.VariableDeclaration;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestFunctionParams {
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
    void function_throws(
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

        // result = getProgramOutput(workDirectory);
        assertThrows(RuntimeException.class, () -> getProgramOutput(workDirectory));

        System.out.println(testClass + " Result from throws : " + result);
        // Thread.sleep(5000);

    }

    /**
     * Mini Python source code for tests from top to bottom : <br>
     * <br>
     * def function1(): <br>
     * return "" <br>
     * <br>
     * def function1(param1): <br>
     * return param1 <br>
     * <br>
     * def function1(param1, param2): <br>
     * return param1 + param2 <br>
     * Calls : <br>
     * <br>
     * print(function1()) <br>
     * print(function1(123)) <br>
     * print(function1(123,123)) <br>
     * <br>
     * Functions will Throw runtime error if more or less parameters are passed. <br>
     * Also if a print called is passed as parameter and something is done that cant be done with a
     * function pointer.
     */
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
                        List.of(new Expression[] {new Call(new Reference("function1"), args)})));

        builder.writeProgram(output);
    }

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
                        List.of(new ReturnStatement(new Reference("param1"))),
                        List.of(new Argument("param1", 0)),
                        List.of(),
                        List.of(new IntLiteral(0)),
                        "0\n"),
                Arguments.of(
                        "function1",
                        List.of(new ReturnStatement(new Reference("param1"))),
                        List.of(new Argument("param1", 0)),
                        List.of(),
                        List.of(new StringLiteral("String")),
                        "String\n"),
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
                                        new Call(
                                                new AttributeReference(
                                                        "__add__", new Reference("param1")),
                                                List.of(
                                                        new Expression[] {
                                                            new Reference("param2")
                                                        })))),
                        List.of(new Argument("param1", 0), new Argument("param2", 1)),
                        List.of(),
                        List.of(new IntLiteral(1), new IntLiteral(2)),
                        "3\n"));
    }

    private static Stream<Arguments> sources_fails() {
        return Stream.of(
                Arguments.of(
                        "function1",
                        List.of(new ReturnStatement(new StringLiteral(""))),
                        List.of(),
                        List.of(),
                        List.of(new IntLiteral(133))),
                Arguments.of(
                        "function1",
                        List.of(new ReturnStatement(new Reference("param1"))),
                        List.of(new Argument("param1", 0)),
                        List.of(),
                        List.of()),
                Arguments.of(
                        "function1",
                        List.of(new ReturnStatement(new Reference("param1"))),
                        List.of(new Argument("param1", 0)),
                        List.of(),
                        List.of(new StringLiteral("String1"), new StringLiteral("String2"))),
                Arguments.of(
                        "function1",
                        List.of(new ReturnStatement(new Reference("param1"))),
                        List.of(new Argument("param1", 0)),
                        List.of(),
                        List.of(new Call(new Reference("print"), List.of(new Expression[] {})))),
                Arguments.of(
                        "function1",
                        List.of(
                                new ReturnStatement(
                                        new Call(
                                                new AttributeReference(
                                                        "__add__", new Reference("param1")),
                                                List.of(
                                                        new Expression[] {
                                                            new Reference("param2")
                                                        })))),
                        List.of(new Argument("param1", 0), new Argument("param2", 1)),
                        List.of(),
                        List.of(new IntLiteral(1))));
    }
}
