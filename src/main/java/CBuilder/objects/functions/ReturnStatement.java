package CBuilder.objects.functions;

import CBuilder.Expression;
import CBuilder.Statement;

/**
 * A return statement inside a function.
 */
public class ReturnStatement implements Statement {

    private Expression returnValue;

    /**
     * Create a new return statement.
     *
     * @param returnValue return value of this return statement
     */
    public ReturnStatement(Expression returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * @return C code to return returnValue from the current function
     */
    @Override
    public String buildStatement() {
        // Note: retVal and goto ret depend on the implementation of Function#buildCFunction
        // I'm sorry for all the hidden interdependencies in this stuff, but I have no idea
        // how this could have been solved better
        return "retValue = " + returnValue.buildExpression() + ";\n" +
                "goto ret;\n";
    }
}
