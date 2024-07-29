package CBuilder.objects.functions;

import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.variables.VariableDeclaration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mini-Python function declaration.
 *
 * <p>Implements the Reference interface to allow using instances for referring to the declaration
 * too.
 */
public class Function extends Reference {

    private String cName;
    private final List<Statement> body;
    private final List<Argument> positionalArgs; // (a, b, c)
    // TODO default arguments (a=1)
    private final boolean receivesPackedPositionalArgs; // *args
    private final boolean receivesPackedKeywordArgs; // **args

    private final List<VariableDeclaration> localVariables;

    /**
     * Create a new function (or method).
     *
     * <p>Receives a funcName to fulfill requirements of both mini-python's duck typing and the
     * declaration of the native C function containing the functions' statements. Since there are no
     * namespaces in C, a globally unique name is required for the declaration of the function
     * itself. For functions, funcName is used for this purpose; for methods, the class name is
     * automatically added as a prefix to funcName once the class is created (MPyClass).
     *
     * @param funcName The mini-python name of the function.
     * @param body The body of the function.
     * @param positionalArgs The arguments of the function.
     * @param localVariables Variables declared inside the function.
     */
    public Function(
            String funcName,
            List<Statement> body,
            List<Argument> positionalArgs,
            List<VariableDeclaration> localVariables) {
        super(funcName);
        this.cName = funcName;
        this.body = body;
        this.positionalArgs = positionalArgs;
        this.receivesPackedKeywordArgs = false;
        this.receivesPackedPositionalArgs = false;
        this.localVariables = localVariables;
    }

    /**
     * Creates a unique method name used in emitted C code (auxiliary method, called by MPyClass)
     *
     * <p>A method is a function that can only be called in connection with its class. However,
     * since a method is emitted just as a normal function in the C code, the method name must be
     * extended with a prefix at C level and thus be made unique. The constructor of MPyClass
     * handles this by calling this auxiliary method.
     *
     * <p>To call a method, use its name (funcName) and not its unique C name.
     *
     * <p>Warning: Do not use this helper method for functions, as functions are not allowed to have
     * a different C name.
     *
     * @param prefix The prefix string which should be adde to the C name.
     */
    public void createUniqueCName(String prefix) {
        cName = prefix + name;
    }

    /**
     * Create the C code representation for the function.
     *
     * @return The C code of the function implementing this mini-python function.
     */
    public String buildCFunction() {
        StringBuilder declaration = new StringBuilder();

        // FIXME args and kwargs probably breaks code that does not use the same names
        // for accessing packed arguments
        declaration.append("__MPyObj* func_" + cName + "(__MPyObj *args, __MPyObj *kwargs) {\n");

        StringBuilder body = new StringBuilder();

        body.append("assert(args != NULL && kwargs != NULL);\n\n");

        body.append(
                "__MPyGetArgsState argHelper = __mpy_args_init(\""
                        + name
                        + "\", args, kwargs, "
                        + positionalArgs.size()
                        + ");\n");

        for (Argument arg : positionalArgs) {
            body.append(arg.buildArgExtraction());
        }

        if (!receivesPackedPositionalArgs && !receivesPackedKeywordArgs) {
            body.append("__mpy_args_finish(&argHelper);\n");
        } else {
            // FIXME leave out check for unused arguments only partially (not implemented on the c
            // side too)
            body.append("__mpy_obj_ref_dec(args);\n");
            body.append("__mpy_obj_ref_dec(kwargs);\n");
        }
        body.append("\n");

        // this is very ugly, but currently the variable name needs to be manually
        // kept in sync with [ReturnStatement]
        body.append("__MPyObj *retValue = NULL;\n\n");

        for (VariableDeclaration v : this.localVariables) {
            body.append(v.build(true));
        }

        for (Statement s : this.body) {
            body.append(s.buildStatement());
        }
        body.append("\n");

        for (Argument arg : positionalArgs) {
            body.append(arg.buildArgCleanup());
        }
        body.append("\n");

        body.append("goto ret;\n"); // ugly hack to prevent 'unused label' c compiler warning/error
        body.append("ret:\n");
        body.append("if (retValue == NULL) {\n");
        body.append("\tretValue = __mpy_obj_init_object();\n"); // FIXME init none here
        body.append("}\n");
        body.append("return __mpy_obj_return(retValue);");

        declaration.append(
                body.toString()
                        .lines()
                        .map(string -> "\t" + string + "\n")
                        .collect(Collectors.joining()));
        declaration.append("}\n");

        return declaration.toString();
    }

    /**
     * Create the C code for object declaration of this function.
     *
     * @return A mini-python object declaration for this function.
     */
    public String buildFuncObjectDeclaration() {
        return "__MPyObj *" + name + ";\n";
    }

    /**
     * Create the C code for initialization of the function's mini-python object.
     *
     * @return Initialisation code for this function's mini-python object.
     */
    public String buildInitialisation() {
        return name
                + " = __mpy_obj_init_func(&func_"
                + cName
                + ");\n"
                + "__mpy_obj_ref_inc("
                + name
                + ");\n";
    }

    /**
     * Create the C code for cleaning up the function object.
     *
     * @return Cleanup code for collecting this function's mini-python object.
     */
    public String buildRefDec() {
        return "__mpy_obj_ref_dec(" + name + ");\n";
    }
}
