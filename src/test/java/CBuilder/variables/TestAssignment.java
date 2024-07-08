/* (C)2024 */
package CBuilder.variables;

import static org.junit.jupiter.api.Assertions.assertEquals;

import CBuilder.Expression;
import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.literals.IntLiteral;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.Function;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Snapshot 22.02.2024 */
public class TestAssignment {
    String testClass = "[ASSIGNMENT]\n";

    /**
     * Test right hand side reference <br>
     * a = b
     */
    @Test
    void assignment_rhs_ref() {
        String expected =
                """
                __mpy_obj_ref_inc(b);
                __mpy_obj_ref_dec(a);
                a = b;
                """;

        Assignment assignment = new Assignment(new Reference("a"), new Reference("b"));

        String actual = assignment.buildStatement();

        System.out.println(testClass + actual);

        assertEquals(expected, actual);
    }

    /**
     * Test right hand side literal (no reference)<br>
     * a = 133
     */
    @Test
    void assignment_rhs_int_literal() {
        String expected =
                """
                __mpy_obj_ref_dec(a);
                a = __mpy_obj_init_int(133);
                __mpy_obj_ref_inc(a);
                """;

        Assignment assignment = new Assignment(new Reference("a"), new IntLiteral(133));

        String actual = assignment.buildStatement();

        System.out.println(testClass + actual);

        assertEquals(expected, actual);
    }

    /**
     * Test right hand side function (which is also reference) <br>
     * a = printA
     */
    @Test
    void assignment_rhs_function() {
        String expected =
                """
                __mpy_obj_ref_inc(printA);
                __mpy_obj_ref_dec(a);
                a = printA;
                """;

        Function printA =
                new Function(
                        "printA",
                        List.of(
                                new Statement[] {
                                    new Call(
                                            new Reference("print"),
                                            List.of(new Expression[] {new Reference("a")}))
                                }),
                        List.of(new Argument[] {new Argument("a", 0)}),
                        List.of());

        Assignment assignment = new Assignment(new Reference("a"), printA);

        String actual = assignment.buildStatement();

        System.out.println(testClass + actual);

        assertEquals(expected, actual);
    }

    // d = a+b

    /**
     * Test right hand side expression (no reference)<br>
     * d = a + b
     */
    @Test
    void assignment_rhs_expression() {
        String expected =
                """
                __mpy_obj_ref_dec(d);
                d = __mpy_call(__mpy_obj_get_attr(a, "__add__"), __mpy_tuple_assign(0, b, __mpy_obj_init_tuple(1)), NULL);
                __mpy_obj_ref_inc(d);
                """;

        AttributeReference addA = new AttributeReference("__add__", new Reference("a"));
        Assignment assignD =
                new Assignment(
                        new Reference("d"),
                        new Call(addA, List.of(new Expression[] {new Reference("b")})));

        String actual = assignD.buildStatement();

        System.out.println(testClass + actual);

        assertEquals(expected, actual);
    }
}
