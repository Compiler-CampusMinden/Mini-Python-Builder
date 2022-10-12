package CBuilder;

/**
 * C representation of anything that does something in MiniPython, e. g. assignments, control structures, function calls.
 */
public interface Statement {

    /**
     * Create the C-Code of the representing statement.
     *
     * @return A String which represents the c-code of the statement.
     */
    String buildStatement();

}
