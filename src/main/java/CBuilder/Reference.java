package CBuilder;

/** Representation of a reference to a MiniPython variable, i. e. a variable name. */
public class Reference implements Expression {

    /** The name of the reference. */
    protected String name;

    /**
     * Create a new reference for the given name.
     *
     * @param name The name of the reference which should be created.
     */
    public Reference(String name) {
        this.name = name;
    }

    /**
     * Get the reference name.
     *
     * @return The name of the reference.
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String buildExpression() {
        return name;
    }

    @Override
    public String buildStatement() {
        return name + ";\n";
    }
}
