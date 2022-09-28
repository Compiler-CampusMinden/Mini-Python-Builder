package CBuilder.objects;

import CBuilder.Expression;
import CBuilder.Reference;

/**
 * Mini-Python attribute access, i. e. `a.b`.
 */
public class AttributeReference implements Expression {

    private String name;
    private Expression object;

    /**
     * Example:
     * ```
     * a = 1
     * a.__str__ # AttributeReference("__str__", new Reference("a"))
     * ```
     *
     * @param name The name of the reference.
     * @param object The object to retrieve the reference from
     */
    public AttributeReference(String name, Expression object) {
        this.name = name;
        this.object = object;
    }

    protected String buildName() {
        return "\"" + name + "\"";
    }

    protected String buildObject() {
        return object.buildExpression();
    }

    @Override
    public String buildExpression() {
        return "__mpy_obj_get_attr(" + object.buildExpression() + ", \"" + name + "\")";
    }

    @Override
    public String buildStatement() {
        return buildExpression() + ";\n";
    }

    public String getName() { return this.name; }
}
