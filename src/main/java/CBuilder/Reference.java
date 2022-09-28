package CBuilder;

/**
 * Representation of a reference to a MiniPython variable, i. e. a variable name.
 */
public class Reference implements Expression {

    protected String name;

    public Reference(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }

    @Override
    public String buildExpression() {
        return name;
    }

    @Override
    public String buildStatement() {
        return name + ";\n";
    }

}
