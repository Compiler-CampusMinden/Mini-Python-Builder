/* (C)2024 */
package CBuilder.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestAttributeAssignment {
    String testClass = "[ATTRIBUTEASSIGNMENT]\n";

    private static Stream<Arguments> sources() {
        return Stream.of(
                Arguments.of(
                        new AttributeReference("referenceName1", new IntLiteral(23)),
                        new IntLiteral(34),
                        "__mpy_obj_set_attr(__mpy_obj_init_int(23), \"referenceName1\","
                                + " __mpy_obj_init_int(34));"),
                Arguments.of(
                        new AttributeReference("referenceName2", new BoolLiteral(false)),
                        new BoolLiteral(true),
                        "__mpy_obj_set_attr(__mpy_obj_init_boolean(false), \"referenceName2\","
                                + " __mpy_obj_init_boolean(true));"),
                Arguments.of(
                        new AttributeReference("referenceName3", new StringLiteral("stringL")),
                        new StringLiteral("34"),
                        "__mpy_obj_set_attr(__mpy_obj_init_str_static(\"stringL\"),"
                                + " \"referenceName3\", __mpy_obj_init_str_static(\"34\"));"));
    }

    @ParameterizedTest
    @MethodSource("sources")
    void build_statement(AttributeReference attributeR, Expression value, String expected) {
        AttributeAssignment attributeA = new AttributeAssignment(attributeR, value);

        System.out.println(testClass + attributeA.buildStatement());

        assertEquals(expected, attributeA.buildStatement());
    }
}
