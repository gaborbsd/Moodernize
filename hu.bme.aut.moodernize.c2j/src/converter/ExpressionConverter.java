package converter;

import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;

import hu.bme.aut.oogen.OOExpression;

public class ExpressionConverter {
	public OOExpression convertExpression(IASTExpression expression) {
		if (expression instanceof IASTBinaryExpression) {
			return convertBinaryExpression((IASTBinaryExpression) expression);
		} else if (expression instanceof IASTLiteralExpression) {
			return convertLiteralExpression((IASTLiteralExpression) expression);
		}
		return null;
	}
	
	private OOExpression convertBinaryExpression(IASTBinaryExpression binaryExpression) {
		BinaryExpressionConverter binaryConverter = new BinaryExpressionConverter();
		return binaryConverter.convertBinaryExpression(binaryExpression);
	}
	
	private OOExpression convertLiteralExpression(IASTLiteralExpression literalExpression) {
		LiteralExpressionConverter literalConverter = new LiteralExpressionConverter();
		return literalConverter.convertLiteralExpression(literalExpression);
	}
}
