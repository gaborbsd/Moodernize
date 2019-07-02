package hu.bme.aut.moodernize.c2j.transformation.logic.statement;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOCompoundStatement;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;

public class CompoundStatementTransformationTest extends AbstractTransformationTest {
    @Test
    public void comprehensive_shouldTransformToCorrespondingWhileStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	{ i = 0; while (i != 2) { i = i + 1;}} ");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOStatement> statements = getDefaultClass(model).getMethods().get(0).getStatements();
	Assert.assertEquals(2, statements.size());
	
	OOCompoundStatement compoundStatement = (OOCompoundStatement) statements.get(1);
	Assert.assertEquals(2, compoundStatement.getBodyStatements().size());
    }
}
