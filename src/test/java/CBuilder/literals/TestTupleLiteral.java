/* (C)2024 */
package CBuilder.literals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Snapshot 23.02.2024 print(tupleLiteral) does not work will look into */
public class TestTupleLiteral {
    String testClass = "[TUPLELITERAL]\n";

    private static Stream<Arguments> sources_expression() {
        return Stream.of(
                Arguments.of(
                        List.of(new IntLiteral(1)),
                        "__mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_obj_init_tuple(1))"),
                Arguments.of(
                        List.of(new IntLiteral(1), new IntLiteral(2), new IntLiteral(3)),
                        "__mpy_tuple_assign(0, __mpy_obj_init_int(1), __mpy_tuple_assign(1,"
                            + " __mpy_obj_init_int(2), __mpy_tuple_assign(2, __mpy_obj_init_int(3),"
                            + " __mpy_obj_init_tuple(3))))"),
                Arguments.of(
                        List.of(new StringLiteral("Test1"), new StringLiteral("Test2")),
                        "__mpy_tuple_assign(0, __mpy_obj_init_str_static(\"Test1\"),"
                                + " __mpy_tuple_assign(1, __mpy_obj_init_str_static(\"Test2\"),"
                                + " __mpy_obj_init_tuple(2)))"));
    }

    private static Stream<Arguments> sources_statement() {
        return Stream.of(
                Arguments.of(
                        List.of(new IntLiteral(1)),
                        "__mpy_obj_ref_dec(__mpy_tuple_assign(0, __mpy_obj_init_int(1),"
                                + " __mpy_obj_init_tuple(1)));\n"),
                Arguments.of(
                        List.of(new IntLiteral(1), new IntLiteral(2), new IntLiteral(3)),
                        "__mpy_obj_ref_dec(__mpy_tuple_assign(0, __mpy_obj_init_int(1),"
                            + " __mpy_tuple_assign(1, __mpy_obj_init_int(2), __mpy_tuple_assign(2,"
                            + " __mpy_obj_init_int(3), __mpy_obj_init_tuple(3)))));\n"),
                Arguments.of(
                        List.of(new StringLiteral("Test1"), new StringLiteral("Test2")),
                        "__mpy_obj_ref_dec(__mpy_tuple_assign(0,"
                                + " __mpy_obj_init_str_static(\"Test1\"), __mpy_tuple_assign(1,"
                                + " __mpy_obj_init_str_static(\"Test2\"),"
                                + " __mpy_obj_init_tuple(2))));\n"));
    }

    @ParameterizedTest
    @MethodSource("sources_expression")
    void build_expression(List<Expression> expressionList, String expected) {

        TupleLiteral tupleL = new TupleLiteral(expressionList);

        System.out.println(testClass + tupleL.buildExpression());

        assertEquals(expected, tupleL.buildExpression());
    }

    @ParameterizedTest
    @MethodSource("sources_statement")
    void build_statement(List<Expression> expressionList, String expected) {
        TupleLiteral tupleL = new TupleLiteral(expressionList);

        System.out.println(testClass + tupleL.buildStatement());

        assertEquals(expected, tupleL.buildStatement());
    }
}
