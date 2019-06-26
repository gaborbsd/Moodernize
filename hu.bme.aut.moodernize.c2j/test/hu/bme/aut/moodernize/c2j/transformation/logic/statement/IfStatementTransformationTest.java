package hu.bme.aut.moodernize.c2j.transformation.logic.statement;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOEqualsExpression;
import hu.bme.aut.oogen.OOIf;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;

public class IfStatementTransformationTest extends AbstractTransformationTest {
    // TODO: Elseif tests
    @Test
    public void oneStatementWithoutBraces_shouldTransformToOneStatementIf() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	if (i < 0) i = i + 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement statement = getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(statement instanceof OOIf);
	OOIf ifStatement = (OOIf) statement;
	Assert.assertEquals(1, ifStatement.getBodyStatements().size());
    }
    
    @Test
    public void oneStatementWithBraces_shouldTransformToOneStatementIf() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	if (i < 0){ i = i + 1;}");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement statement = getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(statement instanceof OOIf);
	OOIf ifStatement = (OOIf) statement;
	Assert.assertEquals(1, ifStatement.getBodyStatements().size());
    }
    
    @Test
    public void comprehensive_shouldTransformToCorrespondingIfStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	if (i == 2) { i = 0; if (i != 0) { i = 0;}} ");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOStatement> statements = getDefaultClass(model).getMethods().get(0).getStatements();
	Assert.assertEquals(2, statements.size());
	
	OOIf ifStatement = (OOIf) statements.get(1);
	Assert.assertTrue(ifStatement.getCondition() instanceof OOEqualsExpression);
	Assert.assertEquals(2, ifStatement.getBodyStatements().size());
    }
}
