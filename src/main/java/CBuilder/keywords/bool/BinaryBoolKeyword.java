package CBuilder.keywords.bool;

import CBuilder.Expression;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;

import java.util.List;
import java.util.Map;

class BinaryBoolKeyword implements Expression {

    private String cOp;
    private Expression x;
    private Expression y;

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
        Call boolCallY = new Call(boolMethodX, List.of(), Map.of());
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
