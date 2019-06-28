package hu.bme.aut.moodernize.c2j.transformation.logic.statement;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOContinue;
import hu.bme.aut.oogen.OOFor;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;

public class ContinueStatementTransformationTest extends AbstractTransformationTest {
    @Test
    public void continueInsideLoop_shouldTransformToOOContinueStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	for (int i = 0; i < 10; i = i + 1) { continue; }");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement statement = ((OOFor) getDefaultClass(model).getMethods().get(0).getStatements().get(0))
		.getBodyStatements().get(0);

	Assert.assertTrue(statement instanceof OOContinue);
    }
}
