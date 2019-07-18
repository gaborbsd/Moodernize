package hu.bme.aut.moodernize.c2j.transformation.logic.declaration;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOLogicalLiteral;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOSubtractionExpression;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableDeclarationList;

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
	List<OOStatement> statements = getDefaultClass(model).getMethods().get(0).getStatements();
	for (int i = 0; i <= 3; i++) {
	    OOStatement statement = statements.get(i);
	    List<OOVariable> variableDeclarations = ((OOVariableDeclarationList) statement).getVariableDeclarations();
	    Assert.assertEquals(1, variableDeclarations.size());
	    OOVariable variable = variableDeclarations.get(0);
	    switch (i) {
	    case 0:
		Assert.assertTrue(variable.getType().getBaseType() == OOBaseType.BOOLEAN);
		Assert.assertTrue(variable.getName().equals("b"));
		break;
	    case 1:
		Assert.assertTrue(variable.getType().getBaseType() == OOBaseType.LONG);
		Assert.assertTrue(variable.getName().equals("li"));
		break;
	    case 2:
		Assert.assertTrue(variable.getType().getBaseType() == OOBaseType.DOUBLE);
		Assert.assertTrue(variable.getName().equals("f"));
		break;
	    case 3:
		Assert.assertTrue(variable.getType().getBaseType() == OOBaseType.BYTE);
		Assert.assertTrue(variable.getName().equals("uc"));
		break;
	    }
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
	List<OOStatement> statements = getDefaultClass(model).getMethods().get(0).getStatements();
	for (int i = 0; i <= 1; i++) {
	    OOStatement statement = statements.get(i);
	    List<OOVariable> variableDeclarations = ((OOVariableDeclarationList) statement).getVariableDeclarations();
	    Assert.assertEquals(1, variableDeclarations.size());
	    OOVariable variable = variableDeclarations.get(0);
	    switch (i) {
	    case 0:
		Assert.assertTrue(variable.getType().getBaseType() == OOBaseType.BOOLEAN);
		Assert.assertTrue(variable.getName().equals("b"));

		OOLogicalLiteral boolVarValue = (OOLogicalLiteral) variable.getInitializerExpression();
		Assert.assertFalse(boolVarValue.isValue());
		break;
	    case 1:
		Assert.assertTrue(variable.getType().getBaseType() == OOBaseType.LONG);
		Assert.assertTrue(variable.getName().equals("li"));

		OOSubtractionExpression initExp = (OOSubtractionExpression) variable.getInitializerExpression();
		OOIntegerLiteral lhs = (OOIntegerLiteral) initExp.getLeftSide();
		OOIntegerLiteral rhs = (OOIntegerLiteral) initExp.getRightSide();
		Assert.assertEquals(123, lhs.getValue());
		Assert.assertEquals(456, rhs.getValue());
		break;
	    }
	}
    }

    @Test
    public void structTypeVariable_shouldTransformToCorrespondingDeclaration() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef struct S1 {int i, j = 0;}someStruct;");
	sourceCode.append("struct S2 {char *c;};");
	sourceCode.append("int main(void) {");
	sourceCode.append("someStruct struct1; ");
	sourceCode.append("struct S2 struct3;}");

	OOModel model = getModelBySourceCode(sourceCode.toString());
	OOMethod function = TransformUtil.getFunctionByName(getDefaultClass(model).getMethods(), "main");
	List<OOStatement> statements = function.getStatements();
	for (int i = 0; i <= 1; i++) {
	    OOStatement statement = statements.get(i);
	    List<OOVariable> variableDeclarations = ((OOVariableDeclarationList) statement).getVariableDeclarations();
	    Assert.assertEquals(1, variableDeclarations.size());
	    OOVariable variable = variableDeclarations.get(0);
	    switch (i) {
	    case 0:
		Assert.assertTrue(variable.getType().getClassType().getName().equals("someStruct"));
		Assert.assertTrue(variable.getName().equals("struct1"));
		break;
	    case 1:
		Assert.assertTrue(variable.getType().getClassType().getName().equals("S2"));
		Assert.assertTrue(variable.getName().equals("struct3"));
		break;
	    }
	}
    }

    @Test
    public void multipleDeclaratorsInOneStatement_shouldTransformToMultipleStatementsInDeclarationList() {
	StringBuilder sourceCode = new StringBuilder();

	sourceCode.append("int main(void) {");
	sourceCode.append("int a = 0, b, c = 1; ");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOVariableDeclarationList declarationList = (OOVariableDeclarationList) getDefaultClass(model).getMethods()
		.get(0).getStatements().get(0);
	List<OOVariable> declarations = declarationList.getVariableDeclarations();
	Assert.assertEquals(3, declarations.size());

	for (int i = 0; i <= 2; i++) {
	    OOVariable variable = declarations.get(i);
	    switch (i) {
	    case 0:
		Assert.assertEquals("a", variable.getName());
		Assert.assertEquals(0, ((OOIntegerLiteral) (variable.getInitializerExpression())).getValue());
		break;
	    case 1:
		Assert.assertEquals("b", variable.getName());
		Assert.assertNull(variable.getInitializerExpression());
		break;
	    case 2:
		Assert.assertEquals("c", variable.getName());
		Assert.assertEquals(1, ((OOIntegerLiteral) (variable.getInitializerExpression())).getValue());
		break;
	    }
	}
    }
}
