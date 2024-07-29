/* (C)2024 */
package CBuilder.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import CBuilder.Reference;
import CBuilder.literals.IntLiteral;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestCall {
    String testClass = "[CALL]\n";

    private static Stream<Arguments> sources_build_expression() {
        return Stream.of(
                Arguments.of(
                        new Reference("print"),
                        List.of(new Expression[] {new IntLiteral(23)}),
                        "__mpy_call(print, __mpy_tuple_assign(0, __mpy_obj_init_int(23),"
                                + " __mpy_obj_init_tuple(1)), NULL)"),
                Arguments.of(
                        new Reference("type"),
                        List.of(new Expression[] {new IntLiteral(23)}),
                        "__mpy_call(type, __mpy_tuple_assign(0, __mpy_obj_init_int(23),"
                                + " __mpy_obj_init_tuple(1)), NULL)"));
    }

    private static Stream<Arguments> sources_build_statement() {
        return Stream.of(
                Arguments.of(
                        new Reference("print"),
                        List.of(new Expression[] {new IntLiteral(23)}),
                        "__mpy_obj_ref_dec(__mpy_call(print, __mpy_tuple_assign(0,"
                                + " __mpy_obj_init_int(23), __mpy_obj_init_tuple(1)), NULL));\n"),
                Arguments.of(
                        new Reference("type"),
                        List.of(new Expression[] {new IntLiteral(23)}),
                        "__mpy_obj_ref_dec(__mpy_call(type, __mpy_tuple_assign(0,"
                                + " __mpy_obj_init_int(23), __mpy_obj_init_tuple(1)), NULL));\n"));
    }

    @ParameterizedTest
    @MethodSource("sources_build_expression")
    void build_expression(Expression callable, List<Expression> args, String expected) {
        Call call = new Call(callable, args);

        System.out.println(testClass + call.buildExpression());

        assertEquals(expected, call.buildExpression());
    }

    @ParameterizedTest
    @MethodSource("sources_build_statement")
    void build_statement(Expression callable, List<Expression> args, String expected) {
        Call call = new Call(callable, args);

        System.out.println(testClass + call.buildStatement());

        assertEquals(expected, call.buildStatement());
    }
}
