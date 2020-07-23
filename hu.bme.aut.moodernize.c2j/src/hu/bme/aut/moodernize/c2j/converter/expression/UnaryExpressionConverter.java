package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTUnaryExpression;

import hu.bme.aut.moodernize.c2j.util.IntegerLiteralToBooleanConverter;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOBracketedExpression;
import hu.bme.aut.oogen.OOEmptyExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOOneOperandArithmeticExpression;
import hu.bme.aut.oogen.OOOneOperandLogicalExpression;
import hu.bme.aut.oogen.OogenFactory;

public class UnaryExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOExpression convertUnaryExpression(IASTUnaryExpression unaryExpression) {
	ExpressionConverter converter = new ExpressionConverter();
	return handleByOperator(unaryExpression.getOperator(),
		TransformUtil.convertExpressionAndProcessPrecedingStatements(converter, unaryExpression.getOperand()));
    }

    private OOExpression handleByOperator(int operator, OOExpression operand) {
	switch (operator) {
	case IASTUnaryExpression.op_amper:
	    return operand;
	case IASTUnaryExpression.op_bracketedPrimary:
	    return setOperandAndReturn(factory.createOOBracketedExpression(), operand);
	case IASTUnaryExpression.op_minus:
	    return setOperandAndReturn(factory.createOOMinusExpression(), operand);
	case IASTUnaryExpression.op_not:
	    return setOperandAndReturn(factory.createOONotExpression(), operand);
	case IASTUnaryExpression.op_plus:
	    return setOperandAndReturn(factory.createOOPlusExpression(), operand);
	case IASTUnaryExpression.op_postFixDecr:
	    return setOperandAndReturn(factory.createOOPostfixDecrementExpression(), operand);
	case IASTUnaryExpression.op_postFixIncr:
	    return setOperandAndReturn(factory.createOOPostfixIncrementExpression(), operand);
	case IASTUnaryExpression.op_prefixDecr:
	    return setOperandAndReturn(factory.createOOPrefixDecrementExpression(), operand);
	case IASTUnaryExpression.op_prefixIncr:
	    return setOperandAndReturn(factory.createOOPrefixIncrementExpression(), operand);
	case IASTUnaryExpression.op_sizeof:
	    OOEmptyExpression emptyExpression = factory.createOOEmptyExpression();
	    TransformUtil.createAndAttachNotRecognizedErrorComment(emptyExpression,
		    "Error: sizeof expression not supported yet");
	    return emptyExpression;
	// throw new NotImplementedException();
	case IASTUnaryExpression.op_star:
	    return operand;
	case IASTUnaryExpression.op_tilde:
	    return setOperandAndReturn(factory.createOOBitWiseComplement(), operand);
	default:
	    OOEmptyExpression expression = factory.createOOEmptyExpression();
	    TransformUtil.createAndAttachNotRecognizedErrorComment(expression,
		    "Error: Unsupported unary expression operator");
	    return expression;
	// throw new UnsupportedOperationException("Unsupported unary expression
	// operator encountered " + operator);
	}
    }

    private OOExpression setOperandAndReturn(OOBracketedExpression expression, OOExpression operand) {
	expression.setOperand(operand);
	IntegerLiteralToBooleanConverter.handleIntToBoolConversion(expression);
	return expression;
    }

    private OOExpression setOperandAndReturn(OOOneOperandArithmeticExpression expression, OOExpression operand) {
	expression.setOperand(operand);
	return expression;
    }

    private OOExpression setOperandAndReturn(OOOneOperandLogicalExpression expression, OOExpression operand) {
	expression.setOperand(operand);
	IntegerLiteralToBooleanConverter.handleIntToBoolConversion(expression);
	return expression;
    }
}
