package CBuilder.literals;

public class StringLiteral implements Literal {

    String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public String buildExpression() {
        return "__mpy_obj_init_str_static(\"" + value + "\")";
    }

    @Override
    public String buildStatement() {
        return "__mpy_obj_ref_dec" + buildExpression() + ");\n";
    }
}
