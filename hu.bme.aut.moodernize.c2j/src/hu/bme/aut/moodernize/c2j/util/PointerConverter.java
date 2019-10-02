package hu.bme.aut.moodernize.c2j.util;

import hu.bme.aut.moodernize.c2j.dataholders.FunctionVariableTypesDataHolder;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;

public class PointerConverter {
    public static void handlePointerConversion(OOExpression operandPossiblyContainingPointer) {
	if (operandPossiblyContainingPointer instanceof OOVariableReferenceExpression) {
	    OOVariable referredVariable = ((OOVariableReferenceExpression) operandPossiblyContainingPointer)
		    .getVariable();

	    OOVariable correspondingDeclaration = FunctionVariableTypesDataHolder
		    .getCorrespondingVariable(referredVariable);
	    if (correspondingDeclaration != null) {
		OOType type = correspondingDeclaration.getType();
		int numberOfIndirections = type.getNumberOfIndirections();
		if (numberOfIndirections > 0) {
		    for (int i = 1; i <= numberOfIndirections; i++) {
			type.setArrayDimensions(type.getArrayDimensions() - 1);
		    }
		}
	    }
	}
    }
}