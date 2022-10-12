package CBuilder.objects;

import CBuilder.Expression;
import CBuilder.Statement;

/**
 * Assign a value to the attribute of an object.
 */
public class AttributeAssignment implements Statement {

    /**
     * The reference of the attribute in the object scope.
     */
    private AttributeReference attribute;

    /**
     * The value to assign to the attribute.
     */
    private Expression value;

    /**
     * Create a new attribute assignment.
     *
     * @param attribute The reference of the attribute in the object scope.
     * @param value The value to assign to the attribute.
     */
    public AttributeAssignment(AttributeReference attribute, Expression value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public String buildStatement() {
        return "__mpy_obj_set_attr(" + attribute.buildObject() + ", " + attribute.buildName() + ", " +  value.buildExpression() + ");";
    }
}
