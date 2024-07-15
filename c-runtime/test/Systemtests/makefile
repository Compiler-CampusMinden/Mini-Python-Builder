RUNDECLA := /test/Systemtests/DeclarationAndAssignment/TestDeclarationAndAssignment.out
RUNARITOPS := /test/Systemtests/ArithmeticOps/TestArithmeticOps.out
RUNCOMPOPS := /test/Systemtests/ComparisonOps/TestComparisonOps.out
RUNGEOP := /test/Systemtests/ComparisonOps/TestGreaterEqualOp.out
RUNGTOP := /test/Systemtests/ComparisonOps/TestGreaterOp.out
RUNLEOP := /test/Systemtests/ComparisonOps/TestLessEqualOp.out
RUNLTOP := /test/Systemtests/ComparisonOps/TestLessOp.out
RUNLOGOPS := /test/Systemtests/LogicalOps/TestLogicalOps.out
RUNIFST := /test/Systemtests/Conditions/TestIfStatement.out
RUNELST := /test/Systemtests/Conditions/TestElseStatement.out
RUNELIFST := /test/Systemtests/Conditions/TestElifStatement.out
RUNWHILEST := /test/Systemtests/Conditions/TestWhileStatement.out
RUNCASTINT := /test/Systemtests/BuiltInFunctions/TestCastInt.out
RUNCASTSTRING := /test/Systemtests/BuiltInFunctions/TestCastString.out
RUNID := /test/Systemtests/BuiltInFunctions/TestId.out
RUNPRINT := /test/Systemtests/BuiltInFunctions/TestPrint.out
RUNTYPE := /test/Systemtests/BuiltInFunctions/TestType.out
RUNFUNPARAMS := /test/Systemtests/FunctionCalls/TestFunctionParams.out
RUNFUNPARAMSTHROWS := /test/Systemtests/FunctionCalls/TestFunctionParamsThrows.out
RUNFUNBODY := /test/Systemtests/FunctionCalls/TestFunctionBody.out
RUNFUNRET := /test/Systemtests/FunctionCalls/TestFunctionReturn.out
RUNFUNREC := /test/Systemtests/FunctionCalls/TestFunctionRecursion.out
RUNCLASSINIT := /test/Systemtests/Classes/TestClassInit.out
RUNCLASSINITTHROWS := /test/Systemtests/Classes/TestClassInitThrows.out
RUNCLASSMEMBERS := /test/Systemtests/Classes/TestClassMembers.out
RUNCLASSINHERITANCE := /test/Systemtests/Classes/TestClassInheritance.out

CXXFLAGS ?= -std=c++14

.PHONY : all run echo test_declaration_and_assignment test_arithmetic_ops test_comparison_ops test_greater_equal_op test_greater_op test_less_equal_op test_less_op test_logical_ops test_if_statement test_else_statement test_elif_statement test_while_statement test_cast_int test_cast_string test_id test_print test_type test_function_params test_function_params_throws test_function_body test_function_return test_function_recursion test_class_init test_class_init_throws test_class_members test_class_inheritance clean

all: test_declaration_and_assignment test_arithmetic_ops test_comparison_ops test_greater_equal_op test_greater_op test_less_equal_op test_less_op test_logical_ops test_if_statement test_else_statement test_elif_statement test_while_statement test_cast_int test_cast_string test_print test_type test_function_params test_function_params_throws test_function_body test_function_return test_function_recursion test_class_init test_class_init_throws test_class_members test_class_inheritance run

run:
	.$(RUNDECLA)
	.$(RUNARITOPS)
	.$(RUNCOMPOPS)
	.$(RUNGEOP)
	.$(RUNGTOP)
	.$(RUNLEOP)
	.$(RUNLTOP)
	.$(RUNLOGOPS)
	.$(RUNIFST)
	.$(RUNELST)
	.$(RUNELIFST)
	.$(RUNWHILEST)
	.$(RUNCASTINT)
	.$(RUNCASTSTRING)
	.$(RUNPRINT)
	.$(RUNTYPE)
	.$(RUNFUNPARAMS)
	.$(RUNFUNPARAMSTHROWS)
	.$(RUNFUNBODY)
	.$(RUNFUNRET)
	.$(RUNFUNREC)
	.$(RUNCLASSINIT)
	.$(RUNCLASSINITTHROWS)
	.$(RUNCLASSMEMBERS)
	.$(RUNCLASSINHERITANCE)

echo:
	@echo ${C_FILES}
	@echo ${TESTDIR}
	@echo ${RUN}

test_declaration_and_assignment:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/DeclarationAndAssignment/TestDeclarationAndAssignment.out -w -fpermissive $(TESTDIR)/Systemtests/DeclarationAndAssignment/TestDeclarationAndAssignment.c* $(C_FILES_TEST)

