/* (C)2024 */
package Systemtests.LanguageFeatures.BuiltInFunctions;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.Literal;
import CBuilder.literals.StringLiteral;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCastInt {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    @ParameterizedTest
    @MethodSource("sources_equals")
    void cast_int_equals(Literal value, String expected, @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_cast_int(workDirectory, value);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + "Value : " + value + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("sources_throws")
    void cast_int_throws(Literal value, @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result = "";

        generate_cast_int(workDirectory, value);

        makeProgram(workDirectory);

        assertThrows(RuntimeException.class, () -> getProgramOutput(workDirectory));

        System.out.println(testClass + "Value : " + value + " Result : " + result);

        // Thread.sleep(5000);
    }

    /**
     * <p>Mini Python source code :
     * <br> print(int( True, False, "23", "0", "-23", 23[error], "false"[error])) </p>
     */
    void generate_cast_int(Path output, Literal value) {
        ProgramBuilder builder = new ProgramBuilder();

        // builder.addVariable(new VariableDeclaration("a"));
        // builder.addStatement(new Assignment(new Reference("a"), value));

        // new Assignment(new Reference("a"), new Call(new AttributeReference("__int__", new
        // Reference("a")), List.of(new Expression[]{})));

        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new Call(
                                            new AttributeReference("__int__", value),
                                            List.of(new Expression[] {}))
                                })));

        builder.writeProgram(output);
    }

    private static Stream<Arguments> sources_equals() {
        return Stream.of(
                Arguments.of(new BoolLiteral(true), "1\n"),
                Arguments.of(new BoolLiteral(false), "0\n"),
                Arguments.of(new StringLiteral("23"), "23\n"),
                Arguments.of(new StringLiteral("0"), "0\n"),
                Arguments.of(new StringLiteral("-23"), "-23\n"));
    }

    private static Stream<Arguments> sources_throws() {
        return Stream.of(
                Arguments.of(new IntLiteral(23)), Arguments.of(new StringLiteral("false")));
    }
}
