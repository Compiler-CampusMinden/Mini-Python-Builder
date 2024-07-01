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
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;
import org.junit.jupiter.api.Disabled;
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

@Disabled
public class TestType {
    // TODO print(type(a)) seems not to be working see TODO(./src/function-args.c:36)
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of(new IntLiteral(133), "61\n"),
                Arguments.of(new BoolLiteral(true), "61\n"),
                Arguments.of(new StringLiteral("toPrint"), "61\n"),
                Arguments.of(
                        new Call(
                                new AttributeReference("__add__", new IntLiteral(1)),
                                List.of(new Expression[] {new IntLiteral(1)})),
                        "102\n"));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void type(Literal value, String expected, @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_type(workDirectory, value);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + "toType : " + value + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    /**
     * <p>Mini Python source code :
     * <br> a = (133, True, "toPrint", 1+1)
     * <br> print(type(a))
     * </p>
     */
    void generate_type(Path output, Expression value) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addVariable(new VariableDeclaration("a"));
        builder.addStatement(new Assignment(new Reference("a"), value));

        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new Call(
                                            new Reference("type"),
                                            List.of(new Expression[] {new Reference("a")}))
                                })));

        builder.writeProgram(output);
    }
}
