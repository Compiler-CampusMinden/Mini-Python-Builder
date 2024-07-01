/* (C)2024 */
package CBuilder.bool;

import CBuilder.keywords.bool.NotKeyword;
import CBuilder.literals.BoolLiteral;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Parameterized tests not really useful here.
 * Snapshot 22.02.2024
 */
public class TestNotKeyword {
    String testClass = "[NOTKEYWORD]\n";

    private static Stream<Boolean> sources() {
        return Stream.of(true, false);
    }

    /**
     * @param b_value - Boolean
     */
    @ParameterizedTest
    @MethodSource("sources")
    void build_expression(boolean b_value) {
        String expected =
                "__mpy_obj_init_boolean(!__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean("
                        + b_value
                        + "), \"__bool__\"), __mpy_obj_init_tuple(0), NULL)))";

        NotKeyword notK = new NotKeyword(new BoolLiteral(b_value));

        System.out.println(testClass + notK.buildExpression());

        assertEquals(expected, notK.buildExpression());
    }

    /**
     * @param b_value - Boolean
     */
    @ParameterizedTest
    @MethodSource("sources")
    void build_statement(Boolean b_value) {
        String expected =
                "__mpy_obj_ref_dec(__mpy_obj_init_boolean(!__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean("
                        + b_value
                        + "), \"__bool__\"), __mpy_obj_init_tuple(0), NULL))));\n";

        NotKeyword notK = new NotKeyword(new BoolLiteral(b_value));

        System.out.println(testClass + notK.buildStatement());

        assertEquals(expected, notK.buildStatement());
    }
}
