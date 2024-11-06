package CBuilder;

import CBuilder.conditions.IfThenElseStatement;
import CBuilder.conditions.conditionalStatement.ElifStatement;
import CBuilder.conditions.conditionalStatement.ElseStatement;
import CBuilder.conditions.conditionalStatement.IfStatement;
import CBuilder.conditions.conditionalStatement.WhileStatement;
import CBuilder.keywords.bool.AndKeyword;
import CBuilder.keywords.bool.NotKeyword;
import CBuilder.keywords.bool.OrKeyword;
import CBuilder.literals.BoolLiteral;
import CBuilder.literals.IntLiteral;
import CBuilder.literals.StringLiteral;
import CBuilder.objects.*;
import CBuilder.objects.functions.Argument;
import CBuilder.objects.functions.ReturnStatement;
import CBuilder.variables.Assignment;
import CBuilder.variables.VariableDeclaration;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.*;

public class CBuilderGenerator extends Generator<ProgramBuilder> {
    public CBuilderGenerator() {
        super(ProgramBuilder.class);
    }

    private ProgramBuilder program;
    private GenerationStatus status;
    private static Set<String> identifiers;
    private int statementDepth;
    private int expressionDepth;
    private static final int MAX_STATEMENT_DEPTH = 6;
    private static final int MAX_EXPRESSION_DEPTH = 10;
    private Reference printRef;
    private String[] classNames = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private List<String> usedClasses = new ArrayList<String>();
    /**
     * main-function of Generator
     */
    @Override
    public ProgramBuilder generate(SourceOfRandomness random, GenerationStatus status) {
        program = new ProgramBuilder();
        printRef = new Reference("print");
        List<Expression> parameterRefList = List.of(new Expression[] {new StringLiteral("----------------FUZZING-Start")});
        // add first print to program
        Call printCall = new Call(printRef, parameterRefList);
        program.addStatement(printCall);
        this.status = status;
        this.identifiers = new HashSet<>();
        this.statementDepth = 0;
        this.expressionDepth = 0;
        int counter = 0;
        int max_counter = random.nextInt(1,100);
        // calls generateStatement 1 < x 100 Times
        while (counter < max_counter) {
            generateStatement(random);
            counter++;
        }
        // add last print to program
        parameterRefList = List.of(new Expression[] {new StringLiteral("----------------FUZZING-End")});
        printCall = new Call(printRef, parameterRefList);
        program.addStatement(printCall);
        Path fileOutput = Paths.get("build/compilerOutput/Fuzzing/"+program.hashCode());
        //writes program
        try {
            this.program.writeProgram(fileOutput);
        } catch (Exception e) {
            System.out.println(e);
        }
        return program;
    }

    /**
     * Function that choose with random parameter which statement should be added to the program.
     * @param random
     */
    private void generateStatement(SourceOfRandomness random) {
        statementDepth++;
        // non-recursive function goes in if
        // rescursive functions goes in else
        // recursive functions should increment expressiondepth at the beginning
        // and lower them on the end
        if (expressionDepth >= MAX_STATEMENT_DEPTH || random.nextBoolean()) {
            List<Consumer<SourceOfRandomness>> actions = Arrays.asList(
                this::generateCastStatement,
                this::generateAddition,
                this::generateArithmeticOperation,
                this::generateEquality,
                this::generateComparison,
                this::generateLogicOperation,
                this::generateWhile,
                this::generateIf,
                this::generateClassWithoutInheritance,
                this::generateClassWithInheritance,
                this::generateFunction
            );
            int randomIndex = random.nextInt(1,actions.size());
            actions.get(randomIndex-1).accept(random);
        }
        else {}
        statementDepth--;
    }

