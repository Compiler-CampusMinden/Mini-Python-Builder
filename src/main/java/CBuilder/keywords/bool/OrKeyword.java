package CBuilder.keywords.bool;

import CBuilder.Expression;

public class OrKeyword extends BinaryBoolKeyword {

    public OrKeyword(Expression x, Expression y) {
        super("||", x, y);
    }

}
