/* (C)2024 */
package CBuilder.conditions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.conditions.conditionalStatement.ElifStatement;
import CBuilder.conditions.conditionalStatement.ElseStatement;
import CBuilder.conditions.conditionalStatement.IfStatement;
import CBuilder.literals.BoolLiteral;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TestIfThenElseStatement {
    String testClass = "[IFTHENELSESTATEMENT]\n";

    private static Stream<Arguments> sources_build_statement() {
        return Stream.of(
                Arguments.of(
                        new IfStatement(new BoolLiteral(true), List.of()),
                        Optional.of(List.of(new ElifStatement(new BoolLiteral(true), List.of()))),
                        Optional.of(new ElseStatement(List.of())),
                        """
                                if (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(true), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
                                }else if (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(true), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
                                }else {
                                }"""),
                Arguments.of(
                        new IfStatement(new BoolLiteral(true), List.of()),
                        Optional.empty(),
                        Optional.of(new ElseStatement(List.of())),
                        """
                                if (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(true), "__bool__"), __mpy_obj_init_tuple(0), NULL))) {
                                }else {
                                }"""),
                Arguments.of(
                        new IfStatement(new BoolLiteral(true), List.of()),
                        Optional.empty(),
                        Optional.empty(),
                        "if (__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(true),"
                            + " \"__bool__\"), __mpy_obj_init_tuple(0), NULL))) {\n"
                            + "}"));
    }

    @ParameterizedTest
    @MethodSource("sources_build_statement")
    void build_statement(
            IfStatement ifStatement,
            Optional<List<ElifStatement>> elifStatementList,
            Optional<ElseStatement> elseStatement,
            String expected) {
        IfThenElseStatement ifThenElseS =
                new IfThenElseStatement(ifStatement, elifStatementList, elseStatement);

        System.out.println(testClass + ifThenElseS.buildStatement());

        assertEquals(expected, ifThenElseS.buildStatement());
    }
}
