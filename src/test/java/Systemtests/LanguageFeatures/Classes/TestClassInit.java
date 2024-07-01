/* (C)2024 */
package Systemtests.LanguageFeatures.Classes;

import CBuilder.Expression;
import CBuilder.ProgramBuilder;
import CBuilder.Reference;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import CBuilder.objects.Call;
import CBuilder.objects.MPyClass;
import CBuilder.objects.SuperCall;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static Systemtests.TestHelpers.getProgramOutput;
import static Systemtests.TestHelpers.makeProgram;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClassInit {
    String testClass = '[' + this.getClass().getSimpleName().toUpperCase() + "]\n";

    private static Stream<Arguments> sources_equals() {
        return Stream.of(
                Arguments.of(
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
                                                                }))),
                                        List.of(new Argument("self", 0)),
                                        List.of())),
                        Map.of(),
                        List.of(),
                        "[ClassA] Print from __init__\n"),
                Arguments.of(
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
                                                                                    + " __init__"),
                                                                    new Reference("x")
                                                                }))),
                                        List.of(new Argument("self", 0), new Argument("x", 1)),
                                        List.of())),
                        Map.of(),
                        List.of(new StringLiteral("test")),
                        "[ClassB] Print from __init__ test\n"),
                Arguments.of(
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
                                                                                + " __init__ Param1"
                                                                                + " :"),
                                                                    new Reference("x"),
                                                                    new StringLiteral(" Param2 : "),
                                                                    new Reference("y")
                                                                }))),
                                        List.of(
                                                new Argument("self", 0),
                                                new Argument("x", 1),
                                                new Argument("y", 2)),
                                        List.of())),
                        Map.of(),
                        List.of(new StringLiteral("test"), new IntLiteral(123)),
                        "[ClassB] Print from __init__ Param1 : test  Param2 :  123\n"));
    }

    @ParameterizedTest
    @MethodSource("sources_equals")
    void class_definition(
            String className,
            Reference parent,
            List<Function> functions,
            Map<Reference, Expression> classAttributes,
            List<Expression> initArgs,
            String expected,
            @TempDir Path workDirectory)
            throws IOException, InterruptedException {
        String result;

        generate_class(workDirectory, className, parent, functions, classAttributes, initArgs);

        makeProgram(workDirectory);

        result = getProgramOutput(workDirectory);

        System.out.println(testClass + " Result : " + result);

        // Thread.sleep(5000);
        assertEquals(expected, result);
    }

    /**
     * <p> Mini Python source code for tests from top to bottom :
     * <br>
     * <br> class ClassA(__MPyType_Object):
     * <br>
     * <br>     def __init__(self):
     * <br>         print("[ClassA] Print from __init__\n")
     * <br>
     * <br> class ClassB(__MPyType_Object):
     * <br>     def __init__(self, x):
     * <br>         print("[ClassB] Print from __init__" + str(param1))
     * <br>
     * <br> class ClassB(__MPyTypeObject):
     * <br>     def __init__(self, x, y):
     * <br>         print("[ClassB] Print from __init__ Param1 : " + str(x) + " Param2 : " + str(y))
     * <br>
     * <br> x = className(( , param1, (param1,param2)))
     */
    void generate_class(
            Path output,
            String className,
            Reference parent,
            List<Function> functions,
            Map<Reference, Expression> classAttributes,
            List<Expression> initArgs) {
        ProgramBuilder builder = new ProgramBuilder();

        builder.addClass(new MPyClass(className, parent, functions, classAttributes));

        builder.addVariable(new VariableDeclaration("x"));

        builder.addStatement(
                new Assignment(new Reference("x"), new Call(new Reference(className), initArgs)));

        builder.writeProgram(output);
    }
}
