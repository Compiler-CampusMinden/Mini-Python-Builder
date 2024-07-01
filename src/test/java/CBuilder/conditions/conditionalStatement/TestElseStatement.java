/* (C)2024 */
package CBuilder.conditions.conditionalStatement;

import CBuilder.Expression;
import CBuilder.Statement;
import CBuilder.literals.IntLiteral;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestElseStatement {
    String testClass = "[ELSESTATEMENT]\n";

    private static Stream<Arguments> sources_build() {
        return Stream.of(
                Arguments.of(List.of(), "else {\n" + "}"),
                Arguments.of(
                        List.of(
                                new Call(
                                        new AttributeReference("__add__", new IntLiteral(1)),
                                        List.of(new Expression[] {new IntLiteral(3)}))),
                        """
                                else {
                                \t__mpy_obj_ref_dec(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1), "__add__"), __mpy_tuple_assign(0, __mpy_obj_init_int(3), __mpy_obj_init_tuple(1)), NULL));
                                }"""));
    }

    @ParameterizedTest
    @MethodSource("sources_build")
    void build(List<Statement> statementList, String expected) {
        ElseStatement elseS = new ElseStatement(statementList);

        System.out.println(testClass + elseS.build());

        assertEquals(expected, elseS.build());
    }

    @Test
    void build_statement() {
        ElseStatement elseS = new ElseStatement(List.of());

        System.out.println(testClass + elseS.buildStatement());

        assertNull(elseS.buildStatement());
    }
}
