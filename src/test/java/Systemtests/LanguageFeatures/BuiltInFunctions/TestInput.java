/* (C)2024 */
package Systemtests.LanguageFeatures.BuiltInFunctions;

import static Systemtests.TestHelpers.getProgramOutputBasedOnInput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.objects.Call;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Disabled
public class TestInput {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of("61\n", "61\n"),
                Arguments.of("True\n", "True\n"),
                Arguments.of("!!\n", "!!\n"),
                Arguments.of("\n", "\n"));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void input(String input, String expected, @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result = "";

        generate_input(workDirectory);

        makeProgram(workDirectory);

        result = getProgramOutputBasedOnInput(workDirectory, result, input);

        System.out.println(testClass + "input : " + input + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    @Test
    void input_timeout(@TempDir Path workDirectory) throws IOException, InterruptedException {
        String input = "";
        String result = "";

        generate_input(workDirectory);

        makeProgram(workDirectory);

        System.out.println(testClass + "input : " + input + " Result : " + result);

        assertTimeoutPreemptively(
                Duration.ofSeconds(5),
                () -> getProgramOutputBasedOnInput(workDirectory, result, input));
    }

    void generate_input(Path output) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new Call(new Reference("input"), List.of(new Expression[] {}))
                                })));

        builder.writeProgram(output);
    }
}
