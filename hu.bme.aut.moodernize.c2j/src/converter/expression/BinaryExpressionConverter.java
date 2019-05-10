package converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;

import hu.bme.aut.oogen.OOAdditionExpression;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OogenFactory;

public class BinaryExpressionConverter {
	private static OogenFactory factory = OogenFactory.eINSTANCE;

	public OOExpression convertBinaryExpression(IASTBinaryExpression binaryExpression) {
		ExpressionConverter converter = new ExpressionConverter();
		OOExpression lhs = converter.convertExpression(binaryExpression.getOperand1());
		OOExpression rhs = converter.convertExpression(binaryExpression.getOperand2());
		int operator = binaryExpression.getOperator();

		return handleByOperator(operator, lhs, rhs);
	}

	private OOExpression handleByOperator(int operator, OOExpression lhs, OOExpression rhs) {
		switch (operator) {
		case IASTBinaryExpression.op_assign:
			OOAssignmentExpression assignmentExpression = factory.createOOAssignmentExpression();
			assignmentExpression.setLeftSide(lhs);
			assignmentExpression.setRightSide(rhs);
			return assignmentExpression;
		case IASTBinaryExpression.op_plus:
			OOAdditionExpression additionExpression = factory.createOOAdditionExpression();
			additionExpression.setLeftSide(lhs);
			additionExpression.setRightSide(rhs);
			return additionExpression;
			
		default: throw new UnsupportedOperationException("The following binary expression is not yet supported: " + operator);
		}
	}
}
