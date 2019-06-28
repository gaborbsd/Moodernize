package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTUnaryExpression;

import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOOneOperandArithmeticExpression;
import hu.bme.aut.oogen.OogenFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UnaryExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    
    public OOExpression convertUnaryExpression(IASTUnaryExpression unaryExpression) {
	ExpressionConverter converter = new ExpressionConverter();
	return handleByOperator(unaryExpression.getOperator(), converter.convertExpression(unaryExpression.getOperand()));
    }
    
    private OOExpression handleByOperator(int operator, OOExpression operand) {
	switch (operator) {
	case IASTUnaryExpression.op_amper:
	    throw new NotImplementedException();
	case IASTUnaryExpression.op_bracketedPrimary:
	    return setOperandAndReturn(factory.createOOBracketedExpression(), operand);
	case IASTUnaryExpression.op_minus:
	    throw new NotImplementedException();
	case IASTUnaryExpression.op_not:
	    throw new NotImplementedException();
	case IASTUnaryExpression.op_plus:
	    throw new NotImplementedException();
	case IASTUnaryExpression.op_postFixDecr:
	    return setOperandAndReturn(factory.createOOPostfixDecrementExpression(), operand);
	case IASTUnaryExpression.op_postFixIncr:
	    return setOperandAndReturn(factory.createOOPostfixIncrementExpression(), operand);
	case IASTUnaryExpression.op_prefixDecr:
	    return setOperandAndReturn(factory.createOOPrefixDecrementExpression(), operand);
	case IASTUnaryExpression.op_prefixIncr:
	    return setOperandAndReturn(factory.createOOPrefixIncrementExpression(), operand);
	case IASTUnaryExpression.op_sizeof:
	    throw new NotImplementedException();
	case IASTUnaryExpression.op_star:
	    throw new NotImplementedException();
	case IASTUnaryExpression.op_tilde:
	    throw new NotImplementedException();
	default:
	    throw new UnsupportedOperationException("Unsupported unary expression operator encountered " + operator);
	}
    }
    
    private OOExpression setOperandAndReturn(OOOneOperandArithmeticExpression expression, OOExpression operand) {
	expression.setOperand(operand);
	return expression;
    }
}
