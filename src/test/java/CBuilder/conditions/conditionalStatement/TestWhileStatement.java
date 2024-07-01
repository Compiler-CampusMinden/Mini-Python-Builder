/* (C)2024 */
package CBuilder.conditions.conditionalStatement;

import CBuilder.Expression;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
import CBuilder.variables.Assignment;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWhileStatement {
    String testClass = "[WHILESTATEMENT]\n";

    private static Stream<Arguments> sources_build() {
        return Stream.of(
                Arguments.of(
                        new BoolLiteral(true),
                        List.of(),
                        "while"
                            + " (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(true),"
                            + " \"__bool__\"), __mpy_obj_init_tuple(0), NULL))) {\n"
                            + "}"),
                Arguments.of(
                        new BoolLiteral(true),
                        List.of(
                                new Call(
                                        new AttributeReference("__add__", new IntLiteral(1)),
                                        List.of(new Expression[] {new IntLiteral(3)}))),
                        """
                                while (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(true), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
                                \t__mpy_obj_ref_dec(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1), "__add__"), __mpy_tuple_assign(0, __mpy_obj_init_int(3), __mpy_obj_init_tuple(1)), NULL));
                                }"""),
                Arguments.of(
                        new BoolLiteral(false),
                        List.of(
                                new Call(
                                        new AttributeReference("__add__", new IntLiteral(1)),
                                        List.of(new Expression[] {new IntLiteral(3)}))),
                        """
                                while (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(false), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
                                \t__mpy_obj_ref_dec(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1), "__add__"), __mpy_tuple_assign(0, __mpy_obj_init_int(3), __mpy_obj_init_tuple(1)), NULL));
                                }"""),
                Arguments.of(
                        new Call(
                                new AttributeReference("__gt__", new Reference("a")),
                                List.of(new Expression[] {new IntLiteral(1)})),
                        List.of(
                                new Call(
                                        new Reference("print"),
                                        List.of(new Expression[] {new Reference("a")})),
                                new Assignment(
                                        new Reference("a"),
                                        new Call(
                                                new AttributeReference(
                                                        "__sub__", new Reference("a")),
                                                List.of(new Expression[] {new IntLiteral(1)})))),
                        """
                                while (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_call(__mpy_obj_get_attr(a, "__gt__"), __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1)), NULL), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
                                \t__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0, a, __mpy_obj_init_tuple(1)), NULL));
                                \t__mpy_obj_ref_dec(a);
                                a = __mpy_call(__mpy_obj_get_attr(a, "__sub__"), __mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1)), NULL);
                                __mpy_obj_ref_inc(a);
                                }"""));
    }

    @ParameterizedTest
    @MethodSource("sources_build")
    void build(Expression condition, List<Statement> statementList, String expected) {
        WhileStatement whileS = new WhileStatement(condition, statementList);

        System.out.println(testClass + whileS.build());

        assertEquals(expected, whileS.build());
    }

    @ParameterizedTest
    @MethodSource("sources_build")
    void build_statement(Expression condition, List<Statement> statementList, String expected) {
        WhileStatement whileS = new WhileStatement(condition, statementList);

        System.out.println(testClass + whileS.buildStatement());

        assertEquals(expected, whileS.buildStatement());
    }
}
