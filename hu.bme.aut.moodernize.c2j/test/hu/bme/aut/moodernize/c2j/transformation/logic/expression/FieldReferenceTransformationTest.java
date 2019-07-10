package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFieldReferenceExpression;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOThisLiteral;
import hu.bme.aut.oogen.OOVariableReferenceExpression;

public class FieldReferenceTransformationTest extends AbstractTransformationTest {

    @Test
    public void fieldReference_shouldTransformToOOFieldReference() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef struct S1 { int x; } S1;");
	sourceCode.append("S1 s;");
	sourceCode.append("void setSx() {");
	sourceCode.append("	s.x = 2;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOMethod function = getDefaultClass(model).getMethods().get(0);
	OOAssignmentExpression assignment = (OOAssignmentExpression) function.getStatements().get(0);
	OOExpression lhs = assignment.getLeftSide();
	Assert.assertTrue(lhs instanceof OOFieldReferenceExpression);
	OOFieldReferenceExpression fieldReference = (OOFieldReferenceExpression) lhs;
	Assert.assertEquals("x", fieldReference.getFieldName());
	Assert.assertTrue(fieldReference.getFieldOwner() instanceof OOVariableReferenceExpression);
	Assert.assertEquals("s",
		((OOVariableReferenceExpression) fieldReference.getFieldOwner()).getVariable().getName());
    }
    
    @Test
    public void fieldReference_ownerShouldBeReplacedWithOOThisLiteral() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef struct S1 { int x; } S1;");
	sourceCode.append("S1 s;");
	sourceCode.append("void setSx(S1 s) {");
	sourceCode.append("	s.x = 2;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());
	OOMethod function = TransformUtil.findAndGetMethodFromClasses(model.getPackages().get(0).getClasses(), "setSx");
	OOAssignmentExpression assignment = (OOAssignmentExpression) function.getStatements().get(0);
	OOExpression lhs = assignment.getLeftSide();
	Assert.assertTrue(lhs instanceof OOFieldReferenceExpression);
	OOFieldReferenceExpression fieldReference = (OOFieldReferenceExpression) lhs;
	Assert.assertEquals("x", fieldReference.getFieldName());
	Assert.assertTrue(fieldReference.getFieldOwner() instanceof OOThisLiteral);
    }
}
