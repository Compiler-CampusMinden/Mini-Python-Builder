/* (C)2024 */
package CBuilder.bool;

import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.keywords.bool.AndKeyword;
import CBuilder.literals.IntLiteral;
import org.junit.jupiter.api.Test;

/** Snapshot 23.02.2024 */
public class TestAndKeyword {
    String testClass = "[ANDKEYWORD]\n";

    /** 1 && 1 */
    @Test
    void build_expression() {
        String expected =
                "__mpy_obj_init_boolean(__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1),"
                    + " \"__bool__\"), __mpy_obj_init_tuple(0), NULL)) &&"
                    + " __mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1),"
                    + " \"__bool__\"), __mpy_obj_init_tuple(0), NULL)))";

        AndKeyword andK = new AndKeyword(new IntLiteral(1), new IntLiteral(1));

        System.out.println(testClass + andK.buildExpression());

        assertEquals(expected, andK.buildExpression());
    }

    @Test
    void build_statement() {
        String expected =
                "__mpy_obj_ref_dec(__mpy_obj_init_boolean(__mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1),"
                    + " \"__bool__\"), __mpy_obj_init_tuple(0), NULL)) &&"
                    + " __mpy_boolean_raw(__mpy_call(__mpy_obj_get_attr(__mpy_obj_init_int(1),"
                    + " \"__bool__\"), __mpy_obj_init_tuple(0), NULL))));\n";

        AndKeyword andK = new AndKeyword(new IntLiteral(1), new IntLiteral(1));

        System.out.println(testClass + andK.buildStatement());

        assertEquals(expected, andK.buildStatement());
    }
}
