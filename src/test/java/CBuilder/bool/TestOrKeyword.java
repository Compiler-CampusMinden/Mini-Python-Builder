/* (C)2024 */
package CBuilder.bool;

import CBuilder.keywords.bool.OrKeyword;
import CBuilder.literals.BoolLiteral;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Snapshot 23.02.2024
 */
public class TestOrKeyword {
    String testClass = "[ORKEYWORD]\n";

    @Test
    void build_expression() {
        String expected =
                "__mpy_obj_init_boolean(__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(true),"
                    + " \"__bool__\"), __mpy_obj_init_tuple(0), NULL)) ||"
                    + " __mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(false),"
                    + " \"__bool__\"), __mpy_obj_init_tuple(0), NULL)))";

        OrKeyword orK = new OrKeyword(new BoolLiteral(true), new BoolLiteral(false));

        System.out.println(testClass + orK.buildExpression());

        assertEquals(expected, orK.buildExpression());
    }

    @Test
    void build_statement() {
        String expected =
                "__mpy_obj_ref_dec(__mpy_obj_init_boolean(__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(true),"
                    + " \"__bool__\"), __mpy_obj_init_tuple(0), NULL)) ||"
                    + " __mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_boolean(false),"
                    + " \"__bool__\"), __mpy_obj_init_tuple(0), NULL))));\n";

        OrKeyword orK = new OrKeyword(new BoolLiteral(true), new BoolLiteral(false));

        System.out.println(testClass + orK.buildStatement());

        assertEquals(expected, orK.buildStatement());
    }
}