    /**
     * Function to generate Cast Statement.
     * @param random
     */
    private void generateCastStatement(SourceOfRandomness random) {
        expressionDepth++;
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateCast")}));
        this.program.addStatement(generatePrint);
        VariableDeclaration varA = new VariableDeclaration("a");
        Reference varAref = new Reference("a");
        // creates a random choose of type for 'a' and generates a value
        Map.Entry<String,Assignment> result = random.choose(Arrays.<Function<SourceOfRandomness, Map<String,Assignment> >>asList(
            (r) -> this.generateInteger(r,varAref),
            (r) -> this.generateBool(r,varAref)
        )).apply(random).entrySet().iterator().next();
        Assignment varAassignment = result.getValue();
        String type = result.getKey();
        String CastOp = "__str__";
        this.program.addVariable(varA);
        this.program.addStatement(varAassignment);
        // if a is boolean
        if (type=="Boolean") {
            //first cast
            CastOp = random.choose(Arrays.<Function<SourceOfRandomness, String>>asList(
                (r) -> "__str__",
                (r) -> "__int__"
            )).apply(random);
            AttributeReference varACast = new AttributeReference(CastOp,varAref);
            Call castA = new Call(varACast,List.of(new Expression[] {}));
            this.program.addStatement(castA);
            Call printA = new Call(printRef,List.of(new Expression[] {varAref}));
            this.program.addStatement(printA);
            if (CastOp != "__str__") {
                CastOp = random.choose(Arrays.<Function<SourceOfRandomness, String>>asList(
                    (r) -> "__str__",
                    (r) -> ""
                )).apply(random);
            }
            // second cast
            if (CastOp == ""){}
            else {
                varACast = new AttributeReference(CastOp,varAref);
                castA = new Call(varACast,List.of(new Expression[] {}));
                this.program.addStatement(castA);
            }
        } else {
            //if a is int
            //first cast
            CastOp = random.choose(Arrays.<Function<SourceOfRandomness, String>>asList(
                (r) -> "__str__",
                (r) -> "__int__"
            )).apply(random);
            AttributeReference varACast = new AttributeReference(CastOp,varAref);
            Call castA = new Call(varACast,List.of(new Expression[] {}));
            this.program.addStatement(castA);
            Call printA = new Call(printRef,List.of(new Expression[] {varAref}));
            this.program.addStatement(printA);
            //second cast
            CastOp = random.choose(Arrays.<Function<SourceOfRandomness, String>>asList(
                (r) -> "__str__",
                (r) -> "__int__"
            )).apply(random);
            varACast = new AttributeReference(CastOp,varAref);
            castA = new Call(varACast,List.of(new Expression[] {}));
            this.program.addStatement(castA);
            printA = new Call(printRef,List.of(new Expression[] {varAref}));
            this.program.addStatement(printA);
        }
        expressionDepth--;
    }
    private void generateAddition(SourceOfRandomness random) {
        expressionDepth++;
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateAddition")}));
        this.program.addStatement(generatePrint);
        VariableDeclaration varA = new VariableDeclaration("a");
        Reference varAref = new Reference("a");
        // generate value of a
        Map.Entry<String,Assignment> result = random.choose(Arrays.<Function<SourceOfRandomness, Map<String,Assignment> >>asList(
            (r) -> this.generateInteger(r,varAref),
            (r) -> this.generateString(r,varAref)
        )).apply(random).entrySet().iterator().next();
        Assignment varAassignment = result.getValue();
        String type = result.getKey();
        VariableDeclaration varB = new VariableDeclaration("b");
        Reference varBref = new Reference("b");
        Assignment varBassignment = null;
        // generate b with the same type as a
        if (Objects.equals(type, "Integer")) {
            varBassignment = new Assignment(varBref, generateIntLiteral(random));
        } else if (Objects.equals(type, "String")) {
            varBassignment = new Assignment(varBref, generateStringLiteral(random));
        }
        AttributeReference varAAdd = new AttributeReference("__add__",varAref);
        Call addInteger = new Call(varAAdd, List.of(new Expression[] {varBref}));

        this.program.addVariable(varA);
        this.program.addStatement(varAassignment);
        this.program.addVariable(varB);
        this.program.addStatement(varBassignment);
        this.program.addStatement(addInteger);
        expressionDepth--;
    }

