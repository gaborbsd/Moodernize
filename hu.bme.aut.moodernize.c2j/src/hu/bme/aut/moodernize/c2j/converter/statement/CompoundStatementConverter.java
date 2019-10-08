package hu.bme.aut.moodernize.c2j.converter.statement;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTStatement;

import hu.bme.aut.moodernize.c2j.converter.expression.ExpressionConverter;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOStatement;

public class CompoundStatementConverter {
    public OOExpression convertConditionExpression(IASTExpression conditionExpression) {
	OOExpression convertedExpression = TransformUtil
		.convertExpressionAndProcessPrecedingStatements(new ExpressionConverter(), conditionExpression);
	// TODO: Convert IntLiteral to BoolLiteral

	return convertedExpression;
    }

    public void addStatementsToBody(IASTStatement[] statements, List<OOStatement> body) {
	if (statements == null || body == null) {
	    return;
	}
	StatementConverter converter = new StatementConverter();

	for (IASTStatement statement : statements) {
	    if (statement != null) {
		body.add(converter.convertStatement(statement));
	    }
	}

    }

    public void addStatementsToBody(IASTStatement statements, List<OOStatement> body) {
	if (statements == null || body == null) {
	    return;
	}
	StatementConverter converter = new StatementConverter();

	if (statements instanceof IASTCompoundStatement) {
	    addStatementsToBody(((IASTCompoundStatement) statements).getStatements(), body);
	} else {
	    body.add(converter.convertStatement(statements));
	}
    }
}
