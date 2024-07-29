/* (C)2024 */
package CBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TestReference {
    String testClass = "[REFERENCE]\n";

    private static Stream<String> sources() {
        return Stream.of("ref1", "ref2");
    }

    @ParameterizedTest
    @MethodSource("sources")
    void constructor(String name) {
        Reference ref = new Reference(name);

        System.out.println(testClass + ref.getName());

        assertEquals(name, ref.getName());
    }

    @ParameterizedTest
    @MethodSource("sources")
    void build_expression(String name) {
        Reference ref = new Reference(name);

        System.out.println(testClass + ref.buildExpression());

        assertEquals(name, ref.buildExpression());
    }

    @ParameterizedTest
    @MethodSource("sources")
    void build_statement(String name) {
        Reference ref = new Reference(name);

        System.out.println(testClass + ref.buildStatement());

        assertEquals(name + ";\n", ref.buildStatement());
    }
}
