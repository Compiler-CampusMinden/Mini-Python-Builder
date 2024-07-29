/* (C)2024 */
package CBuilder.conditions.conditionalStatement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import CBuilder.Expression;
import CBuilder.Statement;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestElifStatement {
    String testClass = "[ELIFSTATEMENT]\n";

    private static Stream<Arguments> sources_build() {
        return Stream.of(
                Arguments.of(
                        new BoolLiteral(true),
                        List.of(),
                        "else if"
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
                                else if (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(true), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
                                \t__mpy_obj_ref_dec(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1), "__add__"), __mpy_tuple_assign(0, __mpy_obj_init_int(3), __mpy_obj_init_tuple(1)), NULL));
                                }"""),
                Arguments.of(
                        new BoolLiteral(false),
                        List.of(
                                new Call(
                                        new AttributeReference("__add__", new IntLiteral(1)),
                                        List.of(new Expression[] {new IntLiteral(3)}))),
                        """
                                else if (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(false), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
                                \t__mpy_obj_ref_dec(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1), "__add__"), __mpy_tuple_assign(0, __mpy_obj_init_int(3), __mpy_obj_init_tuple(1)), NULL));
                                }"""));
    }

    @ParameterizedTest
    @MethodSource("sources_build")
    void build(Expression condition, List<Statement> statementList, String expected) {
        ElifStatement elifS = new ElifStatement(condition, statementList);

        System.out.println(testClass + elifS.build());

        assertEquals(expected, elifS.build());
    }

    @Test
    void build_statement() {
        ElifStatement elifS = new ElifStatement(new BoolLiteral(true), List.of());

        System.out.println(testClass + elifS.buildStatement());

        assertNull(elifS.buildStatement());
    }
}
