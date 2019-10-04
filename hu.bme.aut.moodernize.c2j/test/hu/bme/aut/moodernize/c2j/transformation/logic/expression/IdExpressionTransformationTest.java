package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOReturn;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;

public class IdExpressionTransformationTest extends AbstractTransformationTest {
    @Test
    public void baseTypeVariableReference_shouldTransformToPrimitiveTypeOOVariableReference() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("void someFunction() {");
	sourceCode.append("	int x = 2;");
	sourceCode.append("	x = 1 + 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement assignmentStatement = getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(assignmentStatement instanceof OOAssignmentExpression);
	
	OOExpression lhs = ((OOAssignmentExpression) assignmentStatement).getLeftSide();
	Assert.assertTrue(lhs instanceof OOVariableReferenceExpression);
	
	OOVariable referredVariable = ((OOVariableReferenceExpression)lhs).getVariable();
	Assert.assertTrue(referredVariable.getName().equals("x"));
	Assert.assertTrue(referredVariable.getType().getBaseType() == OOBaseType.INT);
    }

    @Test
    public void structTypeVariableReference_shouldTransformToReferenceTypeOOVariableReference() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S {int x; };");
	sourceCode.append("int someFunction(void) {");
	sourceCode.append("	struct S s;");
	sourceCode.append("	return s;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement returnStatement = getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(returnStatement instanceof OOReturn);
	
	OOExpression returnedExpression = ((OOReturn) returnStatement).getReturnedExpresssion();
	Assert.assertTrue(returnedExpression instanceof OOVariableReferenceExpression);
	
	OOVariable referredVariable = ((OOVariableReferenceExpression)returnedExpression).getVariable();
	Assert.assertTrue(referredVariable.getName().equals("s"));
	Assert.assertTrue(referredVariable.getType().getClassType().getName().equals("S"));
    }
}