    /**
     * Generates basic arithmetic operation with integer values
     * @param random
     */
    private void generateArithmeticOperation(SourceOfRandomness random) {
        expressionDepth++;
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateArithmeticOperation")}));
        this.program.addStatement(generatePrint);
        String arithOp = random.choose(Arrays.<Function<SourceOfRandomness, String>>asList(
            (r) -> "__sub__",
            (r) -> "__mul__",
            (r) -> "__div__"
        )).apply(random);

        VariableDeclaration varA = new VariableDeclaration("a");
        Reference varAref = new Reference("a");

        Assignment varAassignment = new Assignment(varAref, generateIntLiteral(random));
        VariableDeclaration varB = new VariableDeclaration("b");
        Reference varBref = new Reference("b");
        Assignment varBassignment;
        if (Objects.equals(arithOp, "__div__")) {
            varBassignment = new Assignment(varBref, generatePositivIntegerWithMax(random,33000));
        } else {
            varBassignment = new Assignment(varBref, generateIntLiteral(random));
        }

        AttributeReference varAAdd = new AttributeReference(arithOp,varAref);

        Call operation = new Call(varAAdd, List.of(new Expression[] {varBref}));

        this.program.addVariable(varA);
        this.program.addStatement(varAassignment);
        this.program.addVariable(varB);
        this.program.addStatement(varBassignment);
        this.program.addStatement(operation);
        expressionDepth--;
    }

    /**
     * Generate Equality equations with all kind of types
     * @param random
     */
    private void generateEquality(SourceOfRandomness random) {
        expressionDepth++;
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateEquality")}));
        this.program.addStatement(generatePrint);
        VariableDeclaration varA = new VariableDeclaration("a");
        Reference varAref = new Reference("a");
        Map.Entry<String,Assignment> result = random.choose(Arrays.<Function<SourceOfRandomness, Map<String,Assignment> >>asList(
            (r) -> this.generateInteger(r,varAref),
            (r) -> this.generateString(r,varAref),
            (r) -> this.generateBool(r,varAref)
        )).apply(random).entrySet().iterator().next();
        Assignment varAassignment = result.getValue();
        String type = result.getKey();

        VariableDeclaration varB = new VariableDeclaration("b");
        Reference varBref = new Reference("b");
        Assignment varBassignment = null;
        if (type == "Integer") {
            varBassignment =new Assignment(varBref, generateIntLiteral(random));
        } else if (type == "String") {
            varBassignment = new Assignment(varBref, generateStringLiteral(random));
        } else if (type == "Bool") {
            varBassignment = new Assignment(varBref, generateBoolLiteral(random));
        }

        String EqualOp = random.choose(Arrays.<Function<SourceOfRandomness, String>>asList(
            (r) -> "__eq__",
            (r) -> "__ne__"
        )).apply(random);

        AttributeReference varAAdd = new AttributeReference(EqualOp,varAref);

        Call operation = new Call(varAAdd, List.of(new Expression[] {varBref}));

        this.program.addVariable(varA);
        this.program.addStatement(varAassignment);
        this.program.addVariable(varB);
        this.program.addStatement(varBassignment);
        this.program.addStatement(operation);
        expressionDepth--;
    }

