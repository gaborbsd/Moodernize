package hu.bme.aut.moodernize.c2j.converter.statement;

import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTStatement;

import hu.bme.aut.moodernize.c2j.converter.expression.ExpressionConverter;
import hu.bme.aut.oogen.OOStatement;

public class StatementConverter {
	public OOStatement convertStatement(IASTStatement statement) {
		if (statement instanceof IASTExpressionStatement) {
			return convertExpressionStatement((IASTExpressionStatement) statement);
		}
		
		throw new UnsupportedOperationException("The following statement type is not yet supported: " + statement);
	}
	
	private OOStatement convertExpressionStatement(IASTExpressionStatement expressionStatement) {
		ExpressionConverter converter = new ExpressionConverter();		
		return converter.convertExpression(expressionStatement.getExpression());
	}
}
