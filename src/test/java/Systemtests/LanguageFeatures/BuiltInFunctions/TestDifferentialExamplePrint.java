/* (C)2024 */
package Systemtests.LanguageFeatures.BuiltInFunctions;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.literals.IntLiteral;
import CBuilder.objects.Call;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDifferentialExamplePrint {

    @Test
    void call_print(@TempDir Path workDirectory) throws IOException, InterruptedException {
        String result;

        generate_call_print(workDirectory);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(result);

        ProcessBuilder builder =
                new ProcessBuilder(
                        "bash", "-c", "printf 'a=133\nprint(a)' >> test.py ; python3 test.py");
        builder.directory(workDirectory.toFile());
        // builder.inheritIO();

        Process p = builder.start();

        StringBuilder python3Output = new StringBuilder();
        String line; // https://github.com/actions/runner-images

        InputStream processStdOutput = p.getInputStream();
        Reader r = new InputStreamReader(processStdOutput);
        BufferedReader br = new BufferedReader(r);

        while ((line = br.readLine()) != null) {
            python3Output.append(line).append("\n");
        }

        if (p.waitFor() != 0) {
            System.out.println("Could not create file test.py or run python3 test.py !");
            throw new RuntimeException("Could not create file test.py !");
        }

        System.out.println(python3Output);

        // Thread.sleep(5000);
        assertEquals(python3Output.toString(), result);

        // After Test
    }

    /**
     * <p>Mini Python source code :
     * <br> a = 133
     * <br> print(a) </p>
     */
    static void generate_call_print(Path output) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addVariable(new VariableDeclaration("a"));
        builder.addStatement(new Assignment(new Reference("a"), new IntLiteral(133)));

        builder.addStatement(
                new Call(new Reference("print"), List.of(new Expression[] {new Reference("a")})));

        builder.writeProgram(output);
    }
}