    /**
     * Generate all Kind of Comparison equation with String and Integer values
     * @param random
     */
    private void generateComparison(SourceOfRandomness random) {
        expressionDepth++;
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateComparison")}));
        this.program.addStatement(generatePrint);
        VariableDeclaration varA = new VariableDeclaration("a");
        Reference varAref = new Reference("a");
        Map.Entry<String,Assignment> result = random.choose(Arrays.<Function<SourceOfRandomness, Map<String,Assignment> >>asList(
            (r) -> this.generateInteger(r,varAref),
            (r) -> this.generateString(r,varAref)
        )).apply(random).entrySet().iterator().next();
        Assignment varAassignment = result.getValue();
        String type = result.getKey();

        VariableDeclaration varB = new VariableDeclaration("b");
        Reference varBref = new Reference("b");
        Assignment varBassignment = null;
        if (type == "Integer") {
            varBassignment =new Assignment(varBref, generateIntLiteral(random));
        } else if (type == "String") {
            varBassignment = new Assignment(varBref, generateStringLiteral(random));
        }

        String EqualOp = random.choose(Arrays.<Function<SourceOfRandomness, String>>asList(
            (r) -> "__ge__",
            (r) -> "__gt__",
            (r) -> "__le__",
            (r) -> "__lt__"
        )).apply(random);

        AttributeReference varAAdd = new AttributeReference(EqualOp,varAref);

        Call operation = new Call(varAAdd, List.of(new Expression[] {varBref}));

        this.program.addVariable(varA);
        this.program.addStatement(varAassignment);
        this.program.addVariable(varB);
        this.program.addStatement(varBassignment);
        this.program.addStatement(operation);
        expressionDepth--;
    }

    /**
     * Generate all kind of Logic Operation with boolean values
     * @param random
     */
    private void generateLogicOperation(SourceOfRandomness random) {
        expressionDepth++;
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateLogicOperation")}));
        this.program.addStatement(generatePrint);
        VariableDeclaration varA = new VariableDeclaration("a");
        Reference varAref = new Reference("a");
        Assignment varAassignment = new Assignment(varAref, generateBoolLiteral(random));

        VariableDeclaration varB = new VariableDeclaration("b");
        Reference varBref = new Reference("b");
        Assignment varBassignment = new Assignment(varBref, generateBoolLiteral(random));

        Expression result = random.choose(Arrays.<Function<SourceOfRandomness, Expression>>asList(
            (r) -> this.generateNot(varAref),
            (r) -> this.generateOr(varAref,varBref),
            (r) -> this.generateAnd(varAref,varBref)
        )).apply(random);

        Call operation = new Call(this.printRef,List.of(new Expression[] {result}));
        this.program.addVariable(varA);
        this.program.addStatement(varAassignment);
        this.program.addVariable(varB);
        this.program.addStatement(varBassignment);
        this.program.addStatement(operation);
        expressionDepth--;
    }

    /**
     * Generate While Statement
     * @param random
     */
    private void generateWhile(SourceOfRandomness random) {
        expressionDepth++;
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateWhile")}));
        this.program.addStatement(generatePrint);
        VariableDeclaration varA = new VariableDeclaration("a");
        Reference varAref = new Reference("a");
        Assignment varAassignment = new Assignment(varAref, new IntLiteral(0));
        VariableDeclaration varB = new VariableDeclaration("b");
        Reference varBref = new Reference("b");
        Assignment varBassignment = new Assignment(varBref, generatePositivIntegerWithMax(random,100));
        Expression printA = new Call(this.printRef,List.of(new Expression[] {varAref}));
        AttributeReference varAAdd = new AttributeReference("__add__",varAref);
        Expression count = new Call(varAAdd,List.of(new Expression[] {new IntLiteral(1)}));
        List<Statement> body = List.of(new Expression[] {printA,count});
        Statement whileStatement = new WhileStatement(new Call(new AttributeReference("__eq__",varAref),List.of(new Expression[] {varBref})),body);
        this.program.addVariable(varA);
        this.program.addStatement(varAassignment);
        this.program.addVariable(varB);
        this.program.addStatement(varBassignment);
        this.program.addStatement(whileStatement);
        expressionDepth--;
    }

