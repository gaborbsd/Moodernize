package hu.bme.aut.moodernize.c2j.transformation.structure;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOAdditionExpression;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OONewArray;
import hu.bme.aut.oogen.OOVariable;

public class VariableTransformationTest extends AbstractTransformationTest {
    @Test
    public void globalVariables_shouldTransformToCorrespondingGlobalVariables() throws CoreException {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int a = 2, b;");
	sourceCode.append("long c[10], d = 5, e;");
	sourceCode.append("int f = 2 * a + b + c + d + e;");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMember> globalVariables = getDefaultClass(model).getMembers();
	Assert.assertEquals(6, globalVariables.size());
	for (OOVariable globalVariable : globalVariables) {
	    switch (globalVariables.indexOf(globalVariable)) {
	    case 0:
		Assert.assertEquals("a", globalVariable.getName());
		Assert.assertTrue(globalVariable.getType().getBaseType() == OOBaseType.INT);
		Assert.assertTrue(globalVariable.getInitializerExpression() instanceof OOIntegerLiteral);
		break;
	    case 1:
		Assert.assertEquals("b", globalVariable.getName());
		Assert.assertTrue(globalVariable.getType().getBaseType() == OOBaseType.INT);
		Assert.assertNull(globalVariable.getInitializerExpression());
		break;
	    case 2:
		Assert.assertEquals("c", globalVariable.getName());
		Assert.assertTrue(globalVariable.getType().getBaseType() == OOBaseType.LONG);
		Assert.assertTrue(globalVariable.getInitializerExpression() instanceof OONewArray);
		Assert.assertEquals(1, globalVariable.getType().getArrayDimensions());
		break;
	    case 3:
		Assert.assertEquals("d", globalVariable.getName());
		Assert.assertTrue(globalVariable.getType().getBaseType() == OOBaseType.LONG);
		Assert.assertTrue(globalVariable.getInitializerExpression() instanceof OOIntegerLiteral);
		break;
	    case 4:
		Assert.assertEquals("e", globalVariable.getName());
		Assert.assertTrue(globalVariable.getType().getBaseType() == OOBaseType.LONG);
		Assert.assertNull(globalVariable.getInitializerExpression());
		break;
	    case 5:
		Assert.assertEquals("f", globalVariable.getName());
		Assert.assertTrue(globalVariable.getType().getBaseType() == OOBaseType.INT);
		Assert.assertTrue(globalVariable.getInitializerExpression() instanceof OOAdditionExpression);
		break;
	    }
	}
    }

    @Test
    public void localVariable_shouldNotTransformToGlobalVariable() throws CoreException {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) { int x = 0; return 0;}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMember> globalVariables = getDefaultClass(model).getMembers();
	Assert.assertEquals(0, globalVariables.size());
    }
}
