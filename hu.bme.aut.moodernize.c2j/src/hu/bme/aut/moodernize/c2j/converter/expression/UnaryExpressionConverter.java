package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTUnaryExpression;

import hu.bme.aut.moodernize.c2j.util.IntegerLiteralToBooleanConverter;
import hu.bme.aut.moodernize.c2j.util.PointerConverter;
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
	
	
	return handleByOperator(unaryExpression.getOperator(), TransformUtil.convertExpressionAndProcessPrecedingStatements(converter, unaryExpression.getOperand()));
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
	    PointerConverter.handlePointerConversion(operand);
	    return setOperandAndReturn(factory.createOOPostfixDecrementExpression(), operand);
	case IASTUnaryExpression.op_postFixIncr:
	    PointerConverter.handlePointerConversion(operand);
	    return setOperandAndReturn(factory.createOOPostfixIncrementExpression(), operand);
	case IASTUnaryExpression.op_prefixDecr:
	    PointerConverter.handlePointerConversion(operand);
	    return setOperandAndReturn(factory.createOOPrefixDecrementExpression(), operand);
	case IASTUnaryExpression.op_prefixIncr:
	    PointerConverter.handlePointerConversion(operand);
	    return setOperandAndReturn(factory.createOOPrefixIncrementExpression(), operand);
	case IASTUnaryExpression.op_sizeof:
	    OOEmptyExpression expression = factory.createOOEmptyExpression();
	    return expression;
	    //throw new NotImplementedException();
	case IASTUnaryExpression.op_star:
	    PointerConverter.handlePointerConversion(operand);
	    return operand;
	case IASTUnaryExpression.op_tilde:
	    return setOperandAndReturn(factory.createOOBitWiseComplement(), operand);
	default:
	    return factory.createOOEmptyExpression();
	    // throw new UnsupportedOperationException("Unsupported unary expression operator encountered " + operator);
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
