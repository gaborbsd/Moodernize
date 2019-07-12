package hu.bme.aut.moodernize.c2j.transformation.logic.statement;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOAdditionExpression;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOReturn;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;

public class ReturnStatementTransformationTest extends AbstractTransformationTest {
    @Test
    public void returnStatementVariableReference_shouldTransformToVariableReferenceReturnStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int someFunction(int x) {return x;}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMethod> functions = getDefaultClass(model).getMethods();
	Assert.assertEquals(1, functions.size());

	OOMethod function = functions.get(0);
	List<OOStatement> statements = function.getStatements();
	Assert.assertEquals(1, statements.size());
	OOStatement returnStatement = statements.get(0);
	Assert.assertTrue(returnStatement instanceof OOReturn);

	OOExpression returnExpression = ((OOReturn) returnStatement).getReturnedExpresssion();
	Assert.assertTrue(returnExpression instanceof OOVariableReferenceExpression);

	OOVariable referredVariable = ((OOVariableReferenceExpression) returnExpression).getVariable();
	Assert.assertTrue(referredVariable.getName().equals("x"));
	Assert.assertTrue(referredVariable.getType().getBaseType() == OOBaseType.INT);
    }

    @Test
    public void returnStatementLiteral_shouldTransformToLiteralReturnStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int someFunction(int x) {return 12;}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMethod> functions = getDefaultClass(model).getMethods();
	Assert.assertEquals(1, functions.size());

	OOMethod function = functions.get(0);
	List<OOStatement> statements = function.getStatements();
	Assert.assertEquals(1, statements.size());
	OOStatement returnStatement = statements.get(0);
	Assert.assertTrue(returnStatement instanceof OOReturn);

	OOExpression returnExpression = ((OOReturn) returnStatement).getReturnedExpresssion();
	Assert.assertTrue(returnExpression instanceof OOIntegerLiteral);
	Assert.assertEquals(12, ((OOIntegerLiteral) returnExpression).getValue());
    }

    @Test
    public void returnStatementExpression_shouldTransformToExpressionReturnStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int global = 12;");
	sourceCode.append("int someFunction(int x) {return x + global;}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMethod> functions = getDefaultClass(model).getMethods();

	OOMethod function = functions.get(0);
	List<OOStatement> statements = function.getStatements();
	Assert.assertEquals(1, statements.size());
	OOStatement returnStatement = statements.get(0);
	Assert.assertTrue(returnStatement instanceof OOReturn);

	OOExpression returnExpression = ((OOReturn) returnStatement).getReturnedExpresssion();
	Assert.assertTrue(returnExpression instanceof OOAdditionExpression);

	OOExpression lhs = ((OOAdditionExpression) returnExpression).getLeftSide();
	OOExpression rhs = ((OOAdditionExpression) returnExpression).getRightSide();
	Assert.assertTrue(lhs instanceof OOVariableReferenceExpression);
	Assert.assertTrue(rhs instanceof OOVariableReferenceExpression);

	OOVariable reffedVariableLhs = ((OOVariableReferenceExpression) lhs).getVariable();
	Assert.assertTrue(reffedVariableLhs.getName().equals("x"));
	Assert.assertTrue(reffedVariableLhs.getType().getBaseType() == OOBaseType.INT);

	OOVariable reffedVariableRhs = ((OOVariableReferenceExpression) rhs).getVariable();
	Assert.assertTrue(reffedVariableRhs.getName().equals("global"));
	Assert.assertTrue(reffedVariableRhs.getType().getBaseType() == OOBaseType.INT);
    }
}
