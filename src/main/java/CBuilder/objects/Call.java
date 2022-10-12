package CBuilder.objects;

import CBuilder.Expression;
import CBuilder.literals.TupleLiteral;

import java.util.List;
import java.util.Map;

/**
 * A Mini-Python call, i. e. a function call or object initialisation.
 *
 * Samples: `object()`, `a.b()`.
 */
public class Call implements Expression {

    private List<Expression> args;
    private Map<String, Expression> kwargs;
    private Expression callable;

    /**
     *
     * @param callable The callable object, i. e. a type or a function.
     * @param args The positional arguments of the call.
     * @param kwargs The keyword arguments of the call.
     */
    public Call(Expression callable, List<Expression> args, Map<String, Expression> kwargs) {
        this.callable = callable;
        this.args = args;
        this.kwargs = kwargs;
    }

    @Override
    public String buildExpression() {

        // first pack arguments, i.e. put them into a tuple
        TupleLiteral packedArguments = new TupleLiteral(args);

        // then keyword arguments
        // FIXME unimplemented

        // finally: call
        // FIXME replace NULL with keyword arguments
        return "__mpy_call(" + callable.buildExpression() + ", " + packedArguments.buildExpression() + ", NULL)";
    }

    @Override
    public String buildStatement() {
        // allow cleanup of returned object (which would otherwise simply vanish and leak the allocated memory)
        // note: not needed for expressions, since as an expression the returned object is used (e. g. for assignment)
        return "__mpy_obj_ref_dec(" + buildExpression() + ");\n";
    }

}
