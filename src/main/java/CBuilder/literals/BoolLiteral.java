package CBuilder.literals;

public class BoolLiteral implements Literal {

    boolean value;

    public BoolLiteral(boolean value) {
        this.value = value;
    }

    @Override
    public String buildExpression() {
        return "__mpy_obj_init_boolean(" + value + ")";
    }

    @Override
    public String buildStatement() {
        return buildExpression() + ";\n";
    }

}
