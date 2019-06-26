package hu.bme.aut.moodernize.c2j.transformation.logic.statement;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOEqualsExpression;
import hu.bme.aut.oogen.OOIf;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOWhile;

public class WhileStatementTransformationTest extends AbstractTransformationTest {
 // TODO: Do-While tests
    @Test
    public void oneStatementWithoutBraces_shouldTransformToOneStatementWhile() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	while (i < 0) i = i + 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement statement = getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(statement instanceof OOWhile);
	OOWhile whileStatement = (OOWhile) statement;
	Assert.assertEquals(1, whileStatement.getBodyStatements().size());
    }
    
    @Test
    public void oneStatementWithBraces_shouldTransformToOneStatementWhile() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	while (i < 0) {i = i + 1;}");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement statement = getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(statement instanceof OOWhile);
	OOWhile whileStatement = (OOWhile) statement;
	Assert.assertEquals(1, whileStatement.getBodyStatements().size());
    }
    
    @Test
    public void comprehensive_shouldTransformToCorrespondingWhileStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	while (i == 2) { i = 0; while (i != 2) { i = i + 1;}} ");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOStatement> statements = getDefaultClass(model).getMethods().get(0).getStatements();
	Assert.assertEquals(2, statements.size());
	
	OOWhile whileStatement = (OOWhile) statements.get(1);
	Assert.assertTrue(whileStatement.getCondition() instanceof OOEqualsExpression);
	Assert.assertEquals(2, whileStatement.getBodyStatements().size());
    }
}
