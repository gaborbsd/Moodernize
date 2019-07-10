package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFieldReferenceExpression;
import hu.bme.aut.oogen.OOIndexing;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOVariableReferenceExpression;

public class IndexingTransformationTest extends AbstractTransformationTest {

    @Test
    public void arrayIndexing_shouldTransformToOOIndexing() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef struct S1 { int x[]; } S1;");
	sourceCode.append("S1 s;");
	sourceCode.append("void testIndex(int i) {");
	sourceCode.append("	s.x[i] = i;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod function = getDefaultClass(model).getMethods().get(0);
	OOAssignmentExpression assignment = (OOAssignmentExpression) function.getStatements().get(0);
	OOExpression lhs = assignment.getLeftSide();
	Assert.assertTrue(lhs instanceof OOIndexing);
	OOIndexing indexing = (OOIndexing) lhs;
	Assert.assertTrue(indexing.getCollectionExpression() instanceof OOFieldReferenceExpression);
	Assert.assertTrue(indexing.getIndexExpression() instanceof OOVariableReferenceExpression);
    }
}
