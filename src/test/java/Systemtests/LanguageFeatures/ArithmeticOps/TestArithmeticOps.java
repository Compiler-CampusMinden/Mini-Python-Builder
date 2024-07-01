/* (C)2024 */
package Systemtests.LanguageFeatures.ArithmeticOps;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.literals.IntLiteral;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
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

public class TestArithmeticOps {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of("__add__", 1, 2, "3\n"),
                Arguments.of("__add__", Integer.MAX_VALUE, 0, Integer.MAX_VALUE + "\n"),
                Arguments.of("__add__", Integer.MIN_VALUE, 0, Integer.MIN_VALUE + "\n"),
                Arguments.of("__sub__", 2, 1, "1\n"),
                Arguments.of("__sub__", Integer.MAX_VALUE, 0, Integer.MAX_VALUE + "\n"),
                Arguments.of("__sub__", Integer.MIN_VALUE, 0, Integer.MIN_VALUE + "\n"),
                Arguments.of("__mul__", 2, 2, "4\n"),
                Arguments.of("__mul__", Integer.MAX_VALUE, 0, "0\n"),
                Arguments.of("__mul__", Integer.MIN_VALUE, 0, "0\n"),
                Arguments.of("__div__", 5, 2, "2\n"),
                Arguments.of("__div__", 0, Integer.MAX_VALUE, "0\n"),
                Arguments.of("__div__", 0, Integer.MIN_VALUE, "0\n"));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void arithmetic_operation(
            String operation, Integer a, Integer b, String expected, @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_arithmetic_operation(workDirectory, operation, a, b);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + "Operation : " + operation + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    /**
     * <p>Mini Python source code :
     * <br> print(a (+,-,*,/) b) </p>
     * @param a Summand
     * @param b Summand
     */
    void generate_arithmetic_operation(Path output, String operation, Integer a, Integer b) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new Call(
                                            new AttributeReference(operation, new IntLiteral(a)),
                                            List.of(new Expression[] {new IntLiteral(b)}))
                                })));

        builder.writeProgram(output);
    }
}
