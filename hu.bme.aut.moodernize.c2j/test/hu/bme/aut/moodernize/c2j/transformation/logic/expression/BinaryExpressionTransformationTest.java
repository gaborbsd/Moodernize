package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOAdditionExpression;
import hu.bme.aut.oogen.OOAndExpression;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOBitWiseLeftShift;
import hu.bme.aut.oogen.OOBitWiseRightShift;
import hu.bme.aut.oogen.OOBitwiseAndExpression;
import hu.bme.aut.oogen.OOBitwiseOrExpression;
import hu.bme.aut.oogen.OOBitwiseXorExpression;
import hu.bme.aut.oogen.OODivisionExpression;
import hu.bme.aut.oogen.OOEqualsExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFieldReferenceExpression;
import hu.bme.aut.oogen.OOGreaterEqualsExpression;
import hu.bme.aut.oogen.OOGreaterThanExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOLessEqualsExpression;
import hu.bme.aut.oogen.OOLessThanExpression;
import hu.bme.aut.oogen.OOLogicalLiteral;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOModuloExpression;
import hu.bme.aut.oogen.OOMultiplicationExpression;
import hu.bme.aut.oogen.OONotEqualsExpression;
import hu.bme.aut.oogen.OOOrExpression;
import hu.bme.aut.oogen.OOReturn;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOSubtractionExpression;
import hu.bme.aut.oogen.OOVariableReferenceExpression;

