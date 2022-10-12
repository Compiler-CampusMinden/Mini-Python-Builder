package CBuilder.manualTests;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;

import java.nio.file.Path;
import java.util.List;

public class StrOps {
    static void generateProgram(Path output) {
        ProgramBuilder builder = new ProgramBuilder();

        VariableDeclaration varADecl = new VariableDeclaration("a");
        Reference varA = new Reference("a");
        Reference funcPrint = new Reference("print");

        builder.addVariable(varADecl);
        builder.addStatement(new Assignment(varA, new StringLiteral("abc")));

        builder.addStatement(new Call(funcPrint, List.of(new Expression[]{
            new Call(new AttributeReference("__eq__", varA), List.of(new Expression[]{
                new StringLiteral("abc"),
                }), null)
        }), null));

        builder.writeProgram(output);
    }

    public static void main(String[] args) {
        Path fileOutput = java.nio.file.FileSystems.getDefault().getPath("compilerOutput");
        generateProgram(fileOutput);
    }
}
