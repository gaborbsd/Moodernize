package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;

import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOLogicalExpression;
import hu.bme.aut.oogen.OOTwoOperandArithmeticExpression;
import hu.bme.aut.oogen.OOTwoOperandLogicalExpression;
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
	    return setBothSidesAndReturn(factory.createOOAdditionExpression(), lhs, rhs);

	case IASTBinaryExpression.op_minus:
	    return setBothSidesAndReturn(factory.createOOSubtractionExpression(), lhs, rhs);

	case IASTBinaryExpression.op_multiply:
	    return setBothSidesAndReturn(factory.createOOMultiplicationExpression(), lhs, rhs);

	case IASTBinaryExpression.op_divide:
	    return setBothSidesAndReturn(factory.createOODivisionExpression(), lhs, rhs);

	case IASTBinaryExpression.op_logicalAnd:
	    return setBothSidesAndReturn(factory.createOOAndExpression(), lhs, rhs);

	case IASTBinaryExpression.op_logicalOr:
	    return setBothSidesAndReturn(factory.createOOOrExpression(), lhs, rhs);

	case IASTBinaryExpression.op_binaryAnd:
	    return setBothSidesAndReturn(factory.createOOBitwiseAndExpression(), lhs, rhs);

	case IASTBinaryExpression.op_binaryOr:
	    return setBothSidesAndReturn(factory.createOOBitwiseOrExpression(), lhs, rhs);

	case IASTBinaryExpression.op_binaryXor:
	    return setBothSidesAndReturn(factory.createOOBitwiseXorExpression(), lhs, rhs);

	case IASTBinaryExpression.op_shiftLeft:
	    return setBothSidesAndReturn(factory.createOOBitWiseLeftShift(), lhs, rhs);

	case IASTBinaryExpression.op_shiftRight:
	    return setBothSidesAndReturn(factory.createOOBitWiseRightShift(), lhs, rhs);

	default:
	    throw new UnsupportedOperationException(
		    "The following binary expression operator is not yet supported: " + operator);
	}
    }

    private OOExpression setBothSidesAndReturn(OOTwoOperandArithmeticExpression expression, OOExpression lhs,
	    OOExpression rhs) {
	expression.setLeftSide(lhs);
	expression.setRightSide(rhs);

	return expression;
    }

    private OOExpression setBothSidesAndReturn(OOTwoOperandLogicalExpression expression, OOExpression lhs,
	    OOExpression rhs) {
	if (!(lhs instanceof OOLogicalExpression) || !(rhs instanceof OOLogicalExpression)) {
	    throw new IllegalArgumentException(
		    "Both sides of a TwoOperandLogicalExpression must be of type LogicalExpression.");
	}

	expression.setLeftSide((OOLogicalExpression) lhs);
	expression.setRightSide((OOLogicalExpression) rhs);

	return expression;
    }
}
