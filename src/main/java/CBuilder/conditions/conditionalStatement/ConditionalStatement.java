package CBuilder.conditions.conditionalStatement;

import CBuilder.Expression;
import CBuilder.Statement;
import CBuilder.objects.AttributeReference;
import CBuilder.objects.Call;

import java.util.List;
import java.util.Map;

/**
 * Internal base for all conditional control structures that consist of a condition and a body following that condition.
 */
class ConditionalStatement implements Statement {

    /**
     * The keyword of the representing conditional statement.
     */
    private String keyword;

    /**
     * The condition of the representing conditional statement.
     */
    private Expression condition;

    /**
     * A List of all statements in the body of the conditional statement.
     */
    private List<Statement> body;

    /**
     * Create a new conditional statement block.
     *
     * @param keyword The keyword of the representing conditional statement.
     * @param condition The condition of the representing conditional statement.
     * @param body A List of all statements in the body of the conditional statement.
     */
    public ConditionalStatement(String keyword, Expression condition, List<Statement> body) {
        this.keyword = keyword;
        this.condition = condition;
        this.body = body;
    }

    /**
     * Generate the c-code of the representing conditional statement.
     *
     * @return A string which containing the c-code representation of the conditional statement.
     */
    public String build() {
        StringBuilder string = new StringBuilder();

        // base idea:
        // ```c
        // <keyword> (__mpy_boolean_raw(<boolFn>(<conditionExpression>))) {
        //     <body>
        // }
        // ```
        //
        // why the `__mpy_boolean_raw` and `<boolFn>` stuff:
        // - <boolFn> (boolMethod below): make sure we have a boolean value
        // - __mpy_boolean_raw: convert a minipython boolean object into the corresponding c value

        string.append(keyword + " (");

        AttributeReference boolMethod = new AttributeReference("__bool__", condition);
        Call boolCall = new Call(boolMethod, List.of(), Map.of());

        string.append("__mpy_boolean_raw(" + boolCall.buildExpression() + ")) {\n");

        for (Statement s : body) {
            string.append("\t" + s.buildStatement());
        }

        string.append("}");

        return string.toString();
    }

    @Override
    public String buildStatement() { return null; }
}
