package CBuilder.manualTests;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.literals.StringLiteral;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
import CBuilder.objects.MPyClass;
import CBuilder.objects.SuperCall;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class ClassTest {
    static void generateProgram(Path output) {
        ProgramBuilder builder = new ProgramBuilder();
        VariableDeclaration varBDecl = new VariableDeclaration("b");
        Reference varB = new Reference("b");
        builder.addVariable(varBDecl);

        Reference funcPrint = new Reference("print");
        Reference object = new Reference("__MPyType_Object");

        MPyClass clazz = new MPyClass("A", object, List.of(new Function[]{
            new Function("foo", "foo1", List.of(new Expression[]{
                new Call(funcPrint, List.of(new Expression[]{
                    new StringLiteral("foo")
                }))
            }), List.of(new Argument[]{new Argument("self", 0)}), List.of()),
            new Function("__init__", "__init1_", List.of(new Expression[]{
                new SuperCall(List.of())
            }), List.of(new Argument[]{new Argument("self", 0)}), List.of())
        }), Map.of());
        builder.addClass(clazz);
        builder.addStatement(new Assignment(varB, new Call(new Reference("A"), List.of())));
        builder.addStatement(new Call(new AttributeReference("foo", varB), List.of()));


        builder.writeProgram(output);
    }

    public static void main(String[] args) {
        Path fileOutput = java.nio.file.FileSystems.getDefault().getPath("compilerOutput");
        generateProgram(fileOutput);
    }
}
