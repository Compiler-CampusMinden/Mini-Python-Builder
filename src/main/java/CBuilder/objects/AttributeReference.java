package CBuilder.objects;

import CBuilder.Expression;

/** Mini-Python attribute access, i. e. `a.b`. */
public class AttributeReference implements Expression {

    /** The name of the attribute the reference should point to. */
    private String name;

    /** The object which containing the referenced attribute. */
    private Expression object;

    /**
     * Create a new attribute reference for the given object.
     *
     * <p>Example: ``` a = 1 a.__str__ # AttributeReference("__str__", new Reference("a")) ```
     *
     * @param name The name of the reference.
     * @param object The object to retrieve the reference from.
     */
    public AttributeReference(String name, Expression object) {
        this.name = name;
        this.object = object;
    }

    /**
     * Create the c-code of the attribute name.
     *
     * @return The c-code which represents the attribute name.
     */
    protected String buildName() {
        return "\"" + name + "\"";
    }

    /**
     * Create the c-code of the object.
     *
     * @return The c-code which represents the object.
     */
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

    /**
     * Get the attribute name.
     *
     * @return The name of the attribute.
     */
    public String getName() {
        return this.name;
    }
}
