package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOAndExpression;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOBracketedExpression;
import hu.bme.aut.oogen.OOEqualsExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOLogicalLiteral;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OONotEqualsExpression;
import hu.bme.aut.oogen.OONotExpression;
import hu.bme.aut.oogen.OOOrExpression;
import hu.bme.aut.oogen.OOReturn;
import hu.bme.aut.oogen.OOTernaryOperator;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableDeclarationList;

public class IntegerLiteralToBooleanTransformationTest extends AbstractTransformationTest {
    @Test
    public void twoOperandAndExpression_integerLiteralShouldTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	return b && 0;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOReturn returnStatement = (OOReturn) TransformUtil.getMethodFromClass(getDefaultClass(model), "someFunction")
		.getStatements().get(0);
	OOExpression returnedExpression = returnStatement.getReturnedExpresssion();
	Assert.assertTrue(returnedExpression instanceof OOAndExpression);
	OOExpression rhs = ((OOAndExpression) returnedExpression).getRightSide();
	Assert.assertTrue(rhs instanceof OOLogicalLiteral);
	Assert.assertFalse(((OOLogicalLiteral) rhs).isValue());
    }

    @Test
    public void twoOperandOrExpression_integerLiteralShouldTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	return b || -128;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOReturn returnStatement = (OOReturn) TransformUtil.getMethodFromClass(getDefaultClass(model), "someFunction")
		.getStatements().get(0);
	OOExpression returnedExpression = returnStatement.getReturnedExpresssion();
	Assert.assertTrue(returnedExpression instanceof OOOrExpression);
	OOExpression rhs = ((OOOrExpression) returnedExpression).getRightSide();
	Assert.assertTrue(rhs instanceof OOLogicalLiteral);
	Assert.assertTrue(((OOLogicalLiteral) rhs).isValue());
    }

    @Test
    public void oneOperandNotExpression_integerLiteralShouldTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	return !0;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOReturn returnStatement = (OOReturn) TransformUtil.getMethodFromClass(getDefaultClass(model), "someFunction")
		.getStatements().get(0);
	OOExpression returnedExpression = returnStatement.getReturnedExpresssion();
	Assert.assertTrue(returnedExpression instanceof OONotExpression);
	OOExpression operand = ((OONotExpression) returnedExpression).getOperand();
	Assert.assertTrue(operand instanceof OOLogicalLiteral);
	Assert.assertFalse(((OOLogicalLiteral) operand).isValue());
    }

    @Test
    public void bracketedExpression_integerLiteralsShouldTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	return b || (0 && 1);");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOReturn returnStatement = (OOReturn) TransformUtil.getMethodFromClass(getDefaultClass(model), "someFunction")
		.getStatements().get(0);
	OOExpression returnedExpression = returnStatement.getReturnedExpresssion();
	Assert.assertTrue(returnedExpression instanceof OOOrExpression);
	OOExpression rhs = ((OOOrExpression) returnedExpression).getRightSide();
	Assert.assertTrue(rhs instanceof OOBracketedExpression);
	OOAndExpression andExpression = (OOAndExpression) ((OOBracketedExpression) rhs).getOperand();
	Assert.assertFalse(((OOLogicalLiteral) andExpression.getLeftSide()).isValue());
	Assert.assertTrue(((OOLogicalLiteral) andExpression.getRightSide()).isValue());
    }

    @Test
    public void ternaryOperator_integerLiteralsShouldTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	return b != 0 ? true : false;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOReturn returnStatement = (OOReturn) TransformUtil.getMethodFromClass(getDefaultClass(model), "someFunction")
		.getStatements().get(0);
	OOExpression returnedExpression = returnStatement.getReturnedExpresssion();
	Assert.assertTrue(returnedExpression instanceof OOTernaryOperator);
	OOTernaryOperator ternaryOperator = (OOTernaryOperator) returnedExpression;
	OOExpression condition = ternaryOperator.getCondition();
	Assert.assertTrue(condition instanceof OONotEqualsExpression);
	Assert.assertFalse(((OOLogicalLiteral) ((OONotEqualsExpression) condition).getRightSide()).isValue());

	Assert.assertTrue(((OOLogicalLiteral) ternaryOperator.getPositiveBranch()).isValue());
	Assert.assertFalse(((OOLogicalLiteral) ternaryOperator.getNegativeBranch()).isValue());

    }

    @Test
    public void booleanAssignmentExpression_integerLiteralShouldTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	b = 0;");
	sourceCode.append("	return b;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOAssignmentExpression assignment = (OOAssignmentExpression) TransformUtil
		.getMethodFromClass(getDefaultClass(model), "someFunction").getStatements().get(0);
	Assert.assertFalse(((OOLogicalLiteral) assignment.getRightSide()).isValue());
    }

    @Test
    public void nonBooleanAssignmentExpression_integerLiteralShouldNotTansformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	int x;");
	sourceCode.append("	x = 0;");
	sourceCode.append("	return b;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOAssignmentExpression assignment = (OOAssignmentExpression) TransformUtil
		.getMethodFromClass(getDefaultClass(model), "someFunction").getStatements().get(1);
	Assert.assertTrue(assignment.getRightSide() instanceof OOIntegerLiteral);
    }

    @Test
    public void booleanEqualsExpression_integerLiteralShouldTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	return b == 0;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOReturn returnStatement = (OOReturn) TransformUtil.getMethodFromClass(getDefaultClass(model), "someFunction")
		.getStatements().get(0);
	OOEqualsExpression equalsExpression = (OOEqualsExpression) (returnStatement.getReturnedExpresssion());
	Assert.assertFalse(((OOLogicalLiteral) equalsExpression.getRightSide()).isValue());
    }

    @Test
    public void nonBooleanEqualsExpression_integerLiteralShouldNotTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	int x;");
	sourceCode.append("	return x == 0;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOReturn returnStatement = (OOReturn) TransformUtil.getMethodFromClass(getDefaultClass(model), "someFunction")
		.getStatements().get(1);
	OOEqualsExpression equalsExpression = (OOEqualsExpression) (returnStatement.getReturnedExpresssion());
	Assert.assertTrue(equalsExpression.getRightSide() instanceof OOIntegerLiteral);
    }

    @Test
    public void booleanNotEqualsExpression_integerLiteralShouldTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	return b != 0;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOReturn returnStatement = (OOReturn) TransformUtil.getMethodFromClass(getDefaultClass(model), "someFunction")
		.getStatements().get(0);
	OONotEqualsExpression notEqualsExpression = (OONotEqualsExpression) (returnStatement.getReturnedExpresssion());
	Assert.assertFalse(((OOLogicalLiteral) notEqualsExpression.getRightSide()).isValue());
    }

    @Test
    public void nonBooleanNotEqualsExpression_integerLiteralShouldNotTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	int x;");
	sourceCode.append("	return x != 0;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOReturn returnStatement = (OOReturn) TransformUtil.getMethodFromClass(getDefaultClass(model), "someFunction")
		.getStatements().get(1);
	OONotEqualsExpression notEqualsExpression = (OONotEqualsExpression) (returnStatement.getReturnedExpresssion());
	Assert.assertTrue(notEqualsExpression.getRightSide() instanceof OOIntegerLiteral);
    }

    @Test
    public void booleanVariableDeclarationList_integerLiteralsShouldTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction(bool b) {");
	sourceCode.append("	bool b1, b2 = 0, b3 = 1024;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOVariableDeclarationList declarationList = (OOVariableDeclarationList) TransformUtil
		.getMethodFromClass(getDefaultClass(model), "someFunction").getStatements().get(0);
	for (int i = 0; i < declarationList.getVariableDeclarations().size(); i++) {
	    OOVariable variableDeclaration = declarationList.getVariableDeclarations().get(i);
	    Assert.assertTrue(variableDeclaration.getType().getBaseType() == OOBaseType.BOOLEAN);
	    switch (i) {
	    case 0:
		Assert.assertNull(variableDeclaration.getInitializerExpression());
		break;
	    case 1:
		Assert.assertFalse(((OOLogicalLiteral) variableDeclaration.getInitializerExpression()).isValue());
		break;
	    case 2:
		Assert.assertTrue(((OOLogicalLiteral) variableDeclaration.getInitializerExpression()).isValue());
		break;
	    default:
		Assert.fail("Unexpected declaration index: " + i);
	    }
	}
    }

    @Test
    public void booleanVariableMemberSetterCall_integerLiteralsShouldTransformToBoolean() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef struct S {bool b;} S;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	S s;");
	sourceCode.append("	s.b = 2;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOFunctionCallExpression setterCall = (OOFunctionCallExpression) (TransformUtil.getMethodFromClass(getDefaultClass(model), "someFunction")
		.getStatements().get(1));
	OOLogicalLiteral argumentLiteral = (OOLogicalLiteral) setterCall.getArgumentExpressions().get(0);
	Assert.assertTrue(argumentLiteral.isValue());
    }
}