test_arithmetic_ops:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ArithmeticOps/TestArithmeticOps.out -w -fpermissive $(TESTDIR)/Systemtests/ArithmeticOps/TestArithmeticOps.c* $(C_FILES_TEST)

test_comparison_ops:
	g++-13 --version
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ComparisonOps/TestComparisonOps.out -w -fpermissive $(TESTDIR)/Systemtests/ComparisonOps/TestComparisonOps.c* $(C_FILES_TEST)

test_greater_equal_op:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ComparisonOps/TestGreaterEqualOp.out -w -fpermissive $(TESTDIR)/Systemtests/ComparisonOps/TestGreaterEqualOp.c* $(C_FILES_TEST)

test_greater_op:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ComparisonOps/TestGreaterOp.out -w -fpermissive $(TESTDIR)/Systemtests/ComparisonOps/TestGreaterOp.c* $(C_FILES_TEST)

test_less_equal_op:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ComparisonOps/TestLessEqualOp.out -w -fpermissive $(TESTDIR)/Systemtests/ComparisonOps/TestLessEqualOp.c* $(C_FILES_TEST)

test_less_op:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ComparisonOps/TestLessOp.out -w -fpermissive $(TESTDIR)/Systemtests/ComparisonOps/TestLessOp.c* $(C_FILES_TEST)

test_logical_ops:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/LogicalOps/TestLogicalOps.out -w -fpermissive $(TESTDIR)/Systemtests/LogicalOps/TestLogicalOps.c* $(C_FILES_TEST)

test_if_statement:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Conditions/TestIfStatement.out -w -fpermissive $(TESTDIR)/Systemtests/Conditions/TestIfStatement.c* $(C_FILES_TEST)

test_else_statement:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Conditions/TestElseStatement.out -w -fpermissive $(TESTDIR)/Systemtests/Conditions/TestElseStatement.c* $(C_FILES_TEST)

test_elif_statement:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Conditions/TestElifStatement.out -w -fpermissive $(TESTDIR)/Systemtests/Conditions/TestElifStatement.c* $(C_FILES_TEST)

test_while_statement:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Conditions/TestWhileStatement.out -w -fpermissive $(TESTDIR)/Systemtests/Conditions/TestWhileStatement.c* $(C_FILES_TEST)

test_cast_int:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/BuiltInFunctions/TestCastInt.out -w -fpermissive $(TESTDIR)/Systemtests/BuiltInFunctions/TestCastInt.c* $(C_FILES_TEST)

test_cast_string:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/BuiltInFunctions/TestCastString.out -w -fpermissive $(TESTDIR)/Systemtests/BuiltInFunctions/TestCastString.c* $(C_FILES_TEST)

test_id:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/BuiltInFunctions/TestId.out -w -fpermissive $(TESTDIR)/Systemtests/BuiltInFunctions/TestId.c* $(C_FILES_TEST)

test_print:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/BuiltInFunctions/TestPrint.out -w -fpermissive $(TESTDIR)/Systemtests/BuiltInFunctions/TestPrint.c* $(C_FILES_TEST)

test_type:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/BuiltInFunctions/TestType.out -w -fpermissive $(TESTDIR)/Systemtests/BuiltInFunctions/TestType.c* $(C_FILES_TEST)

test_function_params:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionParams.out -w -fpermissive $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionParams.c* $(C_FILES_TEST)

test_function_params_throws:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionParamsThrows.out -w -fpermissive $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionParamsThrows.c* $(C_FILES_TEST)

test_function_body:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionBody.out -w -fpermissive $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionBody.c* $(C_FILES_TEST)

test_function_return:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionReturn.out -w -fpermissive $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionReturn.c* $(C_FILES_TEST)

test_function_recursion:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionRecursion.out -w -fpermissive $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionRecursion.c* $(C_FILES_TEST)

test_class_init:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Classes/TestClassInit.out -w -fpermissive $(TESTDIR)/Systemtests/Classes/TestClassInit.c* $(C_FILES_TEST)

test_class_init_throws:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Classes/TestClassInitThrows.out -w -fpermissive $(TESTDIR)/Systemtests/Classes/TestClassInitThrows.c* $(C_FILES_TEST)

test_class_members:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Classes/TestClassMembers.out -w -fpermissive $(TESTDIR)/Systemtests/Classes/TestClassMembers.c* $(C_FILES_TEST)

test_class_inheritance:
	g++-13 --version
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Classes/TestClassInheritance.out -w -fpermissive $(TESTDIR)/Systemtests/Classes/TestClassInheritance.c* $(C_FILES_TEST)

test_wuppie:
	g++-13 -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/wuppie.out -w -fpermissive $(TESTDIR)/Systemtests/wuppie.c* $(C_FILES_TEST)


clean:
	$(RM) -r $(TESTDIR)/*.out
	$(RM) -r $(TESTDIR)/*/*/*.out
