/* (C)2024 */
package CBuilder.objects.functions;

import CBuilder.Expression;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestReturnStatement {
    String testClass = "[RETURNSTATEMENT]\n";

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of(
                        new IntLiteral(34),
                        """
                                retValue = __mpy_obj_init_int(34);
                                goto ret;
                                """),
                Arguments.of(
                        new BoolLiteral(true),
                        """
                                retValue = __mpy_obj_init_boolean(true);
                                goto ret;
                                """),
                Arguments.of(
                        new StringLiteral("34"),
                        """
                                retValue = __mpy_obj_init_str_static("34");
                                goto ret;
                                """));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void build_statement(Expression returnValue, String expected) {
        ReturnStatement returnS = new ReturnStatement(returnValue);

        System.out.println(testClass + returnS.buildStatement());

        assertEquals(expected, returnS.buildStatement());
    }
}
