package CBuilder.objects;

import CBuilder.Expression;
import CBuilder.Reference;
import CBuilder.objects.functions.Function;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mini-Python class declaration.
 */
public class MPyClass extends Reference {

    /**
     * The reference to the parent class.
     */
    private final Reference parent;

    /**
     * The list of methods the class contains.
     */
    private final List<Function> functions;

    /**
     * The list of static attributes the class contains.
     */
    private final Map<Reference, Expression> classAttributes;

    /**
     * Create a new class definition.
     *
     * @param name The type's name.
     * @param parent The parent class.
     * @param functions The associated functions and methods.
     * @param classAttributes The classes attributes.
     */
    public MPyClass(String name, Reference parent, List<Function> functions, Map<Reference, Expression> classAttributes) {
        super(name);
        this.parent = parent;
        this.functions = functions;
        this.classAttributes = classAttributes;
        this.functions.forEach(func -> func.createUniqueCName(this.name + "_"));
    }

    /**
     * Create the c-code for object declaration of this class definition.
     *
     * @return A mini-python object declaration for this class definition.
     */
    public String buildDeclaration() {
        StringBuilder declaration = new StringBuilder();

        // first declare object for the class itself
        declaration.append("__MPyObj *" + name + ";\n");

        // then all method's c implementation
        for (Function function : this.functions) {
            declaration.append(function.buildCFunction());
        }

        return declaration.toString();
    }

    /**
     * Create the c-code for initialization of the class definition object.
     *
     * @return Initialisation code for this class definition object.
     */
    public String buildInitialisation() {
        StringBuilder init = new StringBuilder();

        // first create the class object
        init.append(name + " = __mpy_obj_init_type(\"" + name + "\"" + ", " + parent.buildExpression() + ");\n");
        init.append("__mpy_obj_ref_inc(" + name + ");\n");

        // then bind attributes
        for (Map.Entry<Reference, Expression> attribute : classAttributes.entrySet()) {
            init.append("__mpy_obj_set_attr(" + name + ", \"" + attribute.getKey().buildExpression() + "\", " + attribute.getValue().buildExpression() + ");");
        }

        // and bind functions
        for (Function function : functions) {
            StringBuilder funcBind = new StringBuilder();

            funcBind.append(function.buildFuncObjectDeclaration());
            funcBind.append(function.buildInitialisation());

            funcBind.append("__mpy_obj_set_attr(" + name + ", \"" + function.buildExpression() + "\", " + function.buildExpression() + ");\n");
            funcBind.append(function.buildRefDec()); // this may look surprising, but keep in mind the function object
            // is essentially a temporary variable here, which then naturally needs to be correctly refCounted

           // perform init inside temporary scope to prevent pollution of the current scope
           // with the function names of the class
            init.append("{\n");
            init.append(funcBind.toString().lines().map(string -> "\t" + string + "\n").collect(
                    Collectors.joining()));
            init.append("}\n");
        }

        return init.toString();
    }

    /**
     * Create the c-code for cleaning up the class definition object.
     *
     * @return Cleanup code for collecting this class definition object.
     */
    public String buildRefDec() {
        return "__mpy_obj_ref_dec(" + name + ");\n";
    }

    @Override
    public String buildStatement() {
        return null;
    }
}
