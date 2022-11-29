package CBuilder.manualTests;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.objects.*;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows to test the Builder without involving the ast or anything.
 *
 * TODO remove this when manual testing of this is not needed anymore
 */
public class BooleanTest {

    /**
     * Mini-Python code of program generated:
     * ```python
     * a
     * b
     *
     * a = b
     * d = -30
     *
     * type(a)
     * type(d)
     *
     * idA = id(a)
     * print(idA)
     *
     * print(id(a))
     * id(print(d))
     * id(type(a))
     *
     * e = id(type(print(d)))
     * e = print(-50)
     * ```
     */
    static void generateProgram(Path output) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addVariable(new VariableDeclaration("a"));
        builder.addStatement(new Assignment(new Reference("a"), new BoolLiteral(true)));

        builder.addVariable(new VariableDeclaration("b"));
        builder.addStatement(new Assignment(new Reference("b"), new BoolLiteral(false)));

        builder.addStatement(new Call(new Reference("print"), List.of(new Expression[] {
           new Reference("a"),
           new Reference("b"),
        })));
        builder.addStatement(new Call(new Reference("print"), List.of(new Expression[] {
            new Reference("a"),
            })));
        builder.addStatement(new Call(new Reference("print"), List.of(new Expression[] {
            new Reference("b"),
            })));
        builder.addStatement(new Call(new Reference("print"), List.of(new Expression[] {
            new Reference("a"),
            })));
        builder.addStatement(new Call(new Reference("print"), List.of(new Expression[] {
            new Reference("b"),
            })));

        builder.writeProgram(output);
    }

    public static void main(String[] args) {
        Path fileOutput = java.nio.file.FileSystems.getDefault().getPath("compilerOutput");
        generateProgram(fileOutput);
    }
}
