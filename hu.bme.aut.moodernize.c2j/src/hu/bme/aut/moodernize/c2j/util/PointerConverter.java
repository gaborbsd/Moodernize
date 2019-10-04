package hu.bme.aut.moodernize.c2j.util;

import hu.bme.aut.moodernize.c2j.dataholders.FunctionVariableTypesDataHolder;
import hu.bme.aut.moodernize.c2j.dataholders.TransformationDataHolder;
import hu.bme.aut.moodernize.c2j.projectcreation.MainClassCreator;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFieldReferenceExpression;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;

public class PointerConverter {
    public static void handlePointerConversion(OOExpression operandPossiblyContainingPointer) {
	if (operandPossiblyContainingPointer instanceof OOFieldReferenceExpression) {
	    String referredName = ((OOFieldReferenceExpression) operandPossiblyContainingPointer).getFieldName();
	    if (TransformUtil.isGlobalVariable(referredName)) {
		OOClass mainClass = TransformUtil.getClassByName(TransformationDataHolder.createdClasses,
			MainClassCreator.MAINCLASSNAME);
		for (OOMember member : mainClass.getMembers()) {
		    if (member.getName().equals(referredName)) {
			changeTypeFromArrayToPointer(member);
			break;
		    }
		}
	    }
	} else if (operandPossiblyContainingPointer instanceof OOVariableReferenceExpression) {
	    OOVariable referredVariable = ((OOVariableReferenceExpression) operandPossiblyContainingPointer)
		    .getVariable();
	    OOVariable correspondingDeclaration = FunctionVariableTypesDataHolder
		    .getCorrespondingVariable(referredVariable);
	    if (correspondingDeclaration != null) {
		changeTypeFromArrayToPointer(correspondingDeclaration);
	    }

	}
    }

    private static void changeTypeFromArrayToPointer(OOVariable toChange) {
	OOType type = toChange.getType();
	int numberOfIndirections = type.getNumberOfIndirections();
	if (numberOfIndirections > 0) {
	    for (int i = 1; i <= numberOfIndirections; i++) {
		type.setArrayDimensions(type.getArrayDimensions() - 1);
	    }
	}
    }
}