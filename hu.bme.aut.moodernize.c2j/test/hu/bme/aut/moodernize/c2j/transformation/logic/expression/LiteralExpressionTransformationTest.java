package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;

public class LiteralExpressionTransformationTest extends AbstractTransformationTest {
    @Test
    public void integer_shouldTransformToOOIntegerLiteral() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int someFunction() {");
	sourceCode.append("	return -175;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());
	
	OOStatement returnStatement = getDefaultClass(model).getMethods().get(0).getStatements().get(0);
    }
}
