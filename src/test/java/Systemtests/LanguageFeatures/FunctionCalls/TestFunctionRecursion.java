/* (C)2024 */
package Systemtests.LanguageFeatures.FunctionCalls;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.conditions.IfThenElseStatement;
import CBuilder.conditions.conditionalStatement.IfStatement;
import CBuilder.literals.IntLiteral;
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

public class TestFunctionRecursion {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> sources_equals() {
        return Stream.of(
                Arguments.of(
                        "function1",
                        List.of(
                                new IfThenElseStatement(
                                        new IfStatement(
                                                new Call(
                                                        new AttributeReference(
                                                                "__gt__", new Reference("x")),
                                                        List.of(
                                                                new Expression[] {
                                                                    new IntLiteral(0)
                                                                })),
                                                List.of(
                                                        new Assignment(
                                                                new Reference("x"),
                                                                new Call(
                                                                        new AttributeReference(
                                                                                "__sub__",
                                                                                new Reference("x")),
                                                                        List.of(
                                                                                new Expression[] {
                                                                                    new IntLiteral(
                                                                                            1)
                                                                                }))),
                                                        new Call(
                                                                new Reference("print"),
                                                                List.of(
                                                                        new Expression[] {
                                                                            new Reference("x")
                                                                        })),
                                                        new ReturnStatement(
                                                                new Call(
                                                                        new Reference("function1"),
                                                                        List.of(
                                                                                new Expression[] {
                                                                                    new Reference(
                                                                                            "x")
                                                                                }))))),
                                        Optional.empty(),
                                        Optional.empty()),
                                new ReturnStatement(new Reference("x"))),
                        List.of(new Argument("x", 0)),
                        List.of(),
                        List.of(new IntLiteral(5)),
                        "4\n3\n2\n1\n0\n"));
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

    /**
     * <p> Mini Python source code :
     * <br>
     * <br> def function1(x):
     * <br>     if(x > 0):
     * <br>         x = x - 1
     * <br>         print(x)
     * <br>         return function1(x)
     * <br>
     * <br> function1(5)
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

        builder.addStatement(new Call(new Reference("function1"), args));

        builder.writeProgram(output);
    }
}
