package CBuilder.keywords.bool;

import CBuilder.Expression;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;

import java.util.List;
import java.util.Map;

public class NotKeyword implements Expression {

    private Expression x;

    public NotKeyword(Expression x) {
        this.x = x;
    }

    @Override
    public String buildExpression() {
        AttributeReference boolMethodX = new AttributeReference("__bool__", x);
        Call boolCallX = new Call(boolMethodX, List.of(), Map.of());
        String boolX = "__mpy_boolean_raw(" + boolCallX.buildExpression() + ")";

        return "__mpy_obj_init_boolean(!" + boolX + ")";
    }

    @Override
    public String buildStatement() {
        // allow cleanup of returned object (which would otherwise simply vanish and leak the allocated memory)
        // note: not needed for expressions, since as an expression the returned object is used (e. g. for assignment)
        return "__mpy_obj_ref_dec(" + buildExpression() + ");\n";
    }

}
