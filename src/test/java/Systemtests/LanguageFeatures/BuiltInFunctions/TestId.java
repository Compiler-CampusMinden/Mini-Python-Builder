/* (C)2024 */
package Systemtests.LanguageFeatures.BuiltInFunctions;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Disabled
public class TestId {
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
    void id(Expression toId, String expected, @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_id(workDirectory, toId);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + "toId : " + toId + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    /**
     * Mini Python source code : <br>
     * print(id(133, True, "toPrint", 1+1))
     */
    void generate_id(Path output, Expression toId) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new Call(new Reference("id"), List.of(new Expression[] {toId}))
                                })));

        builder.writeProgram(output);
    }
}
