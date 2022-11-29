package CBuilder.objects.functions;

import CBuilder.Reference;
import CBuilder.Statement;
import CBuilder.variables.VariableDeclaration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mini-Python function declaration.
 * Implements the Reference interface to allow using instances for referring to the declaration too.
 */
public class Function extends Reference {

    private final String uniqueName;
    private final List<Statement> body;
    private final List<Argument> positionalArgs; // (a, b, c)
    // TODO default arguments (a=1)
    private final boolean receivesPackedPositionalArgs; // *args
    private final boolean receivesPackedKeywordArgs; // **args

    private final List<VariableDeclaration> localVariables;


    /**
     * Create a new function.
     *
     * Receives both funcName and unique name to fulfill requirements of both mini-python's duck typing
     * and the declaration of the native C function containing the functions' statements.
     * Since C does not have namespaces, for declaring the function itself a globally unique name is needed.
     * But to make duck typing work, function names that were equal in the original mini-python code
     * need to be the same here too.
     *
     * By setting receivesPackedKeywordArgs or receivesPackedPositionalArgs to true,
     * the functions arguments handling make 'args' and/or 'kwargs' variables available in the scope of the function.
     *
     * @param funcName The mini-python name of the function.
     * @param uniqueName The C name for the function.
     * @param body The body of the function.
     * @param positionalArgs The arguments of the function.
     * @param localVariables Variables declared inside the function.
     */
    public Function(String funcName, String uniqueName, List<Statement> body, List<Argument> positionalArgs, List<VariableDeclaration> localVariables) {
        super(funcName);

        this.uniqueName = uniqueName;
        this.body = body;
        this.positionalArgs = positionalArgs;
        this.receivesPackedKeywordArgs = false;
        this.receivesPackedPositionalArgs = false;
        this.localVariables = localVariables;
    }

    /**
     * Create the c-code representation for the function.
     *
     * @return The c-code of the  function implementing this mini-python function.
     */
    public String buildCFunction() {
        StringBuilder declaration = new StringBuilder();

        // FIXME args and kwargs probably breaks code that does not use the same names
        // for accessing packed arguments
        declaration.append("__MPyObj* func_" + uniqueName + "(__MPyObj *args, __MPyObj *kwargs) {\n");

        StringBuilder body = new StringBuilder();

        body.append("assert(args != NULL && kwargs != NULL);\n\n");

        body.append("__MPyGetArgsState argHelper = __mpy_args_init(\"" + name + "\", args, kwargs, " + positionalArgs.size() + ");\n");

        for (Argument arg : positionalArgs) {
            body.append(arg.buildArgExtraction());
        }

        if (!receivesPackedPositionalArgs && !receivesPackedKeywordArgs) {
            body.append("__mpy_args_finish(&argHelper);\n");
        } else {
            // FIXME leave out check for unused arguments only partially (not implemented on the c side too)
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

        declaration.append(body.toString().lines().map(string -> "\t" + string + "\n").collect(
                Collectors.joining()));
        declaration.append("}\n");

        return declaration.toString();
    }

    /**
     * Create the c-code for object declaration of this function.
     *
     * @return A mini-python object declaration for this function.
     */
    public String buildFuncObjectDeclaration() {
        return "__MPyObj *" + name + ";\n";
    }

    /**
     * Create the c-code for initialization of the function's mini-python object.
     *
     * @return Initialisation code for this function's mini-python object.
     */
    public String buildInitialisation() {
        return name + " = __mpy_obj_init_func(&func_" + uniqueName +  ");\n" +
                "__mpy_obj_ref_inc(" + name + ");\n";
    }

    /**
     * Create the c-code for cleaning up the function object.
     *
     * @return Cleanup code for collecting this function's mini-python object.
     */
    public String buildRefDec() {
        return "__mpy_obj_ref_dec(" + name + ");\n";
    }

}
