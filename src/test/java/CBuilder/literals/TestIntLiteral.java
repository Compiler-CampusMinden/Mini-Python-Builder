/* (C)2024 */
package CBuilder.literals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestIntLiteral {
    String testClass = "[INTLITERAL]\n";

    private static Stream<Integer> sources() {
        return Stream.of(Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    @ParameterizedTest
    @MethodSource("sources")
    void constructor(Integer i_value) {
        long expected = i_value;

        IntLiteral intL = new IntLiteral(i_value);

        System.out.println(testClass + intL.value);

        assertEquals(expected, intL.value);
    }

    @ParameterizedTest
    @MethodSource("sources")
    void build_expression(Integer i_value) {
        String expected = "__mpy_obj_init_int(" + i_value + ")";

        IntLiteral intL = new IntLiteral(i_value);

        System.out.println(testClass + intL.buildExpression());

        assertEquals(expected, intL.buildExpression());
    }

    @ParameterizedTest
    @MethodSource("sources")
    void build_statement(Integer i_value) {
        String expected = "__mpy_obj_init_int(" + i_value + ");\n";

        IntLiteral intL = new IntLiteral(i_value);

        System.out.println(testClass + intL.buildStatement());

        assertEquals(expected, intL.buildStatement());
    }
}
