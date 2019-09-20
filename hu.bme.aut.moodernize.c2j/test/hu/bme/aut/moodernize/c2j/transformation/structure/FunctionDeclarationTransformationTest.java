package hu.bme.aut.moodernize.c2j.transformation.structure;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOVariable;

public class FunctionDeclarationTransformationTest extends AbstractTransformationTest {
    @Test
    public void function_shouldTransformToCorrespondingSignature() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("void someFunction(int a);");
	sourceCode.append("int anotherFunction(){return 65535;};");
	sourceCode.append("int main(void){return 0;}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMethod> globalFunctions = getDefaultClass(model).getMethods();

	for (int i = 0; i < globalFunctions.size(); i++) {
	    OOMethod method = globalFunctions.get(i);
	    List<OOVariable> parameters = method.getParameters();
	    switch (method.getName()) {
	    case "someFunction": {
		Assert.assertTrue(method.getReturnType() == null);
		Assert.assertEquals(1, parameters.size());
		break;
	    }
	    case "anotherFunction": {
		Assert.assertTrue(method.getReturnType().getBaseType() == OOBaseType.INT);
		Assert.assertEquals(0, parameters.size());
		break;
	    }
	    case "main": {
		Assert.assertTrue(method.getReturnType().getBaseType() == OOBaseType.INT);
		Assert.assertEquals(0, parameters.size());
		break;
	    }
	    default:
		break;
	    }
	}
    }
}
