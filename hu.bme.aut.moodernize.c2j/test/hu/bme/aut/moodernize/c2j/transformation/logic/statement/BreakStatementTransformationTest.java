package hu.bme.aut.moodernize.c2j.transformation.logic.statement;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOBreak;
import hu.bme.aut.oogen.OOFor;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOSwitch;

public class BreakStatementTransformationTest extends AbstractTransformationTest {
    @Test
    public void breakInsideLoop_shouldTransformToOOBreakStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	for (int i = 0; i < 10; i = i + 1) { break; }");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement statement = ((OOFor) getDefaultClass(model).getMethods().get(0).getStatements().get(0))
		.getBodyStatements().get(0);

	Assert.assertTrue(statement instanceof OOBreak);
    }

    @Test
    public void breakInsideCaseStatement_shouldTransformToOOBreakStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i = 0;");
	sourceCode.append("	switch (i) { case 0: break; }");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOSwitch statement = (OOSwitch) getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(statement.getCaseStatements().get(0).getBodyStatements().get(0) instanceof OOBreak);
    }
}
