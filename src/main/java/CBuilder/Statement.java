package CBuilder;

/**
 * C representation of anything that does something in MiniPython, e. g. assignments, control structures, function calls.
 */
public interface Statement {

    String buildStatement();

}
