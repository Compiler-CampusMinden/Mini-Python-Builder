/* (C)2024 */
package Systemtests.LanguageFeatures.BuiltInFunctions;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.literals.IntLiteral;
import CBuilder.objects.Call;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TestMetamorphicExamplePrint {

    @Test
    void call_print(@TempDir Path workDirectory) throws IOException, InterruptedException {
        String result;
        String expected = "133\n";

        generate_call_print(workDirectory);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(result);

        // Thread.sleep(5000);
        assertEquals(expected, result);

        // After Test
    }

    @Test
    void print_through_function(@TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;
        String expected = "133\n";

        generate_print_through_function(workDirectory);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(result);

        // Thread.sleep(5000);
        assertEquals(expected, result);

        // After Test
    }

    /**
     * Mini Python source code : <br>
     * a = 133 <br>
     * print(a)
     */
    static void generate_call_print(Path output) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addVariable(new VariableDeclaration("a"));
        builder.addStatement(new Assignment(new Reference("a"), new IntLiteral(133)));

        builder.addStatement(
                new Call(new Reference("print"), List.of(new Expression[] {new Reference("a")})));

        builder.writeProgram(output);
    }

    /**
     * Mini Python source code : <br>
     * a = 133 <br>
     * <br>
     * def printA(a): <br>
     * print(a) <br>
     * <br>
     * printA(a)
     */
    static void generate_print_through_function(Path output) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addVariable(new VariableDeclaration("a"));
        builder.addStatement(new Assignment(new Reference("a"), new IntLiteral(133)));

        builder.addFunction(
                new Function(
                        "printA",
                        List.of(
                                new Statement[] {
                                    new Call(
                                            new Reference("print"),
                                            List.of(new Expression[] {new Reference("a")}))
                                }),
                        List.of(new Argument[] {new Argument("a", 0)}),
                        List.of()));

        builder.addStatement(
                new Call(new Reference("printA"), List.of(new Expression[] {new Reference("a")})));

        builder.writeProgram(output);
    }
}
