package hu.bme.aut.moodernize.c2j.transformation.logic.statement;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOEmptyStatement;
import hu.bme.aut.oogen.OOFor;
import hu.bme.aut.oogen.OOLessThanExpression;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;

public class ForStatementTransformationTest extends AbstractTransformationTest {
    //TODO: Foreach on collections
    //TODO: i = i + 1 -> i++;
    @Test
    public void initStatementVariableDeclaration_shouldTransformToOOVariable() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	for (int i = 0; i < 10; i = i + 1);");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement statement = getDefaultClass(model).getMethods().get(0).getStatements().get(0);
	Assert.assertTrue(statement instanceof OOFor);
	OOFor forStatement = (OOFor) statement;
	Assert.assertTrue(forStatement.getInitStatement() instanceof OOVariable);
	OOVariable declaredVariable = (OOVariable) forStatement.getInitStatement();
	Assert.assertEquals("i", declaredVariable.getName());
	Assert.assertTrue(declaredVariable.getType().getBaseType() == OOBaseType.INT);
    }

    @Test
    public void initStatementVariableReference_shouldTransformToOOVariableReference() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	for (i = 0; i < 10; i = i + 1);");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement statement = getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(statement instanceof OOFor);
	OOFor forStatement = (OOFor) statement;
	Assert.assertTrue(forStatement.getInitStatement() instanceof OOAssignmentExpression);
	OOAssignmentExpression assignmentExpression = ((OOAssignmentExpression) forStatement.getInitStatement());
	OOVariable referredVariable = ((OOVariableReferenceExpression) assignmentExpression.getLeftSide())
		.getVariable();
	Assert.assertEquals("i", referredVariable.getName());
	Assert.assertTrue(referredVariable.getType().getBaseType() == OOBaseType.INT);
    }
    
    @Test
    public void initStatementEmptyStatement_shouldTransformToOOEmptyStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	for (; i < 10; i = i + 1);");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement statement = getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(statement instanceof OOFor);
	OOFor forStatement = (OOFor) statement;
	Assert.assertTrue(forStatement.getInitStatement() instanceof OOEmptyStatement);
    }
    
    @Test
    public void oneStatementWithoutBraces_shouldTransformToOneStatementFor() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	for (i = 0; i < 10; i = i + 1) i = i + 1;");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement statement = getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(statement instanceof OOFor);
	OOFor forStatement = (OOFor) statement;
	Assert.assertEquals(1, forStatement.getBodyStatements().size());
    }
    
    @Test
    public void oneStatementWithBraces_shouldTransformToOneStatementFor() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	for (i = 0; i < 10; i = i + 1) {i = i + 1;}");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	OOStatement statement = getDefaultClass(model).getMethods().get(0).getStatements().get(1);
	Assert.assertTrue(statement instanceof OOFor);
	OOFor forStatement = (OOFor) statement;
	Assert.assertEquals(1, forStatement.getBodyStatements().size());
    }
    
    @Test
    public void comprehensive_shouldTransformToCorrespondingForStatement() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int main(void) {");
	sourceCode.append("	int i;");
	sourceCode.append("	for (int i = 0; i < 100; i = i + 1) { i = 2 * i; if (i != 100) { i = 100; } ");
	sourceCode.append("}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOStatement> statements = getDefaultClass(model).getMethods().get(0).getStatements();
	Assert.assertEquals(2, statements.size());
	
	OOFor forStatement = (OOFor) statements.get(1);
	Assert.assertTrue(forStatement.getInitStatement() instanceof OOVariable);
	Assert.assertTrue(forStatement.getCondition() instanceof OOLessThanExpression);
	Assert.assertTrue(forStatement.getIncrementExpression() instanceof OOAssignmentExpression);
	Assert.assertEquals(2, forStatement.getBodyStatements().size());
    }
}
