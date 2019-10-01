package hu.bme.aut.moodernize.c2j.util;

import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;

public class PointerConverter {
    public static void handlePointerConversion(OOExpression operandWithPossiblePointerInIt) {
	if (operandWithPossiblePointerInIt instanceof OOVariableReferenceExpression) {
	    OOVariable referredVariable = ((OOVariableReferenceExpression) operandWithPossiblePointerInIt)
		    .getVariable();
	    OOType type = referredVariable.getType();
	    int numberOfIndirections = type.getNumberOfIndirections();
	    if (numberOfIndirections > 0) {
		for (int i = 1; i <= numberOfIndirections; i++) {
		    type.setArrayDimensions(type.getArrayDimensions() - 1);
		}
	    }
	}
    }
}