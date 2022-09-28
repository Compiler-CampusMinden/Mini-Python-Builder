package CBuilder.keywords.bool;

import CBuilder.Expression;

public class AndKeyword extends BinaryBoolKeyword {

    public AndKeyword(Expression x, Expression y) {
        super("&&", x, y);
    }

}
