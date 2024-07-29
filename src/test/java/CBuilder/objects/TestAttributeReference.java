/* (C)2024 */
package CBuilder.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestAttributeReference {
    String testClass = "[ATTRIBUTEREFERENCE]\n";

    private static Stream<Arguments> sources_get_name() {
        return Stream.of(
                Arguments.of("referenceName", new IntLiteral(23), "referenceName"),
                Arguments.of("referenceName1", new BoolLiteral(false), "referenceName1"),
                Arguments.of("referenceName2", new StringLiteral("stringL"), "referenceName2"));
    }

    private static Stream<Arguments> sources_build_expression() {
        return Stream.of(
                Arguments.of(
                        "referenceName",
                        new IntLiteral(23),
                        "__mpy_obj_get_attr(__mpy_obj_init_int(23), \"referenceName\")"),
                Arguments.of(
                        "referenceName1",
                        new BoolLiteral(true),
                        "__mpy_obj_get_attr(__mpy_obj_init_boolean(true), \"referenceName1\")"),
                Arguments.of(
                        "referenceName2",
                        new StringLiteral("stringL"),
                        "__mpy_obj_get_attr(__mpy_obj_init_str_static(\"stringL\"),"
                                + " \"referenceName2\")"));
    }

    private static Stream<Arguments> sources_build_statement() {
        return Stream.of(
                Arguments.of(
                        "referenceName",
                        new IntLiteral(23),
                        "__mpy_obj_get_attr(__mpy_obj_init_int(23), \"referenceName\");\n"),
                Arguments.of(
                        "referenceName1",
                        new BoolLiteral(true),
                        "__mpy_obj_get_attr(__mpy_obj_init_boolean(true), \"referenceName1\");\n"),
                Arguments.of(
                        "referenceName2",
                        new StringLiteral("stringL"),
                        "__mpy_obj_get_attr(__mpy_obj_init_str_static(\"stringL\"),"
                                + " \"referenceName2\");\n"));
    }

    private static Stream<Arguments> sources_build_object() {
        return Stream.of(
                Arguments.of("referenceName", new IntLiteral(23), "__mpy_obj_init_int(23)"),
                Arguments.of(
                        "referenceName1", new BoolLiteral(true), "__mpy_obj_init_boolean(true)"),
                Arguments.of(
                        "referenceName2",
                        new StringLiteral("stringL"),
                        "__mpy_obj_init_str_static(\"stringL\")"));
    }

    @ParameterizedTest
    @MethodSource("sources_get_name")
    void get_name(String name, Expression expression, String expected) {
        AttributeReference attributeR = new AttributeReference(name, expression);

        System.out.println(testClass + attributeR.getName());

        assertEquals(expected, attributeR.getName());
    }

    @ParameterizedTest
    @MethodSource("sources_build_expression")
    void build_expression(String name, Expression expression, String expected) {
        AttributeReference attributeR = new AttributeReference(name, expression);

        System.out.println(testClass + attributeR.buildExpression());

        assertEquals(expected, attributeR.buildExpression());
    }

    @ParameterizedTest
    @MethodSource("sources_build_statement")
    void build_statement(String name, Expression expression, String expected) {
        AttributeReference attributeR = new AttributeReference(name, expression);

        System.out.println(testClass + attributeR.buildStatement());

        assertEquals(expected, attributeR.buildStatement());
    }

    @ParameterizedTest
    @MethodSource("sources_build_object")
    void build_object(String name, Expression expression, String expected) {
        AttributeReference attributeR = new AttributeReference(name, expression);

        System.out.println(attributeR.buildObject());

        assertEquals(expected, attributeR.buildObject());
    }

    @Test
    void build_name() {
        AttributeReference attributeR = new AttributeReference("attributeR", new IntLiteral(23));

        System.out.println(testClass + attributeR.buildName());

        assertEquals("\"attributeR\"", attributeR.buildName());
    }
}
