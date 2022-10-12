package CBuilder.variables;

import CBuilder.Expression;
import CBuilder.Reference;
import CBuilder.Statement;

/**
 * Assign a value to a variable.
 */
public class Assignment implements Statement {

    /**
     * The name of the variable.
     */
    private final Reference lhs;

    /**
     * The new value of the variable.
     */
    private final Expression rhs;

    /**
     * Create a new assignment that assigns a value to a variable.
     *
     * @param lhs The name of the variable to assign to.
     * @param rhs The value to assign.
     */
    public Assignment(Reference lhs, Expression rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public String buildStatement() {
        String rhs = this.rhs.buildExpression();
        String lhs = this.lhs.buildExpression();

        if (this.rhs instanceof Reference) {
            // first increment reference count of object we assign since it's now used once more
            // then decrement reference count of object in variable that is reassigned, since it's now used once less.
            // Afterwards perform assignment.
            //
            // Note: Order is important, since for example for the case of `a=a` the variable could reach a count of 0 even
            // if it's still in use afterwards.
            return "__mpy_obj_ref_inc(" + rhs + ");\n" +
                    "__mpy_obj_ref_dec(" + lhs + ");\n" +
                    lhs + " = " + rhs + ";\n";
        } else {
            // Note: Order is not important here, since returned values (literals or function calls)
            // have the temporary mechanism and therefore a refCount of +1 until the _inc call, so calling _dec first
            // is not problematic (e. g. def returnSelf(x): return x; x = 10; x = returnSelf(x); works even if decrementing first)
            return "__mpy_obj_ref_dec(" + lhs + ");\n" +
                    lhs + " = " + rhs + ";\n" +
                    "__mpy_obj_ref_inc(" + lhs + ");\n";
        }
    }

}
