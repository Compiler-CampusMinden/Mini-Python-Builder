package CBuilder.literals;

import CBuilder.Expression;
import java.util.List;

/** A simple tuple literal. */
public class TupleLiteral implements Literal {

    /** The list of values the tuple is containing. */
    private final List<Expression> elems;

    /**
     * Create a new tuple literal with the specified values.
     *
     * @param elems A list of values the tuple should contain.
     */
    public TupleLiteral(List<Expression> elems) {
        this.elems = elems;
    }

    @Override
    public String buildExpression() {
        StringBuilder tupleInit = new StringBuilder();

        for (int i = 0; i < elems.size(); i++) {
            tupleInit.append(
                    "__mpy_tuple_assign(" + i + ", " + elems.get(i).buildExpression() + ", ");
        }
        tupleInit.append("__mpy_obj_init_tuple(" + elems.size() + ")");
        tupleInit.append(")".repeat(elems.size()));

        return tupleInit.toString();
    }

    @Override
    public String buildStatement() {
        return "__mpy_obj_ref_dec(" + buildExpression() + ");\n";
    }
}
