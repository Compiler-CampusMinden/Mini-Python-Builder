/* (C)2024 */
package CBuilder.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestSuperCall {
    String testClass = "[SUPERCALL]\n";

    private static Stream<Arguments> sources_expression() {
        return Stream.of(
                Arguments.of(
                        List.of(new Expression[] {new IntLiteral(23)}),
                        "__mpy_call(__mpy_super, __mpy_tuple_assign(0, self, __mpy_tuple_assign(1,"
                                + " __mpy_obj_init_int(23), __mpy_obj_init_tuple(2))), NULL)"),
                Arguments.of(
                        List.of(new Expression[] {new BoolLiteral(true)}),
                        "__mpy_call(__mpy_super, __mpy_tuple_assign(0, self, __mpy_tuple_assign(1,"
                            + " __mpy_obj_init_boolean(true), __mpy_obj_init_tuple(2))), NULL)"));
    }

    private static Stream<Arguments> sources_statement() {
        return Stream.of(
                Arguments.of(
                        List.of(new Expression[] {new IntLiteral(23)}),
                        "__mpy_obj_ref_dec(__mpy_call(__mpy_super, __mpy_tuple_assign(0, self,"
                                + " __mpy_tuple_assign(1, __mpy_obj_init_int(23),"
                                + " __mpy_obj_init_tuple(2))), NULL));\n"),
                Arguments.of(
                        List.of(new Expression[] {new BoolLiteral(true)}),
                        "__mpy_obj_ref_dec(__mpy_call(__mpy_super, __mpy_tuple_assign(0, self,"
                                + " __mpy_tuple_assign(1, __mpy_obj_init_boolean(true),"
                                + " __mpy_obj_init_tuple(2))), NULL));\n"));
    }

    @ParameterizedTest
    @MethodSource("sources_expression")
    void build_expression(List<Expression> args, String expected) {
        SuperCall superC = new SuperCall(args);

        System.out.println(testClass + superC.buildExpression());

        assertEquals(expected, superC.buildExpression());
    }

    @ParameterizedTest
    @MethodSource("sources_statement")
    void build_statement(List<Expression> args, String expected) {
        SuperCall superC = new SuperCall(args);

        System.out.println(testClass + superC.buildStatement());

        assertEquals(expected, superC.buildStatement());
    }
}
