package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;

import hu.bme.aut.oogen.OOExpression;

public class ExpressionConverter {
	public OOExpression convertExpression(IASTExpression expression) {
		if (expression instanceof IASTBinaryExpression) {
			return convertBinaryExpression((IASTBinaryExpression) expression);
		} else if (expression instanceof IASTLiteralExpression) {
			return convertLiteralExpression((IASTLiteralExpression) expression);
		} else if (expression instanceof IASTIdExpression) {
			return convertIdExpression((IASTIdExpression) expression);
		}
		
		throw new UnsupportedOperationException("The following expression type is not yet supported: " + expression);
	}
	
	private OOExpression convertBinaryExpression(IASTBinaryExpression binaryExpression) {
		BinaryExpressionConverter binaryConverter = new BinaryExpressionConverter();
		return binaryConverter.convertBinaryExpression(binaryExpression);
	}
	
	private OOExpression convertLiteralExpression(IASTLiteralExpression literalExpression) {
		LiteralExpressionConverter literalConverter = new LiteralExpressionConverter();
		return literalConverter.convertLiteralExpression(literalExpression);
	}
	
	private OOExpression convertIdExpression(IASTIdExpression idExpression) {
		IdExpressionConverter idConverter = new IdExpressionConverter();
		return idConverter.convertIdExpression(idExpression);
	}
}
