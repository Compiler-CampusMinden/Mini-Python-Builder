/* (C)2024 */
package Systemtests.LanguageFeatures.Classes;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import CBuilder.objects.*;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import CBuilder.objects.functions.ReturnStatement;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClassMembers {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    @Test
    void getter_setter(@TempDir Path workDirectory) throws IOException, InterruptedException {
        String result;

        generate_getter_and_setter(
                workDirectory,
                new MPyClass(
                        "ClassB",
                        new Reference("__MPyType_Object"),
                        List.of(
                                new Function(
                                        "__init__",
                                        List.of(
                                                new SuperCall(List.of()),
                                                new Call(
                                                        new Reference("print"),
                                                        List.of(
                                                                new Expression[] {
                                                                    new StringLiteral(
                                                                            "[ClassB] Print from"
                                                                                    + " __init__")
                                                                })),
                                                new AttributeAssignment(
                                                        new AttributeReference(
                                                                "x", new Reference("self")),
                                                        new Reference("val"))),
                                        List.of(new Argument("self", 0), new Argument("val", 1)),
                                        List.of()),
                                new Function(
                                        "getX",
                                        List.of(
                                                new ReturnStatement(
                                                        new AttributeReference(
                                                                "x", new Reference("self")))),
                                        List.of(new Argument("self", 0)),
                                        List.of()),
                                new Function(
                                        "setX",
                                        List.of(
                                                new AttributeAssignment(
                                                        new AttributeReference(
                                                                "x", new Reference("self")),
                                                        new Reference("newVal"))),
                                        List.of(new Argument("self", 0), new Argument("newVal", 1)),
                                        List.of())),
                        Map.of()),
                List.of(new IntLiteral(133)));

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals("[ClassB] Print from __init__\n23\n", result);
    }

    /**
     * <p> Mini Python source code for tests from top to bottom :
     * <br>
     * <br> class A(__MPyType_Object):
     * <br>
     * <br>     def __init__(self, val):
     * <br>         super()
     * <br>         print("[ClassB] Print from __init__\n")
     * <br>         self.x = val
     * <br>
     * <br>     def getX(self):
     * <br>         return self.x
     * <br>
     * <br>     def setX(self, newVal):
     * <br>         self.x = newVal
     * <br>
     * <br>
     * <br> x = A(123)
     * <br> x.setX(23)
     * <br> print(x.getX())
     */
    void generate_getter_and_setter(Path output, MPyClass mpyClass, List<Expression> initArgs) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addClass(mpyClass);

        builder.addVariable(new VariableDeclaration("x"));

        builder.addStatement(
                new Assignment(
                        new Reference("x"), new Call(new Reference(mpyClass.getName()), initArgs)));

        builder.addStatement(
                new Call(
                        new AttributeReference("setX", new Reference("x")),
                        List.of(new IntLiteral(23))));

        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new Call(
                                            new AttributeReference("getX", new Reference("x")),
                                            List.of())
                                })));

        builder.writeProgram(output);
    }

    @Test
    void getter(@TempDir Path workDirectory) throws IOException, InterruptedException {
        String result;

        generate_getter(
                workDirectory,
                new MPyClass(
                        "ClassA",
                        new Reference("__MPyType_Object"),
                        List.of(
                                new Function(
                                        "__init__",
                                        List.of(
                                                new SuperCall(List.of()),
                                                new Call(
                                                        new Reference("print"),
                                                        List.of(
                                                                new Expression[] {
                                                                    new StringLiteral(
                                                                            "[ClassA] Print from"
                                                                                    + " __init__")
                                                                })),
                                                new AttributeAssignment(
                                                        new AttributeReference(
                                                                "x", new Reference("self")),
                                                        new Reference("val"))),
                                        List.of(new Argument("self", 0), new Argument("val", 1)),
                                        List.of()),
                                new Function(
                                        "getX",
                                        List.of(
                                                new ReturnStatement(
                                                        new AttributeReference(
                                                                "x", new Reference("self")))),
                                        List.of(new Argument("self", 0)),
                                        List.of())),
                        Map.of()),
                List.of(new IntLiteral(133)));

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals("[ClassA] Print from __init__\n133\n", result);
    }

    void generate_getter(Path output, MPyClass mpyClass, List<Expression> initArgs) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addClass(mpyClass);

        builder.addVariable(new VariableDeclaration("x"));

        builder.addStatement(
                new Assignment(
                        new Reference("x"), new Call(new Reference(mpyClass.getName()), initArgs)));

        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new Call(
                                            new AttributeReference("getX", new Reference("x")),
                                            List.of())
                                })));

        builder.writeProgram(output);
    }
}
