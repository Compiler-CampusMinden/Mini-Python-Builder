package CBuilder;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Path;

import static CBuilder.ManualTest.generateProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled
public class TestWithCToolchains {

    private void generateAndCompileProgram(Path outputDirectory) throws IOException, InterruptedException {
        generateProgram(outputDirectory);

        ProcessBuilder builder = new ProcessBuilder("make");
        builder.directory(outputDirectory.toFile());
        builder.environment().put("DEBUG", "1");
        builder.inheritIO();

        Process p = builder.start();
        if (p.waitFor() != 0) {
            throw new RuntimeException("Failed to compile program!");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = { "gcc", "clang" })
    void compile(String compiler, @TempDir Path outputDirectory) throws IOException, InterruptedException {
        generateProgram(outputDirectory);

        ProcessBuilder builder = new ProcessBuilder("make");
        builder.environment().put("DEBUG", "1");
        builder.environment().put("CC", compiler);
        builder.directory(outputDirectory.toFile());
        builder.inheritIO();

        Process p = builder.start();

        assertEquals(0, p.waitFor());
    }

    @Test
    void valgrind(@TempDir Path workDirectory) throws IOException, InterruptedException {
        generateAndCompileProgram(workDirectory);

        ProcessBuilder builder = new ProcessBuilder("valgrind", "--show-error-list=yes", "--leak-check=full", "--show-leak-kinds=all", "--error-exitcode=1", "./bin/program");
        builder.directory(workDirectory.toFile());
        builder.inheritIO();

        Process p = builder.start();

        assertEquals(0, p.waitFor());
    }

    @Test
    void clang_tidy(@TempDir Path workDirectory) throws IOException, InterruptedException {
        generateProgram(workDirectory);

        // this does not error on warnings, but those are too broad anyway
        ProcessBuilder builder = new ProcessBuilder("make",  "lint");
        builder.directory(workDirectory.toFile());
        builder.inheritIO();

        Process p = builder.start();

        assertEquals(0, p.waitFor());
    }
}
