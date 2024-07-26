UNAME := $(shell uname)

CC = test

ifeq ($(UNAME), Darwin)
	CC = g++-13
endif
ifeq ($(UNAME), Linux)
	CC = g++
endif

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

.PHONY : all run echo test_declaration_and_assignment test_arithmetic_ops test_comparison_ops test_greater_equal_op test_greater_op test_less_equal_op test_less_op test_logical_ops test_if_statement test_else_statement test_elif_statement test_while_statement test_cast_int test_cast_string test_id test_print test_type test_function_body test_function_return test_function_recursion test_class_init test_class_members test_class_inheritance clean
#Disabled tests: test_function_params, test_function_params_throws, test_class_init_throws
#To Enable add to all target, .PHONY and remove comment from run target!
all: test_declaration_and_assignment test_arithmetic_ops test_comparison_ops test_greater_equal_op test_greater_op test_less_equal_op test_less_op test_logical_ops test_if_statement test_else_statement test_elif_statement test_while_statement test_cast_int test_cast_string test_print test_type test_function_body test_function_return test_function_recursion test_class_init test_class_members test_class_inheritance run

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
	#.$(RUNFUNPARAMS)
	#.$(RUNFUNPARAMSTHROWS)
	.$(RUNFUNBODY)
	.$(RUNFUNRET)
	.$(RUNFUNREC)
	.$(RUNCLASSINIT)
	#.$(RUNCLASSINITTHROWS)
	.$(RUNCLASSMEMBERS)
	.$(RUNCLASSINHERITANCE)

echo:
	@echo ${C_FILES}
	@echo ${TESTDIR}
	@echo ${RUN}
	@echo $(UNAME)
	@echo $(CC)

test_declaration_and_assignment:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/DeclarationAndAssignment/TestDeclarationAndAssignment.out -w -fpermissive $(TESTDIR)/Systemtests/DeclarationAndAssignment/TestDeclarationAndAssignment.c* $(C_FILES_TEST) $(C_FILES)

test_arithmetic_ops:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ArithmeticOps/TestArithmeticOps.out -w -fpermissive $(TESTDIR)/Systemtests/ArithmeticOps/TestArithmeticOps.c* $(C_FILES_TEST) $(C_FILES)

test_comparison_ops:
	$(CC) --version
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ComparisonOps/TestComparisonOps.out -w -fpermissive $(TESTDIR)/Systemtests/ComparisonOps/TestComparisonOps.c* $(C_FILES_TEST) $(C_FILES)

test_greater_equal_op:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ComparisonOps/TestGreaterEqualOp.out -w -fpermissive $(TESTDIR)/Systemtests/ComparisonOps/TestGreaterEqualOp.c* $(C_FILES_TEST) $(C_FILES)

test_greater_op:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ComparisonOps/TestGreaterOp.out -w -fpermissive $(TESTDIR)/Systemtests/ComparisonOps/TestGreaterOp.c* $(C_FILES_TEST) $(C_FILES)

test_less_equal_op:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ComparisonOps/TestLessEqualOp.out -w -fpermissive $(TESTDIR)/Systemtests/ComparisonOps/TestLessEqualOp.c* $(C_FILES_TEST) $(C_FILES)

test_less_op:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/ComparisonOps/TestLessOp.out -w -fpermissive $(TESTDIR)/Systemtests/ComparisonOps/TestLessOp.c* $(C_FILES_TEST) $(C_FILES)

test_logical_ops:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/LogicalOps/TestLogicalOps.out -w -fpermissive $(TESTDIR)/Systemtests/LogicalOps/TestLogicalOps.c* $(C_FILES_TEST) $(C_FILES)

test_if_statement:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Conditions/TestIfStatement.out -w -fpermissive $(TESTDIR)/Systemtests/Conditions/TestIfStatement.c* $(C_FILES_TEST) $(C_FILES)

test_else_statement:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Conditions/TestElseStatement.out -w -fpermissive $(TESTDIR)/Systemtests/Conditions/TestElseStatement.c* $(C_FILES_TEST) $(C_FILES)

test_elif_statement:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Conditions/TestElifStatement.out -w -fpermissive $(TESTDIR)/Systemtests/Conditions/TestElifStatement.c* $(C_FILES_TEST) $(C_FILES)

test_while_statement:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Conditions/TestWhileStatement.out -w -fpermissive $(TESTDIR)/Systemtests/Conditions/TestWhileStatement.c* $(C_FILES_TEST) $(C_FILES)

test_cast_int:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/BuiltInFunctions/TestCastInt.out -w -fpermissive $(TESTDIR)/Systemtests/BuiltInFunctions/TestCastInt.c* $(C_FILES_TEST) $(C_FILES)

test_cast_string:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/BuiltInFunctions/TestCastString.out -w -fpermissive $(TESTDIR)/Systemtests/BuiltInFunctions/TestCastString.c* $(C_FILES_TEST) $(C_FILES)

test_id:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/BuiltInFunctions/TestId.out -w -fpermissive $(TESTDIR)/Systemtests/BuiltInFunctions/TestId.c* $(C_FILES_TEST) $(C_FILES)

test_print:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/BuiltInFunctions/TestPrint.out -w -fpermissive $(TESTDIR)/Systemtests/BuiltInFunctions/TestPrint.c* $(C_FILES_TEST) $(C_FILES)

test_type:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/BuiltInFunctions/TestType.out -w -fpermissive $(TESTDIR)/Systemtests/BuiltInFunctions/TestType.c* $(C_FILES_TEST) $(C_FILES)

#test_function_params:
	#$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionParams.out -w -fpermissive $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionParams.c* $(C_FILES_TEST) $(C_FILES)

test_function_params_throws:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionParamsThrows.out -w -fpermissive $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionParamsThrows.c* $(C_FILES_TEST) $(C_FILES)

test_function_body:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionBody.out -w -fpermissive $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionBody.c* $(C_FILES_TEST) $(C_FILES)

test_function_return:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionReturn.out -w -fpermissive $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionReturn.c* $(C_FILES_TEST) $(C_FILES)

test_function_recursion:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionRecursion.out -w -fpermissive $(TESTDIR)/Systemtests/FunctionCalls/TestFunctionRecursion.c* $(C_FILES_TEST) $(C_FILES)

test_class_init:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Classes/TestClassInit.out -w -fpermissive $(TESTDIR)/Systemtests/Classes/TestClassInit.c* $(C_FILES_TEST) $(C_FILES)

test_class_init_throws:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Classes/TestClassInitThrows.out -w -fpermissive $(TESTDIR)/Systemtests/Classes/TestClassInitThrows.c* $(C_FILES_TEST) $(C_FILES)

test_class_members:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Classes/TestClassMembers.out -w -fpermissive $(TESTDIR)/Systemtests/Classes/TestClassMembers.c* $(C_FILES_TEST) $(C_FILES)

test_class_inheritance:
	$(CC) --version
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/Classes/TestClassInheritance.out -w -fpermissive $(TESTDIR)/Systemtests/Classes/TestClassInheritance.c* $(C_FILES_TEST) $(C_FILES)

test_wuppie:
	$(CC) -I $(INCLUDEDIR) -I $(INCLUDEDIRTEST) -o $(TESTDIR)/Systemtests/wuppie.out -w -fpermissive $(TESTDIR)/Systemtests/wuppie.c* $(C_FILES_TEST) $(C_FILES)

clean:
	$(RM) -r $(TESTDIR)/*.out
	$(RM) -r $(TESTDIR)/*/*/*.out
