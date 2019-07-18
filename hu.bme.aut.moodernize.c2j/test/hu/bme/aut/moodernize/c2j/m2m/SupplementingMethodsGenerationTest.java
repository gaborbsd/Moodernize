package hu.bme.aut.moodernize.c2j.m2m;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOReturn;

public class SupplementingMethodsGenerationTest extends AbstractTransformationTest {
    
    @Test
    public void classMembers_shouldGenerateGettersAndSetters() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef struct S1 {int x; } S1;");
	
	OOModel model = getModelBySourceCode(sourceCode.toString());
	OOClass cl = TransformUtil.getClassByName(model.getPackages().get(0).getClasses(), "S1");
	List<OOMethod> methods = cl.getMethods();
	
	Assert.assertEquals(2, methods.size());
	OOMethod method = methods.get(0);
	Assert.assertEquals("getX", method.getName());
	Assert.assertEquals(1, method.getStatements().size());
	Assert.assertTrue(method.getStatements().get(0) instanceof OOReturn);
	
	method = methods.get(1);
	Assert.assertEquals("setX", method.getName());
	Assert.assertEquals(1, method.getParameters().size());
	Assert.assertEquals(1, method.getStatements().size());
	Assert.assertTrue(method.getStatements().get(0) instanceof OOAssignmentExpression);
    }
}
