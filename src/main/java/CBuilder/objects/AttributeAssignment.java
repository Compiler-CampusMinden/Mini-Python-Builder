package CBuilder.objects;

import CBuilder.Expression;
import CBuilder.Statement;

public class AttributeAssignment implements Statement {

    private AttributeReference attribute;
    private Expression value;

    public AttributeAssignment(AttributeReference attribute, Expression value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public String buildStatement() {
        return "__mpy_obj_set_attr(" + attribute.buildObject() + ", " + attribute.buildName() + ", " +  value.buildExpression() + ");";
    }
}
