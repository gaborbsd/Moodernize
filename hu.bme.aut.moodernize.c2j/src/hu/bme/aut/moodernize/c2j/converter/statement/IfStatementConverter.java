package hu.bme.aut.moodernize.c2j.converter.statement;

import org.eclipse.cdt.core.dom.ast.IASTIfStatement;
import org.eclipse.cdt.core.dom.ast.IASTStatement;

import hu.bme.aut.oogen.OOIf;
import hu.bme.aut.oogen.OogenFactory;

public class IfStatementConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOIf convertIfStatement(IASTIfStatement statement) {
	CompoundStatementConverter converter = new CompoundStatementConverter();
	OOIf ifStatement = factory.createOOIf();

	ifStatement.setCondition(converter.convertConditionExpression(statement.getConditionExpression()));
	converter.addStatementsToBody(statement.getThenClause(), ifStatement.getBodyStatements());
	boolean transformedElseIf = handleElseIfs(ifStatement, statement.getElseClause());
	if (!transformedElseIf) {
	    converter.addStatementsToBody(statement.getElseClause(), ifStatement.getElseStatements());
	}

	return ifStatement;
    }

    private boolean handleElseIfs(OOIf ifStatement, IASTStatement elseClause) {
	if (elseClause instanceof IASTIfStatement) {
	    ifStatement.setElseIf(convertIfStatement((IASTIfStatement) elseClause));
	    return true;
	}

	return false;
    }
}
