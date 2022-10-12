package CBuilder.keywords.bool;

import CBuilder.Expression;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;

import java.util.List;
import java.util.Map;

/**
 * Represents a boolean operation with a left and a right expression.
 */
class BinaryBoolKeyword implements Expression {

    /**
     * The representing operator in c.
     */
    private String cOp;

    /**
     * The operations left expression.
     */
    private Expression x;

    /**
     * The operations right expression.
     */
    private Expression y;

    /**
     * Create a new boolean operation.
     *
     * @param cOp The representing operator in c.
     * @param x The operations left expression.
     * @param y The operations right expression.
     */
    public BinaryBoolKeyword(String cOp, Expression x, Expression y) {
        this.cOp = cOp;
        this.x = x;
        this.y = y;
    }

    @Override
    public String buildExpression() {
        AttributeReference boolMethodX = new AttributeReference("__bool__", x);
        Call boolCallX = new Call(boolMethodX, List.of(), Map.of());
        String boolX = "__mpy_boolean_raw(" + boolCallX.buildExpression() + ")";

        AttributeReference boolMethodY = new AttributeReference("__bool__", y);
        Call boolCallY = new Call(boolMethodY, List.of(), Map.of());
        String boolY = "__mpy_boolean_raw(" + boolCallY.buildExpression() + ")";

        return "__mpy_obj_init_boolean(" + boolX + " " + cOp + " " + boolY + ")";
    }

    @Override
    public String buildStatement() {
        // allow cleanup of returned object (which would otherwise simply vanish and leak the allocated memory)
        // note: not needed for expressions, since as an expression the returned object is used (e. g. for assignment)
        return "__mpy_obj_ref_dec(" + buildExpression() + ");\n";
    }
}