    /**
     * Generate if Statement
     * @param random
     */
    private void generateIf(SourceOfRandomness random) {
        expressionDepth++;
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateIf")}));
        VariableDeclaration varA = new VariableDeclaration("a");
        Reference varAref = new Reference("a");
        Assignment varAassignment = new Assignment(varAref, generateBoolLiteral(random));
        Expression conditionIf = generateBoolLiteral(random);
        Expression conditionElifAnd = new AndKeyword(varAref, generateBoolLiteral(random));
        Expression conditionElifOr = new OrKeyword(varAref, generateBoolLiteral(random));

        Statement printIf = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("if")}));
        Statement printElifAnd = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("elif and")}));
        Statement printElifOr = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("elif or")}));
        Statement printElse = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("else")}));

        List<Statement> bodyIf = List.of(new Statement[] {printIf});
        List<Statement> bodyElifAnd = List.of(new Statement[] {printElifAnd});
        List<Statement> bodyElifOr = List.of(new Statement[] {printElifOr});
        List<Statement> bodyElse = List.of(new Statement[] {printElse});

        IfStatement ifStatement = new IfStatement(conditionIf, bodyIf);
        ElifStatement elifStatementAnd = new ElifStatement(conditionElifAnd, bodyElifAnd);
        ElifStatement elifStatementOr = new ElifStatement(conditionElifOr, bodyElifOr);
        ElseStatement elseStatement = new ElseStatement(bodyElse);
        List<ElifStatement> elifList = List.of(new ElifStatement[] {elifStatementAnd,elifStatementOr});

        Statement conditionalStatement = new IfThenElseStatement(ifStatement,Optional.of(elifList),Optional.of(elseStatement));

        this.program.addStatement(generatePrint);
        this.program.addVariable(varA);
        this.program.addStatement(varAassignment);

        this.program.addStatement(conditionalStatement);

    }

    /**
     * Generate a Class without inheritance and maybe with some attributes
     * @param random
     */
    private void generateClassWithoutInheritance(SourceOfRandomness random) {
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateClassWithoutInheritance")}));
        String className = random.choose(this.classNames);
        while (this.usedClasses.contains(className)) {
            className = random.choose(this.classNames);
        }
        List<Statement> classArguments = generateClassArguments(random);
        MPyClass Aclass =
            new MPyClass(
                className,
                new Reference("__MPyType_Object"),
                List.of(
                    new CBuilder.objects.functions.Function[]{
                        new CBuilder.objects.functions.Function(
                            "print",
                            List.of(
                                new Statement[]{
                                    new Call(
                                        new Reference("print"),
                                        List.of(
                                            new Expression[]{
                                                new Reference("self")
                                            }))
                                }),
                            List.of(new Argument[]{new Argument("self", 0)}),
                            List.of()),
                        new CBuilder.objects.functions.Function(
                            "__init__",
                            classArguments,
                            List.of(new Argument[]{new Argument("self", 0)}),
                            List.of())
                    }),
                new HashMap<>());
        this.program.addStatement(generatePrint);
        this.program.addClass(Aclass);
        this.program.addVariable(new VariableDeclaration(className.toLowerCase()+"Obj"));
        this.program.addStatement(new Assignment(new Reference(className.toLowerCase()+"Obj"), new Call(new Reference(className),List.of())));
        this.program.addStatement(new Call(new AttributeReference("print", new Reference(className.toLowerCase()+"Obj")), List.of()));
        this.usedClasses.add(className);
    }


    /**
     * Generate a Class with Inheritance and maybe some arguments
     * @param random
     */
    private void generateClassWithInheritance(SourceOfRandomness random) {
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateClassWithInheritance")}));
        this.program.addStatement(generatePrint);
        if (!this.usedClasses.isEmpty()) {
            String className = random.choose(this.classNames);
            while (this.usedClasses.contains(className)) {
                className = random.choose(this.classNames);
            }
            String inheritance = random.choose(this.usedClasses);
            List<Statement> classArguments = generateClassArguments(random);
            MPyClass Aclass =
                new MPyClass(
                    className,
                    new Reference(inheritance),
                    List.of(
                        new CBuilder.objects.functions.Function[]{
                            new CBuilder.objects.functions.Function(
                                "print",
                                List.of(
                                    new Statement[]{
                                        new Call(
                                            new Reference("print"),
                                            List.of(
                                                new Expression[]{
                                                    new Reference("self")
                                                }))
                                    }),
                                List.of(new Argument[]{new Argument("self", 0)}),
                                List.of()),
                            new CBuilder.objects.functions.Function(
                                "__init__",
                                classArguments,
                                List.of(new Argument[]{new Argument("self", 0)}),
                                List.of())
                        }),
                    new HashMap<>());
            this.program.addClass(Aclass);
            this.program.addVariable(new VariableDeclaration(className.toLowerCase() + "Obj"));
            this.program.addStatement(new Assignment(new Reference(className.toLowerCase() + "Obj"), new Call(new Reference(className), List.of())));
            this.program.addStatement(new Call(new AttributeReference("print", new Reference(className.toLowerCase() + "Obj")), List.of()));
            this.usedClasses.add(className);
        }
    }

    /**
     * Function for generation of Class Arguments
     * @param random
     * @return
     */
    private List<Statement> generateClassArguments(SourceOfRandomness random) {
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateClassArguments")}));
        this.program.addStatement(generatePrint);
        int max = random.nextInt(0,Integer.MAX_VALUE -1);
        List<Statement> arguments = new ArrayList<>();
        arguments.add(
            new SuperCall(List.of())
        );
        for (int i = 0; i < max; i++) {
            String name = generateString(random);
            AttributeAssignment varAassignment = random.choose(Arrays.<Function<SourceOfRandomness, AttributeAssignment>>asList(
                (r) -> this.generateClassInteger(r,name),
                (r) -> this.generateClassString(r,name),
                (r) -> this.generateClassBool(r,name)
            )).apply(random);
            arguments.add(varAassignment);
        }
        return arguments;
    }

    /**
     * function to generate a function
     * @param random
     */
    private void generateFunction(SourceOfRandomness random) {
        expressionDepth++;
        Statement generatePrint = new Call(this.printRef,List.of(new Expression[] {new StringLiteral("generateFunctions")}));
        this.program.addStatement(generatePrint);
        VariableDeclaration localVarYDec1 = new VariableDeclaration("y");
        Assignment assignYWithX = new Assignment(new Reference("y"), new Reference("x"));
        Call printY = new Call(this.printRef,List.of(new Expression[] {new Reference("y")}));
        Statement returnY = new ReturnStatement(new Reference("y"));

        List<Statement> body = List.of(new Statement[] {assignYWithX,printY,returnY});
        List<Argument> parameterArguments = List.of(new Argument[] {new Argument("x",0)});
        List<VariableDeclaration> localVariables = List.of(new VariableDeclaration[]{localVarYDec1});

        CBuilder.objects.functions.Function func1 = new CBuilder.objects.functions.Function("func1",body,parameterArguments,localVariables);//TODO: func-Counter
        this.program.addFunction(func1);
        VariableDeclaration varA = new VariableDeclaration("a");
        Reference varAref = new Reference("a");
        Map.Entry<String,Assignment> result = random.choose(Arrays.<Function<SourceOfRandomness, Map<String,Assignment> >>asList(
            (r) -> this.generateInteger(r,varAref),
            (r) -> this.generateString(r,varAref),
            (r) -> this.generateBool(r,varAref)
        )).apply(random).entrySet().iterator().next();
        Assignment varAassignment = result.getValue();
        String type = result.getKey();
        Call callFunc = new Call(new Reference("func1"), List.of(new Expression[] {varAref}));
        Call callPrint = new Call(this.printRef,List.of(new Expression[] {callFunc}));
        this.program.addVariable(varA);
        this.program.addStatement(varAassignment);
        this.program.addStatement(callPrint);
        expressionDepth--;
    }

    /**
     * Function to generate a IntLiteral with set boundaries
     * @param random
     * @return
     */
    private IntLiteral generateIntLiteral(SourceOfRandomness random) {
        return new IntLiteral(random.nextInt(-33000,33000));
    }

    /**
     * Generate a String
     * @param random
     * @return
     */
    private String generateString(SourceOfRandomness random) {
        return gen().type(String.class).generate(random, status);
    }

    /**
     * Generate a String Literal
     * @param random
     * @return
     */
    private StringLiteral generateStringLiteral(SourceOfRandomness random) {
        return new StringLiteral(generateString(random));
    }

    /**
     * Generate a bool Literal
     * @param random
     * @return
     */
    private BoolLiteral generateBoolLiteral(SourceOfRandomness random) {
        return new BoolLiteral(random.nextBoolean());
    }

    /**
     * Generate a Map of an typestring and an Intliteral
     * @param random
     * @param var
     * @return
     */
    private Map<String,Assignment> generateInteger(SourceOfRandomness random,Reference var) {
        Map<String,Assignment> result = new LinkedHashMap<>();
        result.put("Integer",new Assignment(var, generateIntLiteral(random)));
        return result;
    }

    /**
     * Generate a AttributeAssignement for an class attribute with type integer
     * @param random
     * @param name
     * @return
     */
    private AttributeAssignment generateClassInteger(SourceOfRandomness random, String name) {
        return new AttributeAssignment(
            new AttributeReference(
                name, new Reference("self")),
            generateIntLiteral(random)
        );
    }

    /**
     * Generate a Map of an typestring and an Stringliteral
     * @param random
     * @param var
     * @return
     */
    private Map<String,Assignment>  generateString(SourceOfRandomness random,Reference var) {
        Map<String,Assignment> result = new LinkedHashMap<>();
        result.put("String",new Assignment(var, generateStringLiteral(random)));
        return result;
    }

    /**
     * Generate a AttributeAssignement for an class attribute with type String
     * @param random
     * @param name
     * @return
     */
    private AttributeAssignment generateClassString(SourceOfRandomness random,String name) {
        return new AttributeAssignment(
            new AttributeReference(
                name, new Reference("self")),
            generateStringLiteral(random)
        );
    }

    /**
     * Generate a Map of an typestring and an Boolliteral
     * @param random
     * @param var
     * @return
     */
    private Map<String,Assignment> generateBool(SourceOfRandomness random,Reference var) {
        Map<String,Assignment> result = new LinkedHashMap<>();
        result.put("Bool",new Assignment(var, generateBoolLiteral(random)));
        return result;
    }

    /**
     * Generate a AttributeAssignement for an class attribute with type bool
     * @param random
     * @param name
     * @return
     */
    private AttributeAssignment generateClassBool(SourceOfRandomness random, String name) {
        return new AttributeAssignment(
            new AttributeReference(
                name, new Reference("self")),
            generateBoolLiteral(random)
        );
    }

    /**
     * Generate Not Expression with given reference
     * @param var
     * @return
     */
    private Expression generateNot(Reference var) {
        return new NotKeyword(var);
    }

    /**
     * Generate Or Expression with given references
     * @param var
     * @param var2
     * @return
     */
    private Expression generateOr(Reference var,Reference var2) {
        return new OrKeyword(var,var2);
    }

    /**
     * Generate And Expression with given references
     * @param var
     * @param var2
     * @return
     */
    private Expression generateAnd(Reference var,Reference var2) {
        return new AndKeyword(var,var2);
    }

    /**
     * Generate an IntLiteral with given positive MaxValue
     * @param random
     * @param max
     * @return Positve IntLiteral
     */
    private IntLiteral generatePositivIntegerWithMax(SourceOfRandomness random,int max) {
        return new IntLiteral(random.nextInt(1,max));
    }
}
