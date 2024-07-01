/* (C)2024 */
package CBuilder.literals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBoolLiteral {
    String testClass = "[BOOLLITERAL]\n";

    private static Stream<Boolean> sources() {
        return Stream.of(true, false);
    }

    @ParameterizedTest
    @MethodSource("sources")
    void constructor(boolean b_value) {

        BoolLiteral boolL = new BoolLiteral(b_value);

        System.out.println(testClass + boolL.value);

        assertEquals(b_value, boolL.value);
    }

    @ParameterizedTest
    @MethodSource("sources")
    void build_expression(boolean b_value) {
        String expected = "__mpy_obj_init_boolean(" + b_value + ")";

        BoolLiteral boolL = new BoolLiteral(b_value);

        System.out.println(testClass + boolL.buildExpression());

        assertEquals(expected, boolL.buildExpression());
    }

    @ParameterizedTest
    @MethodSource("sources")
    void build_statement(boolean b_value) {
        String expected = "__mpy_obj_init_boolean(" + b_value + ");\n";

        BoolLiteral boolL = new BoolLiteral(b_value);

        System.out.println(testClass + boolL.buildStatement());

        assertEquals(expected, boolL.buildStatement());
    }
}
