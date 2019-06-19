package hu.bme.aut.moodernize.c2j.transformation.logic.declaration;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOBoolLiteral;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOSubtractionExpression;
import hu.bme.aut.oogen.OOVariable;

public class SimpleDeclarationTransformationTest extends AbstractTransformationTest {
    @Test
    public void baseTypeVariablesWithoutInit_shouldTransformToCorrespondingDeclaration() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("bool b;");
	sourceCode.append("long int li;");
	sourceCode.append("float f;");
	sourceCode.append("unsigned char uc;");
	sourceCode.append("return 0; }");

	OOModel model = getModelBySourceCode(sourceCode.toString());
	List<OOStatement> statements = getDefaultClass(model).getMethods().get(0)
		.getStatements();
	int index = 0;
	for (OOStatement statement : statements) {
	    switch (index) {
	    case 0:
		OOVariable boolVar = (OOVariable) statement;
		Assert.assertTrue(boolVar.getType().getBaseType() == OOBaseType.BOOLEAN);
		Assert.assertTrue(boolVar.getName().equals("b"));
		break;
	    case 1:
		OOVariable longIntVar = (OOVariable) statement;
		Assert.assertTrue(longIntVar.getType().getBaseType() == OOBaseType.LONG);
		Assert.assertTrue(longIntVar.getName().equals("li"));
		break;
	    case 2:
		OOVariable floatVar = (OOVariable) statement;
		Assert.assertTrue(floatVar.getType().getBaseType() == OOBaseType.DOUBLE);
		Assert.assertTrue(floatVar.getName().equals("f"));
		break;
	    case 3:
		OOVariable unsignedCharVar = (OOVariable) statement;
		Assert.assertTrue(unsignedCharVar.getType().getBaseType() == OOBaseType.BYTE);
		Assert.assertTrue(unsignedCharVar.getName().equals("uc"));
		break;
	    default:
		break;
	    }
	    index++;
	}
    }

    @Test
    public void baseTypeVariableWithInit_shouldTransformToCorrespondingDeclaration() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("bool b = false;");
	sourceCode.append("long int li = 123 - 456;");
	sourceCode.append("return 0; }");

	OOModel model = getModelBySourceCode(sourceCode.toString());
	List<OOStatement> statements = getDefaultClass(model).getMethods().get(0)
		.getStatements();
	int index = 0;
	for (OOStatement statement : statements) {
	    switch (index) {
	    case 0:
		OOVariable boolVar = (OOVariable) statement;
		Assert.assertTrue(boolVar.getType().getBaseType() == OOBaseType.BOOLEAN);
		Assert.assertTrue(boolVar.getName().equals("b"));

		OOBoolLiteral boolVarValue = (OOBoolLiteral) boolVar.getInitializerExpression();
		Assert.assertFalse(boolVarValue.isValue());
		break;
	    case 1:
		OOVariable longIntVar = (OOVariable) statement;
		Assert.assertTrue(longIntVar.getType().getBaseType() == OOBaseType.LONG);
		Assert.assertTrue(longIntVar.getName().equals("li"));

		OOSubtractionExpression initExp = (OOSubtractionExpression) longIntVar.getInitializerExpression();
		OOIntegerLiteral lhs = (OOIntegerLiteral) initExp.getLeftSide();
		OOIntegerLiteral rhs = (OOIntegerLiteral) initExp.getRightSide();
		Assert.assertEquals(123, lhs.getValue());
		Assert.assertEquals(456, rhs.getValue());
		break;
	    default:
		break;
	    }
	    index++;
	}
    }

    @Test
    public void structTypeVariable_shouldTransformToCorrespondingDeclaration() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef struct S1 {int i, j = 0;}someStruct;");
	sourceCode.append("struct S2 {char *c;};");
	sourceCode.append("int main(void) {");
	sourceCode.append("someStruct struct1, struct2; ");
	sourceCode.append("struct S2 struct3;}");

	OOModel model = getModelBySourceCode(sourceCode.toString());
	List<OOStatement> statements = getDefaultClass(model).getMethods().get(0)
		.getStatements();
	Assert.assertEquals(3, statements.size());

	int index = 0;
	for (OOStatement statement : statements) {
	    switch (index) {
	    case 0:
		OOVariable struct1Var = (OOVariable) statement;
		Assert.assertTrue(struct1Var.getType().getClassType().getName().equals("someStruct"));
		Assert.assertTrue(struct1Var.getName().equals("struct1"));
		break;
	    case 1:
		OOVariable struct2Var = (OOVariable) statement;
		Assert.assertTrue(struct2Var.getType().getClassType().getName().equals("someStruct"));
		Assert.assertTrue(struct2Var.getName().equals("struct2"));
		break;
	    case 2:
		OOVariable struct3Var = (OOVariable) statement;
		Assert.assertTrue(struct3Var.getType().getClassType().getName().equals("S2"));
		Assert.assertTrue(struct3Var.getName().equals("struct3"));
		break;
	    default:
		break;
	    }
	    index++;
	}
    }
}
