package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;

import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOComparatorExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOLogicalExpression;
import hu.bme.aut.oogen.OOTwoOperandArithmeticExpression;
import hu.bme.aut.oogen.OOTwoOperandAssignableExpression;
import hu.bme.aut.oogen.OOTwoOperandLogicalExpression;
import hu.bme.aut.oogen.OogenFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

	case IASTBinaryExpression.op_modulo:
	    return setBothSidesAndReturn(factory.createOOModuloExpression(), lhs, rhs);

	case IASTBinaryExpression.op_plusAssign:
	    return setBothSidesWithAssignmentAndReturn(factory.createOOAdditionExpression(), lhs, rhs);

	case IASTBinaryExpression.op_minusAssign:
	    return setBothSidesWithAssignmentAndReturn(factory.createOOSubtractionExpression(), lhs, rhs);

	case IASTBinaryExpression.op_multiplyAssign:
	    return setBothSidesWithAssignmentAndReturn(factory.createOOMultiplicationExpression(), lhs, rhs);

	case IASTBinaryExpression.op_divideAssign:
	    return setBothSidesWithAssignmentAndReturn(factory.createOODivisionExpression(), lhs, rhs);

	case IASTBinaryExpression.op_moduloAssign:
	    return setBothSidesWithAssignmentAndReturn(factory.createOOModuloExpression(), lhs, rhs);

	case IASTBinaryExpression.op_logicalAnd:
	    return setBothSidesAndReturn(factory.createOOAndExpression(), lhs, rhs);

	case IASTBinaryExpression.op_logicalOr:
	    return setBothSidesAndReturn(factory.createOOOrExpression(), lhs, rhs);

	case IASTBinaryExpression.op_equals:
	    return setBothSidesAndReturn(factory.createOOEqualsExpression(), lhs, rhs);

	case IASTBinaryExpression.op_notequals:
	    return setBothSidesAndReturn(factory.createOONotEqualsExpression(), lhs, rhs);

	case IASTBinaryExpression.op_greaterThan:
	    return setBothSidesAndReturn(factory.createOOGreaterThanExpression(), lhs, rhs);

	case IASTBinaryExpression.op_greaterEqual:
	    return setBothSidesAndReturn(factory.createOOGreaterEqualsExpression(), lhs, rhs);

	case IASTBinaryExpression.op_lessThan:
	    return setBothSidesAndReturn(factory.createOOLessThanExpression(), lhs, rhs);

	case IASTBinaryExpression.op_lessEqual:
	    return setBothSidesAndReturn(factory.createOOLessEqualsExpression(), lhs, rhs);

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

	case IASTBinaryExpression.op_binaryAndAssign:
	    return setBothSidesWithAssignmentAndReturn(factory.createOOBitwiseAndExpression(), lhs, rhs);

	case IASTBinaryExpression.op_binaryOrAssign:
	    return setBothSidesWithAssignmentAndReturn(factory.createOOBitwiseOrExpression(), lhs, rhs);

	case IASTBinaryExpression.op_binaryXorAssign:
	    return setBothSidesWithAssignmentAndReturn(factory.createOOBitwiseXorExpression(), lhs, rhs);

	case IASTBinaryExpression.op_shiftLeftAssign:
	    return setBothSidesWithAssignmentAndReturn(factory.createOOBitWiseLeftShift(), lhs, rhs);

	case IASTBinaryExpression.op_shiftRightAssign:
	    return setBothSidesWithAssignmentAndReturn(factory.createOOBitWiseRightShift(), lhs, rhs);

	case IASTBinaryExpression.op_ellipses:
	    // gcc only: (...) range
	    throw new NotImplementedException();

	case IASTBinaryExpression.op_max:
	    //g++ only: >?
	    throw new NotImplementedException();

	case IASTBinaryExpression.op_min:
	    //g++ only: <?
	    throw new NotImplementedException();

	case IASTBinaryExpression.op_pmarrow:
	    //C++ only
	    throw new NotImplementedException();

	case IASTBinaryExpression.op_pmdot:
	    // C== only
	    throw new NotImplementedException();

	default:
	    throw new UnsupportedOperationException("Unsupported binary expression operator encountered " + operator);
	}
    }

    private OOExpression setBothSidesAndReturn(OOTwoOperandArithmeticExpression expression, OOExpression lhs,
	    OOExpression rhs) {
	expression.setLeftSide(lhs);
	expression.setRightSide(rhs);

	return expression;
    }

    private OOExpression setBothSidesAndReturn(OOComparatorExpression expression, OOExpression lhs, OOExpression rhs) {
	expression.setLeftSide(lhs);
	expression.setRightSide(rhs);

	return expression;
    }

    private OOExpression setBothSidesAndReturn(OOTwoOperandLogicalExpression expression, OOExpression lhs,
	    OOExpression rhs) {
	if (lhs instanceof OOIntegerLiteral) {

	    lhs = TypeConverter.createBoolFromLogicalInt((OOIntegerLiteral) lhs);
	}
	if (rhs instanceof OOIntegerLiteral) {
	    rhs = TypeConverter.createBoolFromLogicalInt((OOIntegerLiteral) rhs);
	}
	if (!(lhs instanceof OOLogicalExpression) || !(rhs instanceof OOLogicalExpression)) {
	    throw new IllegalArgumentException(
		    "Both sides of a TwoOperandLogicalExpression must be of type LogicalExpression.");
	}

	expression.setLeftSide((OOLogicalExpression) lhs);
	expression.setRightSide((OOLogicalExpression) rhs);

	return expression;
    }

    private OOExpression setBothSidesWithAssignmentAndReturn(OOTwoOperandAssignableExpression expression,
	    OOExpression lhs, OOExpression rhs) {
	expression.setAssigned(true);
	return setBothSidesAndReturn(expression, lhs, rhs);
    }
}
