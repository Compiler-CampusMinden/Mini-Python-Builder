/* (C)2024 */
package CBuilder.objects.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestArgument {
    String testClass = "[ARGUMENT]\n";

    private static Stream<Arguments> sources_arg_extraction() {
        return Stream.of(
                Arguments.of(
                        "argName1",
                        1,
                        "__MPyObj *argName1 = __mpy_args_get_positional(&argHelper, 1,"
                                + " \"argName1\");\n"),
                Arguments.of(
                        "argName2",
                        2,
                        "__MPyObj *argName2 = __mpy_args_get_positional(&argHelper, 2,"
                                + " \"argName2\");\n"),
                Arguments.of(
                        "argName3",
                        3,
                        "__MPyObj *argName3 = __mpy_args_get_positional(&argHelper, 3,"
                                + " \"argName3\");\n"));
    }

    private static Stream<Arguments> sources_build_arg_cleanup() {
        return Stream.of(
                Arguments.of("argName1", 1, "__mpy_obj_ref_dec(argName1);\n"),
                Arguments.of("argName2", 2, "__mpy_obj_ref_dec(argName2);\n"),
                Arguments.of("argName3", 3, "__mpy_obj_ref_dec(argName3);\n"));
    }

    @ParameterizedTest
    @MethodSource("sources_arg_extraction")
    void build_arg_extraction(String name, int position, String expected) {
        Argument argument = new Argument(name, position);

        System.out.println(testClass + argument.buildArgExtraction());

        assertEquals(expected, argument.buildArgExtraction());
    }

    @ParameterizedTest
    @MethodSource("sources_build_arg_cleanup")
    void build_arg_cleanup(String name, int position, String expected) {
        Argument argument = new Argument(name, position);

        System.out.println(testClass + argument.buildArgCleanup());

        assertEquals(expected, argument.buildArgCleanup());
    }
}
