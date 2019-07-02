package hu.bme.aut.moodernize.c2j.transformation.logic.statement;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OODoWhile;
import hu.bme.aut.oogen.OOEqualsExpression;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOWhile;

public class DoWhileStatementTransformationTest extends AbstractTransformationTest {
    @Test
    public void doWhile_shouldTransformToOODoWhile() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	do {");
	sourceCode.append("		i = 0;");
	sourceCode.append("		while (i != 2) { ");
	sourceCode.append("			i = i + 1;");	
	sourceCode.append("		}");	
	sourceCode.append("	} while (i == 2);");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOStatement> statements = getDefaultClass(model).getMethods().get(0).getStatements();
	Assert.assertEquals(2, statements.size());
	
	OODoWhile doWhile = (OODoWhile) statements.get(1);
	Assert.assertTrue(doWhile.getCondition() instanceof OOEqualsExpression);
	Assert.assertEquals(2, doWhile.getBodyStatements().size());
    }
}
