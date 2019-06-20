package hu.bme.aut.moodernize.c2j.transformation.logic.expression;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOModel;

public class IdExpressionTransformationTest extends AbstractTransformationTest {
    @Test
    public void baseTypeVariableReference_shouldTransformToPrimitiveTypeOOVariableReference() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int globalInt;");
	sourceCode.append("void someFunction() {");
	sourceCode.append("	globalInt = 1 + 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());
	Assert.fail();
    }

    @Test
    public void structTypeVariableReference_shouldTransformToReferenceTypeOOVariableReference() {
	Assert.fail();
    }

    @Test
    public void localVariableReference_shouldTransformToVariableReference() {
	Assert.fail();
    }

    @Test
    public void globalVariableReference_shouldTransformToVariableReference() {
	Assert.fail();
    }

    @Test
    public void parameterReference_shouldTransformToVariableReference() {
	Assert.fail();
    }
}
