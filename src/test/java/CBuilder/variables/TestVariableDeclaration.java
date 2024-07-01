/* (C)2024 */
package CBuilder.variables;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Snapshot 22.02.2024
 */
public class TestVariableDeclaration {
    private static Stream<String> sources() {
        return Stream.of("someVar", "anotherOne", "", "'");
    }

    /**
     * <p>var_name # just init</p>
     * @param var_name - variable name
     */
    @ParameterizedTest
    @MethodSource("sources")
    void build_initialisation_parameterized(String var_name) {
        String expected =
                var_name
                        + " = __mpy_obj_init_object();\n"
                        + "__mpy_obj_ref_inc("
                        + var_name
                        + ");\n";

        VariableDeclaration var = new VariableDeclaration(var_name);

        String actual = var.buildInitialisation();

        System.out.println(actual);

        assertEquals(expected, actual);
    }

    /**
     * <p>var_name # with init</p>
     * @param var_name - variable name
     */
    @ParameterizedTest
    @MethodSource("sources")
    void build_true_parameterized(String var_name) {
        String expected =
                "__MPyObj *"
                        + var_name
                        + " = __mpy_obj_init_object();\n"
                        + "__mpy_obj_ref_inc("
                        + var_name
                        + ");\n";

        VariableDeclaration var = new VariableDeclaration(var_name);

        String actual = var.build(true);

        System.out.println(actual);

        assertEquals(expected, actual);
    }

    /**
     * <p>var_name # without init</p>
     * @param var_name - variable name
     */
    @ParameterizedTest
    @MethodSource("sources")
    void build_false_parameterized(String var_name) {
        String expected = "__MPyObj *" + var_name + ";\n";

        VariableDeclaration var = new VariableDeclaration(var_name);

        String actual = var.build(false);

        System.out.println(actual);

        assertEquals(expected, actual);
    }

    /**
     * <p>Test decrementing reference counter of the variable</p>
     * @param var_name - variable name
     */
    @ParameterizedTest
    @MethodSource("sources")
    void build_ref_dec_parameterized(String var_name) {
        String expected = "__mpy_obj_ref_dec(" + var_name + ");\n";

        VariableDeclaration var = new VariableDeclaration(var_name);

        String actual = var.buildRefDec();

        System.out.println(actual);

        assertEquals(expected, actual);
    }
}
