package hu.bme.aut.moodernize.c2j.transformation.logic.statement;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OODefault;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOSwitch;

public class DefaultStatementTransformationTest extends AbstractTransformationTest {
    @Test
    public void defaultStatement_shouldTransformToOODefaultStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	double controller = 4.875;");
	sourceCode.append("	switch ((int) controller) {");
	sourceCode.append("	case 4: break;");
	sourceCode.append("	case 5:");
	sourceCode.append("	case 10:");
	sourceCode.append("	case 0:");
	sourceCode.append("	case 100:");
	sourceCode.append("		controller /= 2;");
	sourceCode.append("		int i = controller += 0;");
	sourceCode.append("		break;");
	sourceCode.append("	default:");
	sourceCode.append("		controller = 0;");
	sourceCode.append("	}");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOSwitch switchStatement = (OOSwitch) getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	OODefault defaultStatement = switchStatement.getDefaultStatement();
	Assert.assertNotNull(defaultStatement);
	List<OOStatement> body = defaultStatement.getBodyStatements();
	Assert.assertEquals(1, body.size());
	Assert.assertTrue(body.get(0) instanceof OOAssignmentExpression);
    }
}
