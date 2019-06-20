package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOReturn;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOTypeCast;
import hu.bme.aut.oogen.OOVariableReferenceExpression;

public class CastExpressionTransformationTest extends AbstractTransformationTest {
    @Test
    public void baseTypeCast_shouldTransformToOOPrimitiveTypeCast() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int someFunction(double n) {");
	sourceCode.append("	return (int) n;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());
	OOStatement returnStatement = getDefaultClass(model).getMethods().get(0).getStatements().get(0);
	OOExpression returnedExpression = ((OOReturn) returnStatement).getReturnedExpresssion();
	Assert.assertTrue(returnedExpression instanceof OOTypeCast);
	Assert.assertTrue(((OOTypeCast) returnedExpression).getType().getBaseType() == OOBaseType.INT);
	Assert.assertTrue(((OOTypeCast) returnedExpression).getExpression() instanceof OOVariableReferenceExpression);
    }

    @Test
    public void structTypeCast_shouldTransformToOOReferenceTypeCast() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef struct S {double n;} S;");
	sourceCode.append("int someFunction(double n) {");
	sourceCode.append("	return (S) 0;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());
	OOStatement returnStatement = getDefaultClass(model).getMethods().get(0).getStatements().get(0);
	OOExpression returnedExpression = ((OOReturn) returnStatement).getReturnedExpresssion();
	Assert.assertTrue(returnedExpression instanceof OOTypeCast);
	Assert.assertTrue(((OOTypeCast) returnedExpression).getType().getClassType().getName().equals("S"));
	Assert.assertTrue(((OOTypeCast) returnedExpression).getExpression() instanceof OOIntegerLiteral);
    }
}
