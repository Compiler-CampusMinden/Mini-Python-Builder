/* (C)2024 */
package Systemtests.LanguageFeatures.FunctionCalls;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.conditions.IfThenElseStatement;
import CBuilder.conditions.conditionalStatement.IfStatement;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import CBuilder.objects.functions.ReturnStatement;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;
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

public class TestFunctionBody {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    /**
     * <p> Mini Python source code for tests from top to bottom :
     * <br>
     * <br> def function1():
     * <br>     return ""
     * <br>
     * <br> def function1():
     * <br>     print("Print from function body")
     * <br>
     * <br> def function1():
     * <br>     y = 133
     * <br>     return y
     * <br>
     * <br> def function1(x):
     * <br>     return x
     * <br>
     * <br> def function1(x, y):
     * <br>     return x + y
     * <br>
     * <br> def function1(x, y):
     * <br>     if(x >= 0):
     * <br>         return x + y
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
                        List.of(
                                new Call(
                                        new Reference("print"),
                                        List.of(
                                                new Expression[] {
                                                    new StringLiteral("Print from function body")
                                                })),
                                new ReturnStatement(new StringLiteral(""))),
                        List.of(),
                        List.of(),
                        List.of(),
                        "Print from function body\n\n"),
                Arguments.of(
                        "function1",
                        List.of(
                                new Assignment(new Reference("y"), new IntLiteral(133)),
                                new ReturnStatement(new Reference("y"))),
                        List.of(),
                        List.of(new VariableDeclaration("y")),
                        List.of(),
                        "133\n"),
                Arguments.of(
                        "function1",
                        List.of(new ReturnStatement(new Reference("x"))),
                        List.of(new Argument("x", 0)),
                        List.of(),
                        List.of(new IntLiteral(133)),
                        "133\n"),
                Arguments.of(
                        "function1",
                        List.of(
                                new ReturnStatement(
                                        new Call(
                                                new AttributeReference(
                                                        "__add__", new Reference("x")),
                                                List.of(new Expression[] {new Reference("y")})))),
                        List.of(new Argument("x", 0), new Argument("y", 1)),
                        List.of(),
                        List.of(new IntLiteral(1), new IntLiteral(3)),
                        "4\n"),
                Arguments.of(
                        "function1",
                        List.of(
                                new IfThenElseStatement(
                                        new IfStatement(
                                                new Call(
                                                        new AttributeReference(
                                                                "__ge__", new Reference("x")),
                                                        List.of(
                                                                new Expression[] {
                                                                    new IntLiteral(0)
                                                                })),
                                                List.of(
                                                        new ReturnStatement(
                                                                new Call(
                                                                        new AttributeReference(
                                                                                "__add__",
                                                                                new Reference("x")),
                                                                        List.of(
                                                                                new Expression[] {
                                                                                    new Reference(
                                                                                            "y")
                                                                                }))))),
                                        Optional.empty(),
                                        Optional.empty())),
                        List.of(new Argument("x", 0), new Argument("y", 1)),
                        List.of(),
                        List.of(new IntLiteral(1), new IntLiteral(3)),
                        "4\n"));
    }

    @ParameterizedTest
    @MethodSource("sources_equals")
    void function(
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
}
