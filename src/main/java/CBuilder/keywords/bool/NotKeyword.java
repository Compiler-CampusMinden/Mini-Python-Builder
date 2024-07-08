package CBuilder.keywords.bool;

import CBuilder.Expression;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;
import java.util.List;

/** A boolean not operation (Python/MiniPython). */
public class NotKeyword implements Expression {

    /** The boolean expression to negate. */
    private Expression x;

    /**
     * Create a new boolean not operation.
     *
     * @param x The boolean expression to negate.
     */
    public NotKeyword(Expression x) {
        this.x = x;
    }

    @Override
    public String buildExpression() {
        AttributeReference boolMethodX = new AttributeReference("__bool__", x);
        Call boolCallX = new Call(boolMethodX, List.of());
        String boolX = "__mpy_boolean_raw(" + boolCallX.buildExpression() + ")";

        return "__mpy_obj_init_boolean(!" + boolX + ")";
    }

    @Override
    public String buildStatement() {
        // allow cleanup of returned object (which would otherwise simply vanish and leak the
        // allocated memory)
        // note: not needed for expressions, since as an expression the returned object is used (e.
        // g. for assignment)
        return "__mpy_obj_ref_dec(" + buildExpression() + ");\n";
    }
}