public class BinaryExpressionTransformationTest extends AbstractTransformationTest {
    @Test
    public void operatorPlus_shouldTransformToOOAddition() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = 1 + 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOAssignmentExpression) someFunction.getStatements().get(0)).getRightSide();
	Assert.assertTrue(expression instanceof OOAdditionExpression);
	Assert.assertTrue(((OOAdditionExpression) expression).getLeftSide() instanceof OOIntegerLiteral);
	Assert.assertTrue(((OOAdditionExpression) expression).getRightSide() instanceof OOIntegerLiteral);
    }

    @Test
    public void operatorMinus_shouldTransformToOOSubstraction() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = 1 - 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOAssignmentExpression) someFunction.getStatements().get(0))
		.getRightSide();
	Assert.assertTrue(expression instanceof OOSubtractionExpression);
	Assert.assertTrue(((OOSubtractionExpression) expression).getLeftSide() instanceof OOIntegerLiteral);
	Assert.assertTrue(
		((OOSubtractionExpression) expression).getRightSide() instanceof OOIntegerLiteral);
    }

    @Test
    public void operatorAssign_shouldTransformToOOAssignment() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = 1 - 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOStatement expression = someFunction.getStatements().get(0);
	Assert.assertTrue(
		((OOAssignmentExpression) expression).getLeftSide() instanceof OOFieldReferenceExpression);
	Assert.assertTrue(
		((OOAssignmentExpression) expression).getRightSide() instanceof OOSubtractionExpression);
    }

    @Test
    public void operatorMultiply_shouldTransformToOOMultiplication() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = 1 * 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression multiplicationExpression = ((OOAssignmentExpression) someFunction.getStatements().get(0))
		.getRightSide();
	Assert.assertTrue(multiplicationExpression instanceof OOMultiplicationExpression);
	Assert.assertTrue(
		((OOMultiplicationExpression) multiplicationExpression).getLeftSide() instanceof OOIntegerLiteral);
	Assert.assertTrue(
		((OOMultiplicationExpression) multiplicationExpression).getRightSide() instanceof OOIntegerLiteral);
    }

    @Test
    public void operatorDivide_shouldTransformToOODivision() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = 1 / 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOAssignmentExpression) someFunction.getStatements().get(0)).getRightSide();
	Assert.assertTrue(expression instanceof OODivisionExpression);
	Assert.assertTrue(((OODivisionExpression) expression).getLeftSide() instanceof OOIntegerLiteral);
	Assert.assertTrue(((OODivisionExpression) expression).getRightSide() instanceof OOIntegerLiteral);
    }
    
    @Test
    public void operatorModulo_shouldTransformToOOModulo() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = globalint % 12;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOAssignmentExpression) someFunction.getStatements().get(0)).getRightSide();
	Assert.assertTrue(expression instanceof OOModuloExpression);
	Assert.assertTrue(((OOModuloExpression) expression).getLeftSide() instanceof OOVariableReferenceExpression);
	Assert.assertTrue(((OOModuloExpression) expression).getRightSide() instanceof OOIntegerLiteral);
    }

    @Test
    public void operatorLogicalAnd_shouldTransformToOOAnd() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool globalBool;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalBool = true && false;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOAssignmentExpression) someFunction.getStatements().get(0)).getRightSide();
	Assert.assertTrue(expression instanceof OOAndExpression);
	Assert.assertTrue(((OOAndExpression) expression).getLeftSide() instanceof OOLogicalLiteral);
	Assert.assertTrue(((OOAndExpression) expression).getRightSide() instanceof OOLogicalLiteral);
    }

    @Test
    public void operatorLogicalOr_shouldTransformToOOOr() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool globalBool;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalBool = true || false;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOAssignmentExpression) someFunction.getStatements().get(0)).getRightSide();
	Assert.assertTrue(expression instanceof OOOrExpression);
	Assert.assertTrue(((OOOrExpression) expression).getLeftSide() instanceof OOLogicalLiteral);
	Assert.assertTrue(((OOOrExpression) expression).getRightSide() instanceof OOLogicalLiteral);
    }

    @Test
    public void operatorBinaryAnd_shouldTransformToOOBitwiseAnd() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("unsigned int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = globalInt & 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOAssignmentExpression) someFunction.getStatements().get(0)).getRightSide();
	Assert.assertTrue(expression instanceof OOBitwiseAndExpression);
	Assert.assertTrue(((OOBitwiseAndExpression) expression).getLeftSide() instanceof OOFieldReferenceExpression);
	Assert.assertTrue(((OOBitwiseAndExpression) expression).getRightSide() instanceof OOIntegerLiteral);
    }

    @Test
    public void operatorBinaryOr_shouldTransformToOOBitwiseOr() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("unsigned int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = globalInt | 0;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOAssignmentExpression) someFunction.getStatements().get(0)).getRightSide();
	Assert.assertTrue(expression instanceof OOBitwiseOrExpression);
	Assert.assertTrue(((OOBitwiseOrExpression) expression).getLeftSide() instanceof OOFieldReferenceExpression);
	Assert.assertTrue(((OOBitwiseOrExpression) expression).getRightSide() instanceof OOIntegerLiteral);
    }

    @Test
    public void operatorBinaryXor_shouldTransformToOOBitwiseXor() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("unsigned int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = globalInt ^ 0;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOAssignmentExpression) someFunction.getStatements().get(0)).getRightSide();
	Assert.assertTrue(expression instanceof OOBitwiseXorExpression);
	Assert.assertTrue(((OOBitwiseXorExpression) expression).getLeftSide() instanceof OOFieldReferenceExpression);
	Assert.assertTrue(((OOBitwiseXorExpression) expression).getRightSide() instanceof OOIntegerLiteral);
    }

    @Test
    public void operatorShiftLeft_shouldTransformToOOBitwiseLeftShift() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("unsigned int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = globalInt << 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOAssignmentExpression) someFunction.getStatements().get(0)).getRightSide();
	Assert.assertTrue(expression instanceof OOBitWiseLeftShift);
	Assert.assertTrue(((OOBitWiseLeftShift) expression).getLeftSide() instanceof OOFieldReferenceExpression);
	Assert.assertTrue(((OOBitWiseLeftShift) expression).getRightSide() instanceof OOIntegerLiteral);
    }

    @Test
    public void operatorShiftRight_shouldTransformToOOBitwiseRightShift() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("unsigned int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = globalInt >> 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOAssignmentExpression) someFunction.getStatements().get(0)).getRightSide();
	Assert.assertTrue(expression instanceof OOBitWiseRightShift);
	Assert.assertTrue(((OOBitWiseRightShift) expression).getLeftSide() instanceof OOFieldReferenceExpression);
	Assert.assertTrue(((OOBitWiseRightShift) expression).getRightSide() instanceof OOIntegerLiteral);
    }
    
    @Test
    public void operatorEquals_shouldTransformToOOEquals() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("unsigned int globalInt;");
	sourceCode.append("bool someFunction(int x) {");
	sourceCode.append("	return x == globalInt;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOReturn) someFunction.getStatements().get(0)).getReturnedExpresssion();
	Assert.assertTrue(expression instanceof OOEqualsExpression);
	Assert.assertTrue(((OOEqualsExpression) expression).getLeftSide() instanceof OOVariableReferenceExpression);
	Assert.assertTrue(((OOEqualsExpression) expression).getRightSide() instanceof OOFieldReferenceExpression);
    }
    
    @Test
    public void operatorNotEquals_shouldTransformToOONotEquals() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("unsigned int globalInt;");
	sourceCode.append("bool someFunction(int x) {");
	sourceCode.append("	return x != globalInt;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOReturn) someFunction.getStatements().get(0)).getReturnedExpresssion();
	Assert.assertTrue(expression instanceof OONotEqualsExpression);
	Assert.assertTrue(((OONotEqualsExpression) expression).getLeftSide() instanceof OOVariableReferenceExpression);
	Assert.assertTrue(((OONotEqualsExpression) expression).getRightSide() instanceof OOFieldReferenceExpression);
    }
    
    @Test
    public void operatorGreaterThan_shouldTransformToOOGreaterThan() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("unsigned int globalInt;");
	sourceCode.append("bool someFunction(int x) {");
	sourceCode.append("	return x > globalInt;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOReturn) someFunction.getStatements().get(0)).getReturnedExpresssion();
	Assert.assertTrue(expression instanceof OOGreaterThanExpression);
	Assert.assertTrue(((OOGreaterThanExpression) expression).getLeftSide() instanceof OOVariableReferenceExpression);
	Assert.assertTrue(((OOGreaterThanExpression) expression).getRightSide() instanceof OOFieldReferenceExpression);
    }
    
    @Test
    public void operatorGreaterEquals_shouldTransformToOOGreaterEquals() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("unsigned int globalInt;");
	sourceCode.append("bool someFunction(int x) {");
	sourceCode.append("	return x >= globalInt;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOReturn) someFunction.getStatements().get(0)).getReturnedExpresssion();
	Assert.assertTrue(expression instanceof OOGreaterEqualsExpression);
	Assert.assertTrue(((OOGreaterEqualsExpression) expression).getLeftSide() instanceof OOVariableReferenceExpression);
	Assert.assertTrue(((OOGreaterEqualsExpression) expression).getRightSide() instanceof OOFieldReferenceExpression);
    }
    
    @Test
    public void operatorLessThan_shouldTransformToOOLessThan() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("unsigned int globalInt;");
	sourceCode.append("bool someFunction(int x) {");
	sourceCode.append("	return x < globalInt;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOReturn) someFunction.getStatements().get(0)).getReturnedExpresssion();
	Assert.assertTrue(expression instanceof OOLessThanExpression);
	Assert.assertTrue(((OOLessThanExpression) expression).getLeftSide() instanceof OOVariableReferenceExpression);
	Assert.assertTrue(((OOLessThanExpression) expression).getRightSide() instanceof OOFieldReferenceExpression);
    }
    
    @Test
    public void operatorLessEquals_shouldTransformToOOLessEquals() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("unsigned int globalInt;");
	sourceCode.append("bool someFunction(int x) {");
	sourceCode.append("	return x <= globalInt;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOExpression expression = ((OOReturn) someFunction.getStatements().get(0)).getReturnedExpresssion();
	Assert.assertTrue(expression instanceof OOLessEqualsExpression);
	Assert.assertTrue(((OOLessEqualsExpression) expression).getLeftSide() instanceof OOVariableReferenceExpression);
	Assert.assertTrue(((OOLessEqualsExpression) expression).getRightSide() instanceof OOFieldReferenceExpression);
    }
}
