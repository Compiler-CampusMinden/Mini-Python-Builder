/* (C)2024 */
package Systemtests;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.literals.IntLiteral;
import CBuilder.objects.*;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TestLanguageFeaturesCombined {
    @Test
    void program(@TempDir Path workDirectory) throws IOException, InterruptedException {
        String result;
        String expected =
                """
                        62
                        62
                        -30
                        -30
                        -50
                        62
                        <object object 381
                        100
                        <object object 494
                        100
                        """;

        generateProgram(workDirectory);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(result);

        // Thread.sleep(5000);
        assertEquals(expected, result);

        // After Test
    }

    /**
     * Mini-Python code of program generated: ```python a b
     *
     * <p>a = b d = -30
     *
     * <p>type(a) type(d)
     *
     * <p>idA = id(a) print(idA)
     *
     * <p>print(id(a)) id(print(d)) id(type(a))
     *
     * <p>e = id(type(print(d))) e = print(-50)
     *
     * <p>type(type)
     *
     * <p>def printA(a): print(a) printA(idA)
     *
     * <p>class B: def print(self): print(self)
     *
     * <p>def __init__(self): self.b = 100
     *
     * <p>class C(B): def __init__(self):
     *
     * <p>bObj = B() bObj.print()
     *
     * <p>print(bObj.b)
     *
     * <p>cObj = C() cOBJ.print()
     *
     * <p>print(cObj.b) ```
     */
    static void generateProgram(Path output) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addVariable(new VariableDeclaration("a")); // a
        builder.addVariable(new VariableDeclaration("b")); // b
        builder.addStatement(new Assignment(new Reference("a"), new Reference("b"))); // a = b

        // does cause build errors with -Wall
        // builder.addVariable(new VariableDeclaration("c"));
        // builder.addStatement(new Assignment(new VariableReference("c"), new
        // VariableReference("c")));

        builder.addVariable(new VariableDeclaration("d"));
        builder.addStatement(new Assignment(new Reference("d"), new IntLiteral(-30))); // d = -30

        builder.addStatement(
                new Call(
                        new Reference("type"),
                        List.of(new Expression[] {new Reference("a")}))); // type(a)
        builder.addStatement(
                new Call(
                        new Reference("type"),
                        List.of(new Expression[] {new Reference("d")}))); // type(d)

        builder.addVariable(new VariableDeclaration("idA"));
        builder.addStatement(
                new Assignment(
                        new Reference("idA"),
                        new Call(
                                new Reference("id"),
                                List.of(new Expression[] {new Reference("a")})))); // idA = id(a)
        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(new Expression[] {new Reference("idA")}))); // print(idA)

        // call each built-in function in a nested context to make sure it correctly
        // implements refCounts
        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new Call(
                                            new Reference("id"),
                                            List.of(new Expression[] {new Reference("a")}))
                                }))); // print(id(a))
        builder.addStatement(
                new Call(
                        new Reference("id"),
                        List.of(
                                new Expression[] {
                                    new Call(
                                            new Reference("print"),
                                            List.of(new Expression[] {new Reference("d")}))
                                }))); // id(print(d))
        builder.addStatement(
                new Call(
                        new Reference("id"),
                        List.of(
                                new Expression[] {
                                    new Call(
                                            new Reference("type"),
                                            List.of(new Expression[] {new Reference("a")}))
                                }))); // id(type(a))

        // assign a nested function call to a variable
        builder.addVariable(new VariableDeclaration("e")); // e
        builder.addStatement(
                new Call(
                        new Reference("id"),
                        List.of(
                                new Expression[] {
                                    new Call(
                                            new Reference("type"),
                                            List.of(
                                                    new Expression[] {
                                                        new Call(
                                                                new Reference("print"),
                                                                List.of(
                                                                        new Expression[] {
                                                                            new Reference("d")
                                                                        }))
                                                    }))
                                }))); // e = id(type(print(d)))

        // call a function on a literal and assign that to a variable
        builder.addStatement(
                new Assignment(
                        new Reference("e"),
                        new Call(
                                new Reference("print"),
                                List.of(
                                        new Expression[] {
                                            new IntLiteral(-50)
                                        })))); // e = print(-50)

        builder.addStatement(
                new Call(
                        new Reference("type"),
                        List.of(new Expression[] {new Reference("type")}))); // type(type)

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
                        List.of())); // def printA(a):
        //      print(a)

        builder.addStatement(
                new Call(
                        new Reference("printA"),
                        List.of(new Expression[] {new Reference("idA")}))); // printA(idA)

        builder.addClass(
                new MPyClass(
                        "ClassB",
                        new Reference("__MPyType_Object"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "print",
                                            List.of(
                                                    new Statement[] {
                                                        new Call(
                                                                new Reference("print"),
                                                                List.of(
                                                                        new Expression[] {
                                                                            new Reference("self")
                                                                        }))
                                                    }),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of()),
                                    new Function(
                                            "__init__",
                                            List.of(
                                                    new Statement[] {
                                                        new SuperCall(List.of()),
                                                        new AttributeAssignment(
                                                                new AttributeReference(
                                                                        "b", new Reference("self")),
                                                                new IntLiteral(100))
                                                    }),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>()));

        builder.addClass(
                new MPyClass(
                        "C",
                        new Reference("ClassB"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "__init__",
                                            List.of(new Statement[] {new SuperCall(List.of())}),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>()));

        builder.addVariable(new VariableDeclaration("bObj"));
        builder.addStatement(
                new Assignment(
                        new Reference("bObj"), new Call(new Reference("ClassB"), List.of())));

        builder.addStatement(
                new Call(new AttributeReference("print", new Reference("bObj")), List.of()));

        // print(bObj.b)
        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new AttributeReference("b", new Reference("bObj"))
                                })));

        builder.addVariable(new VariableDeclaration("cObj"));
        builder.addStatement(
                new Assignment(new Reference("cObj"), new Call(new Reference("C"), List.of())));

        builder.addStatement(
                new Call(new AttributeReference("print", new Reference("cObj")), List.of()));

        builder.addStatement(
                new Call(
                        new Reference("print"),
                        List.of(
                                new Expression[] {
                                    new AttributeReference("b", new Reference("cObj"))
                                })));

        builder.writeProgram(output);
    }
}
