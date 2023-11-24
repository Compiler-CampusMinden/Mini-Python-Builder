package CBuilder;

import CBuilder.literals.IntLiteral;
import CBuilder.objects.*;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

/**
 * Allows to test the Builder without involving the ast or anything.
 *
 * TODO remove this when manual testing of this is not needed anymore
 */
public class ManualTest {

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
        builder.addVariable(new VariableDeclaration("b"));
        builder.addStatement(new Assignment(new Reference("a"), new Reference("b")));

        // does cause build errors with -Wall since the assignment is indeed quite useless
        // (and there's no good solution to ignore the specific error on assignment)
//        builder.addVariable(new VariableDeclaration("c"));
//        builder.addStatement(new Assignment(new VariableReference("c"), new VariableReference("c")));

        builder.addVariable(new VariableDeclaration("d"));
        builder.addStatement(new Assignment(new Reference("d"), new IntLiteral(-30)));

        builder.addStatement(new Call(new Reference("type"), List.of(new Expression[]{
                new Reference("a")
        })));
        builder.addStatement(new Call(new Reference("type"), List.of(new Expression[]{
                new Reference("d")
        })));

        builder.addVariable(new VariableDeclaration("idA"));
        builder.addStatement(new Assignment(new Reference("idA"),
                                            new Call(new Reference("id"),
                                                     List.of(new Expression[]{
                                                                     new Reference("a")
                                                             }))));
        builder.addStatement(new Call(new Reference("print"), List.of(new Expression[]{
                new Reference("idA")
        })));

//         call each built-in function in a nested context to make sure it correctly implements refCounts
        builder.addStatement(new Call(new Reference("print"), List.of(new Expression[]{
                new Call(new Reference("id"), List.of(new Expression[]{
                        new Reference("a")
                }))
        })));
        builder.addStatement(new Call(new Reference("id"), List.of(new Expression[]{
                new Call(new Reference("print"), List.of(new Expression[]{
                        new Reference("d")
                }))
        })));
        builder.addStatement(new Call(new Reference("id"), List.of(new Expression[]{
                new Call(new Reference("type"), List.of(new Expression[]{
                        new Reference("a")
                }))
        })));

        // assign a nested function call to a variable
        builder.addVariable(new VariableDeclaration("e"));
        builder.addStatement(new Call(new Reference("id"), List.of(new Expression[]{
                new Call(new Reference("type"), List.of(new Expression[]{
                        new Call(new Reference("print"), List.of(new Expression[]{
                                new Reference("d")
                        }))
                }))
        })));

        // call a function on a literal and assign that to a variable
        builder.addStatement(new Assignment(new Reference("e"),
                                            new Call(new Reference("print"),
                                                     List.of(new Expression[]{
                                                                     new IntLiteral(-50)
                                                             }))));

        builder.addStatement(new Call(new Reference("type"),
                                      List.of(new Expression[]{
                                                      new Reference("type")
                                              })));

        builder.addFunction(new Function("printA", List.of(new Statement[]{
                new Call(new Reference("print"),
                         List.of(new Expression[]{
                                 new Reference("a")
                         }))
        }), List.of(new Argument[]{
                new Argument("a", 0)
        }), List.of()));

        builder.addStatement(new Call(new Reference("printA"), List.of(new Expression[]{
                new Reference("idA")
        })));

        builder.addClass(new MPyClass("B", new Reference("__MPyType_Object"), List.of(new Function[]{
                new Function("print", List.of(new Statement[]{
                        new Call(new Reference("print"),
                                 List.of(new Expression[]{
                                         new Reference("self")
                                 }))
                }), List.of(new Argument[]{
                        new Argument("self", 0)
                }), List.of()),
                new Function("__init__", List.of(new Statement[] {
                    new SuperCall(List.of()),
                    new AttributeAssignment(new AttributeReference("b", new Reference("self")), new IntLiteral(100))
                }), List.of(new Argument[]{
                        new Argument("self", 0)
                }), List.of())
        }), new HashMap<>()));

        builder.addClass(new MPyClass("C", new Reference("B"), List.of(new Function[]{
               new Function("__init__", List.of(new Statement[] {
                       new SuperCall(List.of())
               }), List.of(new Argument[]{
                       new Argument("self", 0)
               }), List.of())
        }), new HashMap<>()));

        builder.addVariable(new VariableDeclaration("bObj"));
        builder.addStatement(new Assignment(new Reference("bObj"), new Call(new Reference("B"), List.of())));

        builder.addStatement(new Call(new AttributeReference("print", new Reference("bObj")), List.of()));

        // print(bObj.b)
        builder.addStatement(new Call(new Reference("print"), List.of(new Expression[]{
                new AttributeReference("b", new Reference("bObj"))
        })));

        builder.addVariable(new VariableDeclaration("cObj"));
        builder.addStatement(new Assignment(new Reference("cObj"), new Call(new Reference("C"), List.of())));

        builder.addStatement(new Call(new AttributeReference("print", new Reference("cObj")), List.of()));

        builder.addStatement(new Call(new Reference("print"), List.of(new Expression[]{
                new AttributeReference("b", new Reference("cObj"))
        })));

        builder.writeProgram(output);
    }

    public static void main(String[] args) {
        java.nio.file.Path fileOutput = java.nio.file.FileSystems.getDefault().getPath("build/compilerOutput/ManualTest/");
        generateProgram(fileOutput);
    }
}
