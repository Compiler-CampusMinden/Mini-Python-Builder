/* (C)2024 */
package Systemtests.LanguageFeatures.LogicalOps;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.keywords.bool.AndKeyword;
import CBuilder.keywords.bool.NotKeyword;
import CBuilder.keywords.bool.OrKeyword;
import CBuilder.literals.BoolLiteral;
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

public class TestLogicalOps {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of(
                        new AndKeyword(new BoolLiteral(true), new BoolLiteral(true)), "True\n"),
                Arguments.of(
                        new AndKeyword(new BoolLiteral(true), new BoolLiteral(false)), "False\n"),
                Arguments.of(
                        new AndKeyword(new BoolLiteral(false), new BoolLiteral(false)), "False\n"),
                Arguments.of(
                        new AndKeyword(new BoolLiteral(false), new BoolLiteral(true)), "False\n"),
                Arguments.of(new OrKeyword(new BoolLiteral(true), new BoolLiteral(true)), "True\n"),
                Arguments.of(
                        new OrKeyword(new BoolLiteral(true), new BoolLiteral(false)), "True\n"),
                Arguments.of(
                        new OrKeyword(new BoolLiteral(false), new BoolLiteral(false)), "False\n"),
                Arguments.of(
                        new OrKeyword(new BoolLiteral(false), new BoolLiteral(true)), "True\n"),
                Arguments.of(new NotKeyword(new BoolLiteral(true)), "False\n"),
                Arguments.of(new NotKeyword(new BoolLiteral(false)), "True\n"));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void logical_operation(Expression operation, String expected, @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_logical_operation(workDirectory, operation);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + "Operation : " + operation + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    /**
     * <p>Mini Python source code :
     * <br> print(a &&, ||, not b) </p>
     * @param output writeProgram to here
     *
     * @param operation logical operation to test for
     */
    void generate_logical_operation(Path output, Expression operation) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addStatement(
                new Call(new Reference("print"), List.of(new Expression[] {operation})));

        builder.writeProgram(output);
        System.out.println(builder.buildProgram());
    }
}
