package hu.bme.aut.moodernize.c2j.transformation.logic.statement;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOBreak;
import hu.bme.aut.oogen.OOCase;
import hu.bme.aut.oogen.OODivisionExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOSwitch;
import hu.bme.aut.oogen.OOVariable;

public class CaseStatementTransformationTest extends AbstractTransformationTest {
    @Test
    public void comprehensiveCaseStatements_shouldTransformToCorrespondingOOCaseStatements() {
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
	sourceCode.append("	case 1024:");
	sourceCode.append("		controller = 0;");
	sourceCode.append("	}");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOSwitch switchStatement = (OOSwitch) getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	List<OOCase> caseStatements = switchStatement.getCaseStatements();
	Assert.assertEquals(6, caseStatements.size());
	
	for (OOCase caseStatement : caseStatements) {
	    List<OOStatement> body = caseStatement.getBodyStatements();
	    switch (((OOIntegerLiteral)(caseStatement.getExpression())).getValue()) {
	    case 4:
		Assert.assertEquals(1, body.size());
		Assert.assertTrue(body.get(0) instanceof OOBreak);
		break;
	    case 5:
	    case 10:
	    case 0:
		Assert.assertEquals(0, body.size());
		break;
	    case 100:
		Assert.assertEquals(3, body.size());
		Assert.assertTrue(body.get(0) instanceof OODivisionExpression);
		Assert.assertTrue(body.get(1) instanceof OOVariable);
		Assert.assertTrue(body.get(2) instanceof OOBreak);
		break;
	    case 1024:
		Assert.assertEquals(1, body.size());
		Assert.assertTrue(body.get(0) instanceof OOAssignmentExpression);
		break;
	    }
 	}
    }
}
