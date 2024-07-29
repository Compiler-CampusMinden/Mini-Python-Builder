/* (C)2024 */
package CBuilder.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import CBuilder.Expression;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestMPyClass {
    String testClass = "[TESTMPYCLASS]\n";

    @ParameterizedTest
    @MethodSource("sources_build_declaration")
    void build_declaration(
            String name,
            Reference parent,
            List<Function> functions,
            Map<Reference, Expression> classAttributes,
            String expected) {
        MPyClass mpyC = new MPyClass(name, parent, functions, classAttributes);

        System.out.println(testClass + mpyC.buildDeclaration());

        assertEquals(expected, mpyC.buildDeclaration());
    }

    @ParameterizedTest
    @MethodSource("sources_build_initialisation")
    void build_initialisation(
            String name,
            Reference parent,
            List<Function> functions,
            Map<Reference, Expression> classAttributes,
            String expected) {
        MPyClass mpyC = new MPyClass(name, parent, functions, classAttributes);

        System.out.println(testClass + mpyC.buildInitialisation());

        assertEquals(expected, mpyC.buildInitialisation());
    }

    @ParameterizedTest
    @MethodSource("sources_build_refdec")
    void build_refdec(
            String name,
            Reference parent,
            List<Function> functions,
            Map<Reference, Expression> classAttributes,
            String expected) {
        MPyClass mpyC = new MPyClass(name, parent, functions, classAttributes);

        System.out.println(testClass + mpyC.buildRefDec());

        assertEquals(expected, mpyC.buildRefDec());
    }

    @ParameterizedTest
    @MethodSource("sources_build_expression")
    void build_expression(
            String name,
            Reference parent,
            List<Function> functions,
            Map<Reference, Expression> classAttributes,
            String expected) {
        MPyClass mpyC = new MPyClass(name, parent, functions, classAttributes);

        System.out.println(testClass + mpyC.buildExpression());

        assertEquals(expected, mpyC.buildExpression());
    }

    @ParameterizedTest
    @MethodSource("sources_build_statement")
    void build_statement(
            String name,
            Reference parent,
            List<Function> functions,
            Map<Reference, Expression> classAttributes) {
        MPyClass mpyC = new MPyClass(name, parent, functions, classAttributes);

        System.out.println(testClass + mpyC.buildStatement());

        assertNull(mpyC.buildStatement());
    }

    private static Stream<Arguments> sources_build_declaration() {
        return Stream.of(
                Arguments.of(
                        "MyClass1",
                        new Reference("__MPyType_Object"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "__init__",
                                            List.of(new Statement[] {new SuperCall(List.of())}),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>(),
                        """
                                __MPyObj *MyClass1;
                                __MPyObj* func_MyClass1___init__(__MPyObj *args, __MPyObj *kwargs) {
                                \tassert(args != NULL && kwargs != NULL);
                                \t
                                \t__MPyGetArgsState argHelper = __mpy_args_init("__init__", args, kwargs, 1);
                                \t__MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
                                \t__mpy_args_finish(&argHelper);
                                \t
                                \t__MPyObj *retValue = NULL;
                                \t
                                \t__mpy_obj_ref_dec(__mpy_call(__mpy_super, __mpy_tuple_assign(0, self, __mpy_obj_init_tuple(1)), NULL));
                                \t
                                \t__mpy_obj_ref_dec(self);
                                \t
                                \tgoto ret;
                                \tret:
                                \tif (retValue == NULL) {
                                \t\tretValue = __mpy_obj_init_object();
                                \t}
                                \treturn __mpy_obj_return(retValue);
                                }
                                """),
                Arguments.of(
                        "MyClass2",
                        new Reference("MyClass1"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "__init__",
                                            List.of(new Statement[] {new SuperCall(List.of())}),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>(),
                        """
                                __MPyObj *MyClass2;
                                __MPyObj* func_MyClass2___init__(__MPyObj *args, __MPyObj *kwargs) {
                                \tassert(args != NULL && kwargs != NULL);
                                \t
                                \t__MPyGetArgsState argHelper = __mpy_args_init("__init__", args, kwargs, 1);
                                \t__MPyObj *self = __mpy_args_get_positional(&argHelper, 0, "self");
                                \t__mpy_args_finish(&argHelper);
                                \t
                                \t__MPyObj *retValue = NULL;
                                \t
                                \t__mpy_obj_ref_dec(__mpy_call(__mpy_super, __mpy_tuple_assign(0, self, __mpy_obj_init_tuple(1)), NULL));
                                \t
                                \t__mpy_obj_ref_dec(self);
                                \t
                                \tgoto ret;
                                \tret:
                                \tif (retValue == NULL) {
                                \t\tretValue = __mpy_obj_init_object();
                                \t}
                                \treturn __mpy_obj_return(retValue);
                                }
                                """));
    }

    private static Stream<Arguments> sources_build_initialisation() {
        return Stream.of(
                Arguments.of(
                        "MyClass1",
                        new Reference("__MPyType_Object"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "__init__",
                                            List.of(new Statement[] {new SuperCall(List.of())}),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>(),
                        """
                                MyClass1 = __mpy_obj_init_type("MyClass1", __MPyType_Object);
                                __mpy_obj_ref_inc(MyClass1);
                                {
                                \t__MPyObj *__init__;
                                \t__init__ = __mpy_obj_init_func(&func_MyClass1___init__);
                                \t__mpy_obj_ref_inc(__init__);
                                \t__mpy_obj_set_attr(MyClass1, "__init__", __init__);
                                \t__mpy_obj_ref_dec(__init__);
                                }
                                """),
                Arguments.of(
                        "MyClass2",
                        new Reference("MyClass1"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "__init__",
                                            List.of(new Statement[] {new SuperCall(List.of())}),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>(),
                        """
                                MyClass2 = __mpy_obj_init_type("MyClass2", MyClass1);
                                __mpy_obj_ref_inc(MyClass2);
                                {
                                \t__MPyObj *__init__;
                                \t__init__ = __mpy_obj_init_func(&func_MyClass2___init__);
                                \t__mpy_obj_ref_inc(__init__);
                                \t__mpy_obj_set_attr(MyClass2, "__init__", __init__);
                                \t__mpy_obj_ref_dec(__init__);
                                }
                                """));
    }

    private static Stream<Arguments> sources_build_refdec() {
        return Stream.of(
                Arguments.of(
                        "MyClass1",
                        new Reference("__MPyType_Object"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "__init__",
                                            List.of(new Statement[] {new SuperCall(List.of())}),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>(),
                        "__mpy_obj_ref_dec(MyClass1);\n"),
                Arguments.of(
                        "MyClass2",
                        new Reference("MyClass1"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "__init__",
                                            List.of(new Statement[] {new SuperCall(List.of())}),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>(),
                        "__mpy_obj_ref_dec(MyClass2);\n"));
    }

    private static Stream<Arguments> sources_build_expression() {
        return Stream.of(
                Arguments.of(
                        "MyClass1",
                        new Reference("__MPyType_Object"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "__init__",
                                            List.of(new Statement[] {new SuperCall(List.of())}),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>(),
                        "MyClass1"),
                Arguments.of(
                        "MyClass2",
                        new Reference("MyClass1"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "__init__",
                                            List.of(new Statement[] {new SuperCall(List.of())}),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>(),
                        "MyClass2"));
    }

    private static Stream<Arguments> sources_build_statement() {
        return Stream.of(
                Arguments.of(
                        "MyClass1",
                        new Reference("__MPyType_Object"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "__init__",
                                            List.of(new Statement[] {new SuperCall(List.of())}),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>(),
                        "__mpy_obj_ref_dec(MyClass1);\n"),
                Arguments.of(
                        "MyClass2",
                        new Reference("MyClass1"),
                        List.of(
                                new Function[] {
                                    new Function(
                                            "__init__",
                                            List.of(new Statement[] {new SuperCall(List.of())}),
                                            List.of(new Argument[] {new Argument("self", 0)}),
                                            List.of())
                                }),
                        new HashMap<>(),
                        "__mpy_obj_ref_dec(MyClass2);\n"));
    }
}
