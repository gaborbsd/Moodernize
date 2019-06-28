package hu.bme.aut.moodernize.c2j.transformation.logic.statement;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOSwitch;
import hu.bme.aut.oogen.OOTypeCast;

public class SwitchStatementTransformationTest extends AbstractTransformationTest {
    @Test
    public void singleCaseSwitch_shouldTransformToSingleCaseOOSwitch() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	double controller = 4.875;");
	sourceCode.append("	switch ((int) controller) {");
	sourceCode.append("	case 4: break;");
	sourceCode.append("	}");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOSwitch switchStatement = (OOSwitch) getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(switchStatement.getControllerExpression() instanceof OOTypeCast);
	Assert.assertEquals(1, switchStatement.getCaseStatements().size());
    }

    @Test
    public void multipleCaseSwitch_shouldTransformToMultipleCaseOOSwitch() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	double controller = 4.875;");
	sourceCode.append("	switch ((int) controller) {");
	sourceCode.append("	case 4: break;");
	sourceCode.append("	case 5:");
	sourceCode.append("	case 10:");
	sourceCode.append("	case 0:");
	sourceCode.append("	case 100:");
	sourceCode.append("		break;");
	sourceCode.append("	case 1024:");
	sourceCode.append("		controller = 0;");
	sourceCode.append("	}");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOSwitch switchStatement = (OOSwitch) getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(switchStatement.getControllerExpression() instanceof OOTypeCast);
	Assert.assertEquals(6, switchStatement.getCaseStatements().size());
    }

    @Test
    public void noDefaultSwitch_shouldTransformToNoDefaultOOSwitch() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	double controller = 4.875;");
	sourceCode.append("	switch ((int) controller) {");
	sourceCode.append("	case 4: break;");
	sourceCode.append("	}");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOSwitch switchStatement = (OOSwitch) getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertNull(switchStatement.getDefaultStatement());
    }

    @Test
    public void defaultCaseSwitch_shouldTransformTodefaultCaseOOSwitch() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	double controller = 4.875;");
	sourceCode.append("	switch ((int) controller) {");
	sourceCode.append("	case 4: break;");
	sourceCode.append("	case 5:");
	sourceCode.append("	case 10:");
	sourceCode.append("	case 0:");
	sourceCode.append("	case 100:");
	sourceCode.append("		break;");
	sourceCode.append("	default:");
	sourceCode.append("		controller = 0;");
	sourceCode.append("	}");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOSwitch switchStatement = (OOSwitch) getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertNotNull(switchStatement.getDefaultStatement());
    }

    @Test
    public void onlyDefaultSwitch_shouldTransformToOnlyDefaultOOSwitch() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	double controller = 4.875;");
	sourceCode.append("	switch ((int) controller) {");
	sourceCode.append("	default:");
	sourceCode.append("		controller = 0;");
	sourceCode.append("	}");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOSwitch switchStatement = (OOSwitch) getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertEquals(0, switchStatement.getCaseStatements().size());
	Assert.assertNotNull(switchStatement.getDefaultStatement());
    }
}
