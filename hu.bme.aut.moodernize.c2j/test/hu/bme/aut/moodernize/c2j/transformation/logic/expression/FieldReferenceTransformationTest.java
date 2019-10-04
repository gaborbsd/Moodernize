package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFieldReferenceExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOVariableReferenceExpression;

public class FieldReferenceTransformationTest extends AbstractTransformationTest {
    @Test
    public void fieldReference_shouldTransformToGetterMethod() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef struct S1 { int x; } S1;");
	sourceCode.append("S1 s;");
	sourceCode.append("void testIndex(int i) {");
	sourceCode.append("	i = s.x;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod function = getDefaultClass(model).getMethods().get(0);
	OOAssignmentExpression assignment = (OOAssignmentExpression) function.getStatements().get(0);
	OOExpression rhs = assignment.getRightSide();
	Assert.assertTrue(rhs instanceof OOFunctionCallExpression);
	OOFunctionCallExpression getterCall = (OOFunctionCallExpression) rhs;
	Assert.assertEquals("getX", getterCall.getFunctionName());
	Assert.assertEquals(0, getterCall.getArgumentExpressions().size());
	Assert.assertTrue(getterCall.getOwnerExpression() instanceof OOFieldReferenceExpression);
    }
    
    @Test
    public void fieldReference_shouldTransformToSetterMethod() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef struct S1 { int x; } S1;");
	sourceCode.append("S1 s;");
	sourceCode.append("void testIndex(int i) {");
	sourceCode.append("	s.x = i;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod function = getDefaultClass(model).getMethods().get(0);
	
	OOStatement setterStatement = function.getStatements().get(0);
	Assert.assertTrue(setterStatement instanceof OOFunctionCallExpression);
	OOFunctionCallExpression setterCall = (OOFunctionCallExpression) setterStatement;
	Assert.assertEquals("setX", setterCall.getFunctionName());
	Assert.assertEquals(1, setterCall.getArgumentExpressions().size());
	Assert.assertTrue(setterCall.getArgumentExpressions().get(0) instanceof OOVariableReferenceExpression);
	Assert.assertTrue(setterCall.getOwnerExpression() instanceof OOFieldReferenceExpression);
    }
}
