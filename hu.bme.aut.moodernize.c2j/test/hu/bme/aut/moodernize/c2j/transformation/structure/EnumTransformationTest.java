package hu.bme.aut.moodernize.c2j.transformation.structure;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOEnumeration;
import hu.bme.aut.oogen.OOFieldReferenceExpression;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStringLiteral;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableDeclarationList;

public class EnumTransformationTest extends AbstractTransformationTest {
    @Test
    public void enumDefinitionWithoutTypedef_shouldTransformToOOEnumeration() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("enum Color {RED, GREEN, BLUE};");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOEnumeration> enums = model.getPackages().get(0).getEnums();
	Assert.assertEquals(1, enums.size());
	OOEnumeration enumeration = enums.get(0);
	Assert.assertEquals("Color", enumeration.getName());
	for (String option : enumeration.getOptions()) {
	    Assert.assertTrue(option.equals("RED") || option.equals("GREEN") || option.equals("BLUE"));
	}
    }

    @Test
    public void enumDefinitionWithTypedef_shouldTransformToOOEnumeration() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef enum Color {RED, GREEN, BLUE}Color;");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOEnumeration> enums = model.getPackages().get(0).getEnums();
	Assert.assertEquals(1, enums.size());
	OOEnumeration enumeration = enums.get(0);
	Assert.assertEquals("Color", enumeration.getName());
	for (String option : enumeration.getOptions()) {
	    Assert.assertTrue(option.equals("RED") || option.equals("GREEN") || option.equals("BLUE"));
	}
    }

    @Test
    public void enumTypeAndEnumLiteralReferences_shouldTransformToEnumerationTypeAndOptionReference() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("typedef enum Color {RED, GREEN, BLUE}Color;");
	sourceCode.append("int main(void) { Color c = BLUE; return 0;}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOVariableDeclarationList declarationList = (OOVariableDeclarationList) getDefaultClass(model).getMethods().get(0)
		.getStatements().get(0);
	OOVariable declaration = declarationList.getVariableDeclarations().get(0);
	Assert.assertEquals("Color", declaration.getType().getEnumType().getName());
	OOFieldReferenceExpression enumReference = (OOFieldReferenceExpression) declaration.getInitializerExpression();
	Assert.assertEquals("Color", ((OOStringLiteral)enumReference.getFieldOwner()).getValue());
	Assert.assertEquals("BLUE", enumReference.getFieldName());
    }
}
