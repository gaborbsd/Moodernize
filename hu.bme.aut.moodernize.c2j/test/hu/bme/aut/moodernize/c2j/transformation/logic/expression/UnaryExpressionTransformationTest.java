package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOAdditionExpression;
import hu.bme.aut.oogen.OOAndExpression;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOBitWiseComplement;
import hu.bme.aut.oogen.OOBracketedExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOGreaterThanExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOLessThanExpression;
import hu.bme.aut.oogen.OOLogicalLiteral;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOMinusExpression;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOMultiplicationExpression;
import hu.bme.aut.oogen.OONotExpression;
import hu.bme.aut.oogen.OOOrExpression;
import hu.bme.aut.oogen.OOPlusExpression;
import hu.bme.aut.oogen.OOPostfixDecrementExpression;
import hu.bme.aut.oogen.OOPostfixIncrementExpression;
import hu.bme.aut.oogen.OOPrefixDecrementExpression;
import hu.bme.aut.oogen.OOPrefixIncrementExpression;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOVariableDeclarationList;

public class UnaryExpressionTransformationTest extends AbstractTransformationTest {
    @Test
    public void operatorMinus_shouldTransformToOOMinusExpression() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("int someFunction() {");
	sourceCode.append("	globalInt = -globalInt;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOStatement statement = someFunction.getStatements().get(0);
	Assert.assertTrue(((OOAssignmentExpression) statement).getRightSide() instanceof OOMinusExpression);
    }

    @Test
    public void operatorPlus_shouldTransformToOOPlusExpression() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("int someFunction() {");
	sourceCode.append("	globalInt = +globalInt;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOStatement statement = someFunction.getStatements().get(0);
	Assert.assertTrue(((OOAssignmentExpression) statement).getRightSide() instanceof OOPlusExpression);
    }

    @Test
    public void operatorPostfixDecrement_shouldTransformToOOPostfixDecrementExpression() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt--;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOStatement statement = someFunction.getStatements().get(0);
	Assert.assertTrue(statement instanceof OOPostfixDecrementExpression);
    }

    @Test
    public void operatorPostfixIncrement_shouldTransformToOOPostfixIncrementExpression() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt++;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOStatement statement = someFunction.getStatements().get(0);
	Assert.assertTrue(statement instanceof OOPostfixIncrementExpression);
    }

    @Test
    public void operatorPrefixDecrement_shouldTransformToOOPrefixDecrementExpression() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	--globalInt;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOStatement statement = someFunction.getStatements().get(0);
	Assert.assertTrue(statement instanceof OOPrefixDecrementExpression);
    }

    @Test
    public void operatorPrefixIncrement_shouldTransformToOOPrefixIncrementExpression() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	++globalInt;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOStatement statement = someFunction.getStatements().get(0);
	Assert.assertTrue(statement instanceof OOPrefixIncrementExpression);
    }

    @Test
    public void operatorTilde_shouldTransformToOOBitwiseComplement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalint = ~globalInt;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOStatement statement = someFunction.getStatements().get(0);
	Assert.assertTrue(((OOAssignmentExpression) statement).getRightSide() instanceof OOBitWiseComplement);
    }

    @Test
    public void operatorBraces_shouldTransformToOOBracketedExpression() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalint =  2 * (globalint + 2);");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOMultiplicationExpression rhs = (OOMultiplicationExpression) ((OOAssignmentExpression) someFunction
		.getStatements().get(0)).getRightSide();
	Assert.assertTrue(rhs.getLeftSide() instanceof OOIntegerLiteral);
	OOBracketedExpression bracketedExpression = (OOBracketedExpression) rhs.getRightSide();
	Assert.assertTrue(bracketedExpression.getOperand() instanceof OOAdditionExpression);
    }

    @Test
    public void operatorBracesOnLogicalExpressions_shouldTransformToOOCorrespondingExpression() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	bool b = !((globalInt > 0) || !(globalInt < 0 && 1));");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression notExpression = ((OOVariableDeclarationList) someFunction.getStatements().get(0))
		.getVariableDeclarations().get(0).getInitializerExpression();
	Assert.assertTrue(notExpression instanceof OONotExpression);

	OOBracketedExpression outerBraces = (OOBracketedExpression) ((OONotExpression) notExpression).getOperand();
	Assert.assertTrue(outerBraces.getOperand() instanceof OOOrExpression);

	OOOrExpression orExpression = (OOOrExpression) outerBraces.getOperand();
	OOExpression lhs = orExpression.getLeftSide();
	OOExpression rhs = orExpression.getRightSide();
	Assert.assertTrue(lhs instanceof OOBracketedExpression);
	Assert.assertTrue(rhs instanceof OONotExpression);

	Assert.assertTrue(((OOBracketedExpression) lhs).getOperand() instanceof OOGreaterThanExpression);
	OOExpression rhsInnerOperand = ((OOBracketedExpression) (((OONotExpression) rhs).getOperand())).getOperand();
	Assert.assertTrue(rhsInnerOperand instanceof OOAndExpression);

	OOExpression andLhs = ((OOAndExpression) rhsInnerOperand).getLeftSide();
	OOExpression andrhs = ((OOAndExpression) rhsInnerOperand).getRightSide();
	Assert.assertTrue(andLhs instanceof OOLessThanExpression);
	Assert.assertTrue(andrhs instanceof OOLogicalLiteral);
    }
}
