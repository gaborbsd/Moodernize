package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;

import hu.bme.aut.moodernize.c2j.util.IntegerLiteralToBooleanConverter;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOComparatorExpression;
import hu.bme.aut.oogen.OOEmptyExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOTwoOperandArithmeticExpression;
import hu.bme.aut.oogen.OOTwoOperandAssignableExpression;
import hu.bme.aut.oogen.OOTwoOperandLogicalExpression;
import hu.bme.aut.oogen.OogenFactory;

public class BinaryExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOExpression convertBinaryExpression(IASTBinaryExpression binaryExpression) {
	if (binaryExpression.getOperator() == IASTBinaryExpression.op_assign) {
	    return new AssignmentExpressionConverter().convertAssignmentExpression(binaryExpression);
	}
	
	ExpressionConverter converter = new ExpressionConverter();
	OOExpression lhs = TransformUtil.convertExpressionAndProcessPrecedingStatements(converter, binaryExpression.getOperand1());
	OOExpression rhs = TransformUtil.convertExpressionAndProcessPrecedingStatements(converter, binaryExpression.getOperand2());
	int operator = binaryExpression.getOperator();

	return handleByOperator(operator, lhs, rhs);
    }
    
    private OOExpression handleByOperator(int operator, OOExpression lhs, OOExpression rhs) {
	switch (operator) {
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
	    OOEmptyExpression emptyExpression = factory.createOOEmptyExpression();
	    TransformUtil.createAndAttachNotRecognizedErrorComment(emptyExpression, "Error: Unsupported expression: op_ellipses");
	    return emptyExpression;
	    // throw new NotImplementedException();

	case IASTBinaryExpression.op_max:
	    // g++ only: >?
	    emptyExpression = factory.createOOEmptyExpression();
	    TransformUtil.createAndAttachNotRecognizedErrorComment(emptyExpression, "Error: Unsupported expression: op_max");
	    return emptyExpression;
	    // throw new NotImplementedException();

	case IASTBinaryExpression.op_min:
	    // g++ only: <?
	    emptyExpression = factory.createOOEmptyExpression();
	    TransformUtil.createAndAttachNotRecognizedErrorComment(emptyExpression, "Error: Unsupported expression: op_min");
	    return emptyExpression;
	    // throw new NotImplementedException();

	case IASTBinaryExpression.op_pmarrow:
	    // C++ only
	    emptyExpression = factory.createOOEmptyExpression();
	    TransformUtil.createAndAttachNotRecognizedErrorComment(emptyExpression, "Error: Unsupported expression: op_pmarrow");
	    return emptyExpression;
	    // throw new NotImplementedException();

	case IASTBinaryExpression.op_pmdot:
	    // C== only
	    emptyExpression = factory.createOOEmptyExpression();
	    TransformUtil.createAndAttachNotRecognizedErrorComment(emptyExpression, "Error: Unsupported expression: op_pmdot");
	    return emptyExpression;
	    //throw new NotImplementedException();

	default:
	    emptyExpression = factory.createOOEmptyExpression();
	    TransformUtil.createAndAttachNotRecognizedErrorComment(emptyExpression, "Error: Unsupported binary expression operator");
	    return emptyExpression;
	    // throw new UnsupportedOperationException("Unsupported binary expression operator encountered " + operator);
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
	IntegerLiteralToBooleanConverter.handleIntToBoolConversion(expression);
	return expression;
    }

    private OOExpression setBothSidesAndReturn(OOTwoOperandLogicalExpression expression, OOExpression lhs,
	    OOExpression rhs) {
	expression.setLeftSide(lhs);
	expression.setRightSide(rhs);
	IntegerLiteralToBooleanConverter.handleIntToBoolConversion(expression);
	return expression;
    }

    private OOExpression setBothSidesWithAssignmentAndReturn(OOTwoOperandAssignableExpression expression,
	    OOExpression lhs, OOExpression rhs) {
	expression.setAssigned(true);
	return setBothSidesAndReturn(expression, lhs, rhs);
    }
}
