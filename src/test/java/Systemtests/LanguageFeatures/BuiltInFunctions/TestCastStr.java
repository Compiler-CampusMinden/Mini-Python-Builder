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

public class TestCastStr {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of(new BoolLiteral(true), "True\n"),
                Arguments.of(new BoolLiteral(false), "False\n"),
                Arguments.of(new StringLiteral("testString"), "testString\n"),
                Arguments.of(new IntLiteral(0), "0\n"),
                Arguments.of(new IntLiteral(-23), "-23\n"),
                Arguments.of(new IntLiteral(23), "23\n"));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void cast_str(Literal value, String expected, @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_cast_str(workDirectory, value);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + "Value : " + value + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    /**
     * <p>Mini Python source code :
     * <br> print(str( True, False, 23, 0, -23, "testString")) </p>
     */
    void generate_cast_str(Path output, Literal value) {
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
                                            new AttributeReference("__str__", value),
                                            List.of(new Expression[] {}))
                                })));

        builder.writeProgram(output);
    }
}
