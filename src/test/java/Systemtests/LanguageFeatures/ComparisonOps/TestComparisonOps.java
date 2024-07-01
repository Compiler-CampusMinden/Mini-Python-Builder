/* (C)2024 */
package Systemtests.LanguageFeatures.ComparisonOps;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.Literal;
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

public class TestComparisonOps {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of("__eq__", new IntLiteral(1), new IntLiteral(1), "True\n"),
                Arguments.of("__eq__", new IntLiteral(1), new IntLiteral(0), "False\n"),
                Arguments.of("__ne__", new IntLiteral(1), new IntLiteral(1), "False\n"),
                Arguments.of("__ne__", new IntLiteral(1), new IntLiteral(0), "True\n"),
                Arguments.of("__ge__", new IntLiteral(1), new IntLiteral(2), "False\n"),
                Arguments.of("__ge__", new IntLiteral(2), new IntLiteral(1), "True\n"),
                Arguments.of("__gt__", new IntLiteral(1), new IntLiteral(2), "False\n"),
                Arguments.of("__gt__", new IntLiteral(2), new IntLiteral(1), "True\n"),
                Arguments.of("__le__", new IntLiteral(1), new IntLiteral(2), "True\n"),
                Arguments.of("__le__", new IntLiteral(2), new IntLiteral(1), "False\n"),
                Arguments.of("__lt__", new IntLiteral(1), new IntLiteral(2), "True\n"),
                Arguments.of("__lt__", new IntLiteral(2), new IntLiteral(1), "False\n"));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void comparison_operation(
            String operation, Literal a, Literal b, String expected, @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_comparison_operation(workDirectory, operation, a, b);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + "Operation : " + operation + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    /**
     * <p>Mini Python source code :
     * <br> a = 133
     * <br> print(a (==, !=, <, <=, >, >=) b) </p>
     */
    void generate_comparison_operation(Path output, String operation, Literal a, Literal b) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new Call(
                                            new AttributeReference(operation, a),
                                            List.of(new Expression[] {b}))
                                })));

        builder.writeProgram(output);
    }
}
