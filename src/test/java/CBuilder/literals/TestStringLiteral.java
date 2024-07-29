/* (C)2024 */
package CBuilder.literals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TestStringLiteral {
    String testClass = "[STRINGLITERAL]\n";

    private static Stream<String> sources() {
        return Stream.of("true", "false");
    }

    @ParameterizedTest
    @MethodSource("sources")
    void constructor(String name) {

        StringLiteral stringL = new StringLiteral(name);

        System.out.println(testClass + stringL.value);

        assertEquals(name, stringL.value);
    }

    @ParameterizedTest
    @MethodSource("sources")
    void build_expression(String name) {
        String expected = "__mpy_obj_init_str_static(\"" + name + "\")";

        StringLiteral stringL = new StringLiteral(name);

        System.out.println(testClass + stringL.buildExpression());

        assertEquals(expected, stringL.buildExpression());
    }

    @ParameterizedTest
    @MethodSource("sources")
    void build_statement(String name) {
        String expected = "__mpy_obj_ref_dec__mpy_obj_init_str_static(\"" + name + "\"));\n";

        StringLiteral stringL = new StringLiteral(name);

        System.out.println(testClass + stringL.buildStatement());

        assertEquals(expected, stringL.buildStatement());
    }
}
