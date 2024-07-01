/* (C)2024 */
package Systemtests.LanguageFeatures.DeclarationAndAssignment;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.Literal;
import CBuilder.literals.StringLiteral;
import CBuilder.objects.Call;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;
import org.junit.jupiter.api.Test;
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

public class TestDeclarationAndAssignment {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> source_literals() {
        return Stream.of(
                Arguments.of(new IntLiteral(133), "133\n"),
                Arguments.of(new StringLiteral("stringVal"), "stringVal\n"),
                Arguments.of(new BoolLiteral(true), "True\n"));
    }

    @Test
    void declaration_and_assignment(@TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;
        String expected =
                """
                133
                133
                133
                133
                """;

        generate_declaration_and_assignment(workDirectory);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);

        // After Test
    }

    @ParameterizedTest
    @MethodSource("source_literals")
    void assignment_literal(Literal literal, String expected, @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_assignment_literal(workDirectory, literal);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);

        // After Test
    }

    @Test
    void assignment_reference(@TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;
        String expected = "133\n";

        generate_assignment_reference(workDirectory);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);

        // After Test
    }

    @Test
    void assignment_function_call(@TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;
        String expected = "133\n";

        generate_assignment_function_call(workDirectory);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);

        // After Test
    }

    @Test
    void assignment_function(@TempDir Path workDirectory) throws IOException, InterruptedException {
        String result;
        String expected = "133\n";

        generate_assignment_function_call(workDirectory);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);

        // After Test
    }

    /**
     * Mini-Python code of program generated :
     * a
     * a = 133
     * <p>
     * b
     * b = a
     * <p>
     * c = print(a)
     * <p>
     * <p>
     * d
     * def printA(a):
     * print(a)
     * d = printA
     * <p>
     * print(a)
     * print(b)
     * c
     * d(a)
     */
    static void generate_declaration_and_assignment(Path output) {
        ProgramBuilder builder = new ProgramBuilder();
        builder.addVariable(new VariableDeclaration("a"));
        builder.addStatement(new Assignment(new Reference("a"), new IntLiteral(133))); // a = 133

        builder.addVariable(new VariableDeclaration("b")); // b
        builder.addStatement(new Assignment(new Reference("b"), new Reference("a"))); // b = a

        builder.addVariable(new VariableDeclaration("c"));
        builder.addStatement(
                new Assignment(
                        new Reference("c"),
                        new Call(
                                new Reference("print"),
                                List.of(new Expression[] {new Reference("a")})))); // c = print(a)

        builder.addVariable(new VariableDeclaration("d"));
        builder.addFunction(
                new Function(
                        "printA",
                        List.of(
                                new Statement[] {
                                    new Call(
                                            new Reference("print"),
                                            List.of(new Expression[] {new Reference("a")}))
                                }),
                        List.of(new Argument[] {new Argument("a", 0)}),
                        List.of())); // d def printA(a): printa(a)

        builder.addStatement(
                new Assignment(new Reference("d"), new Reference("printA"))); // d = printA

        builder.addStatement(
                new Call(new Reference("print"), List.of(new Expression[] {new Reference("a")})));

        builder.addStatement(
                new Call(new Reference("print"), List.of(new Expression[] {new Reference("b")})));

        builder.addStatement(
                new Call(
                        new Reference("d"),
                        List.of(new Expression[] {new Reference("a")}))); // d(a)

        builder.writeProgram(output);
    }

    /**
     * <p>Mini Python source code :
     * <br> a = 133
     * <br> print(a) </p>
     * @param output writeProgram to here
     */
    static void generate_assignment_literal(Path output, Literal literal) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addVariable(new VariableDeclaration("a"));
        builder.addStatement(new Assignment(new Reference("a"), literal));

        builder.addStatement(
                new Call(new Reference("print"), List.of(new Expression[] {new Reference("a")})));

        builder.writeProgram(output);
    }

    /**
     * <p>Mini python source code :
     * <br> a = 133
     * <br> b = a
     * <br> print(b) </p>
     * @param output writeProgram to here
     */
    static void generate_assignment_reference(Path output) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addVariable(new VariableDeclaration("a"));
        builder.addStatement(new Assignment(new Reference("a"), new IntLiteral(133)));

        builder.addVariable(new VariableDeclaration("b"));
        builder.addStatement(new Assignment(new Reference("b"), new Reference("a")));

        builder.addStatement(
                new Call(new Reference("print"), List.of(new Expression[] {new Reference("b")})));

        builder.writeProgram(output);
    }

    /**
     * <p>Mini python source code :
     * <br> a = 133
     * <br> b
     * <br> def printA(a):
     * <br>     print(a)
     * <br> b = printA
     * <br> b(a)</p>
     *
     * @param output writeProgram to here
     */
    static void generate_assignment_function_call(Path output) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addVariable(new VariableDeclaration("a"));
        builder.addStatement(new Assignment(new Reference("a"), new IntLiteral(133)));

        builder.addVariable(new VariableDeclaration("b"));
        builder.addFunction(
                new Function(
                        "printA",
                        List.of(
                                new Statement[] {
                                    new Call(
                                            new Reference("print"),
                                            List.of(new Expression[] {new Reference("a")}))
                                }),
                        List.of(new Argument[] {new Argument("a", 0)}),
                        List.of()));

        builder.addStatement(new Assignment(new Reference("b"), new Reference("printA")));
        builder.addStatement(
                new Call(new Reference("b"), List.of(new Expression[] {new Reference("a")})));

        builder.writeProgram(output);
    }
}
