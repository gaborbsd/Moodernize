package hu.bme.aut.moodernize.c2j.converter.expression;

import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOLogicalExpression;
import hu.bme.aut.oogen.OOLogicalLiteral;
import hu.bme.aut.oogen.OOOneOperandLogicalExpression;
import hu.bme.aut.oogen.OOTwoOperandLogicalExpression;
import hu.bme.aut.oogen.OogenFactory;

public class IntegerLiteralToBooleanConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    private static OOLogicalExpression createBoolFromLogicalInt(OOIntegerLiteral integerLiteral) {
	OOLogicalLiteral logicalLiteral = factory.createOOLogicalLiteral();
	if (integerLiteral.getValue() == 0) {
	    logicalLiteral.setValue(false);
	} else {
	    logicalLiteral.setValue(true);
	}

	return logicalLiteral;
    }
    
    private static OOExpression checkIfExpressionIsIntegerLiteral(OOExpression expression) {
	if (expression instanceof OOIntegerLiteral) {
	    expression = createBoolFromLogicalInt((OOIntegerLiteral) expression);
	}
	
	return expression;
    }
    
    public static void handleIntToBoolConversion(OOTwoOperandLogicalExpression expression) {
	expression.setLeftSide(checkIfExpressionIsIntegerLiteral(expression.getLeftSide()));
	expression.setRightSide(checkIfExpressionIsIntegerLiteral(expression.getRightSide()));	
    }
    
    public static void handleIntToBoolConversion(OOOneOperandLogicalExpression expression) {
	expression.setOperand(checkIfExpressionIsIntegerLiteral(expression.getOperand()));
    }
}
