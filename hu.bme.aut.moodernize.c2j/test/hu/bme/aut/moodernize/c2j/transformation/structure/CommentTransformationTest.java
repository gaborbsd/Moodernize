package hu.bme.aut.moodernize.c2j.transformation.structure;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOComment;
import hu.bme.aut.oogen.OOEnumeration;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;

/* Struct - Fields
 * Enum
 * Function
 * Statement
 */
public class CommentTransformationTest extends AbstractTransformationTest {
    @Test
    public void structComments_shouldTransformToOOComments() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("// Comment before struct S\n");
	sourceCode.append("struct S {char *c;};\n");
	sourceCode.append("// Comment after struct S");
	
	OOModel model = getModelBySourceCode(sourceCode.toString());
	
	OOClass S = TransformUtil.getClassByName(model.getPackages().get(0).getClasses(), "S");
	List<OOComment> beforeComments = S.getBeforeComments();
	Assert.assertEquals(1, beforeComments.size());
	Assert.assertEquals("// Comment before struct S", beforeComments.get(0).getText());
	
	List<OOComment> afterComments = S.getAfterComments();
	Assert.assertEquals(1, afterComments.size());
	Assert.assertEquals("// Comment after struct S", afterComments.get(0).getText());
    }
    
    @Test
    public void structMemberComments_shouldTransformToOOComments() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S {\n");
	sourceCode.append("char *c;\n\n");
	sourceCode.append("// Comment before member x\n");
	sourceCode.append("int x;\n");
	sourceCode.append("bool b;\n");
	sourceCode.append("};\n");
	
	OOModel model = getModelBySourceCode(sourceCode.toString());
	
	OOClass S = TransformUtil.getClassByName(model.getPackages().get(0).getClasses(), "S");
	for (OOMember member : S.getMembers()) {
	    if (member.getName().equals("x")) {
		List<OOComment> beforeComments = member.getBeforeComments();
		Assert.assertEquals(1, beforeComments.size());
		Assert.assertEquals("// Comment before member x", beforeComments.get(0).getText());
	    }
	}
    }
    
    @Test
    public void EnumComments_shouldTransformToOOComments() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("enum Color {RED, GREEN, BLUE};\n");
	sourceCode.append("// Comment1 after enum\n\n");
	sourceCode.append("// Comment2 after enum\n");
	
	OOModel model = getModelBySourceCode(sourceCode.toString());
	
	OOEnumeration color = model.getPackages().get(0).getEnums().get(0);
	List<OOComment> beforeComments = color.getBeforeComments();
	Assert.assertEquals(0, beforeComments.size());
	
	List<OOComment> afterComments = color.getAfterComments();
	Assert.assertEquals(2, afterComments.size());
	Assert.assertEquals("// Comment1 after enum", afterComments.get(0).getText());
	Assert.assertEquals("// Comment2 after enum", afterComments.get(1).getText());
    }
    
    @Test
    public void functionComments_shouldTransformToOOComments() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("/* Block comment\n");
	sourceCode.append("* before function\n");
	sourceCode.append(" named someFunction*/\n");
	sourceCode.append("void somefunction(void) {}");

	
	OOModel model = getModelBySourceCode(sourceCode.toString());
	
	OOMethod method = getDefaultClass(model).getMethods().get(0);
	List<OOComment> beforeComments = method.getBeforeComments();
	Assert.assertEquals(1, beforeComments.size());
	OOComment comment = beforeComments.get(0);
	Assert.assertTrue(comment.isIsBlockComment());	
	
	List<OOComment> afterComments = method.getAfterComments();
	Assert.assertEquals(0, afterComments.size());
    }
    
    @Test
    public void statementComments_shouldTransformToOOComments() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int foo(void) {}\n");
	sourceCode.append("int main(void) {\n");
	sourceCode.append("	// Define t[] with elements: 1, 2\n");
	sourceCode.append("	int t[] = {1, 2};\n");
	sourceCode.append("	if (t[0] > 0) {\n");
	sourceCode.append("		/* Block comment in if statement */\n");
	sourceCode.append("	}\n");
	sourceCode.append("	return 0;\n");
	sourceCode.append("}");
	
	OOModel model = getModelBySourceCode(sourceCode.toString());
	
	OOMethod main = getDefaultClass(model).getMethods().get(1);
	List<OOStatement> statements = main.getStatements();
	List<OOComment> beforeComments, afterComments;
	
	OOStatement declarationList = statements.get(0);
	beforeComments = declarationList.getBeforeComments();
	afterComments = declarationList.getAfterComments();
	Assert.assertEquals(0, afterComments.size());
	Assert.assertEquals(1, beforeComments.size());
	Assert.assertEquals("// Define t[] with elements: 1, 2", beforeComments.get(0).getText());
	
	OOStatement ifStatement = statements.get(1);
	beforeComments = ifStatement.getBeforeComments();
	afterComments = ifStatement.getAfterComments();
	Assert.assertEquals(0, afterComments.size());
	Assert.assertEquals(1, beforeComments.size());
	Assert.assertEquals("/* Block comment in if statement */", beforeComments.get(0).getText());
	
	OOStatement returnStatement = statements.get(2);
	beforeComments = returnStatement.getBeforeComments();
	afterComments = returnStatement.getAfterComments();
	Assert.assertEquals(0, afterComments.size());
	Assert.assertEquals(0, beforeComments.size());
    }
}
