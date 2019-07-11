package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OODoubleLiteral;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOLogicalLiteral;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OONullLiteral;
import hu.bme.aut.oogen.OOReturn;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableDeclarationList;

public class LiteralExpressionTransformationTest extends AbstractTransformationTest {
    @Test
    public void integer_shouldTransformToOOIntegerLiteral() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int someFunction() {");
	sourceCode.append("	return 175;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement returnStatement = getDefaultClass(model).getMethods().get(0).getStatements().get(0);
	OOExpression returnExpression = ((OOReturn) returnStatement).getReturnedExpresssion();
	Assert.assertTrue(returnExpression instanceof OOIntegerLiteral);
	Assert.assertEquals(175, ((OOIntegerLiteral) returnExpression).getValue());
    }

    @Test
    public void booleanFalse_shouldTransformToOOBoolLiteral() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction() {");
	sourceCode.append("	return false;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement returnStatement = getDefaultClass(model).getMethods().get(0).getStatements().get(0);
	OOExpression returnExpression = ((OOReturn) returnStatement).getReturnedExpresssion();
	Assert.assertTrue(returnExpression instanceof OOLogicalLiteral);
	Assert.assertFalse(((OOLogicalLiteral) returnExpression).isValue());
    }

    @Test
    public void booleanTrue_shouldTransformToOOBoolLiteral() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("bool someFunction() {");
	sourceCode.append("	return true;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement returnStatement = getDefaultClass(model).getMethods().get(0).getStatements().get(0);
	OOExpression returnExpression = ((OOReturn) returnStatement).getReturnedExpresssion();
	Assert.assertTrue(returnExpression instanceof OOLogicalLiteral);
	Assert.assertTrue(((OOLogicalLiteral) returnExpression).isValue());
    }

    @Test
    public void float_shouldTransformToOODoubleLiteral() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("float someFunction() {");
	sourceCode.append("	return 3.27114f;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement returnStatement = getDefaultClass(model).getMethods().get(0).getStatements().get(0);
	OOExpression returnExpression = ((OOReturn) returnStatement).getReturnedExpresssion();
	Assert.assertTrue(returnExpression instanceof OODoubleLiteral);
	Assert.assertEquals(3.27114, ((OODoubleLiteral) returnExpression).getValue(), 0.00001);
    }

    @Test
    public void nullPointer_shouldTransformToOONullLiteral() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("void someFunction() {");
	sourceCode.append("	int *x = NULL;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOVariableDeclarationList declarationList = (OOVariableDeclarationList) getDefaultClass(model).getMethods()
		.get(0).getStatements().get(0);
	Assert.assertEquals(1, declarationList.getVariableDeclarations().size());
	OOVariable declaredVariable = declarationList.getVariableDeclarations().get(0);
	Assert.assertTrue(declaredVariable.getInitializerExpression() instanceof OONullLiteral);
    }

}
