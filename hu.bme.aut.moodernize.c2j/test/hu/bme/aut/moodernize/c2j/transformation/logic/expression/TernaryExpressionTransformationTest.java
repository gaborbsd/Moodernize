package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOMinusExpression;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OONotEqualsExpression;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOTernaryOperator;

public class TernaryExpressionTransformationTest extends AbstractTransformationTest {
    @Test
    public void ternaryOperator_shouldTransformToOOTernaryOperator() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("int someFunction() {");
	sourceCode.append("	globalInt = globalInt != 0 ? -globalInt : 0;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod someFunction = getDefaultClass(model).getMethods().get(0);
	OOStatement statement = ((OOAssignmentExpression) someFunction.getStatements().get(0)).getRightSide();
	Assert.assertTrue(statement instanceof OOTernaryOperator);
	OOTernaryOperator ternary = (OOTernaryOperator) statement;
	Assert.assertTrue(ternary.getCondition() instanceof OONotEqualsExpression);
	Assert.assertTrue(ternary.getPositiveBranch() instanceof OOMinusExpression);
	Assert.assertTrue(ternary.getNegativeBranch() instanceof OOIntegerLiteral);
    }
    
}
