package hu.bme.aut.moodernize.c2j.util;

import hu.bme.aut.moodernize.c2j.dataholders.FunctionSymbolTable;
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
	    OOFieldReferenceExpression fieldReference = (OOFieldReferenceExpression) operandPossiblyContainingPointer;
	    String referredName = fieldReference.getFieldName();
	    if (TransformUtil.isGlobalVariable(referredName)) {
		OOClass mainClass = TransformUtil.getClassByName(TransformationDataHolder.createdClasses,
			MainClassCreator.MAINCLASSNAME);
		if (mainClass != null) {
		    findMemberAndChangeTypeFromArrayToPointer(mainClass, referredName);
		}
	    } else {
		OOExpression ownerExpression = fieldReference.getFieldOwner();
		if (ownerExpression instanceof OOVariableReferenceExpression) {
		    OOVariable referredVariable = ((OOVariableReferenceExpression) ownerExpression).getVariable();
		    OOClass classType = referredVariable.getType().getClassType();
		    OOClass ownerClass = TransformUtil.getClassByName(TransformationDataHolder.createdClasses,
			    classType.getName());
		    if (ownerClass != null) {
			findMemberAndChangeTypeFromArrayToPointer(ownerClass, referredName);
		    }
		}
	    }
	} else if (operandPossiblyContainingPointer instanceof OOVariableReferenceExpression) {
	    OOVariable referredVariable = ((OOVariableReferenceExpression) operandPossiblyContainingPointer)
		    .getVariable();
	    OOVariable correspondingDeclaration = FunctionSymbolTable.getCorrespondingVariable(referredVariable);
	    if (correspondingDeclaration != null) {
		changeTypeFromArrayToPointer(correspondingDeclaration);
	    }
	}
    }

    private static void findMemberAndChangeTypeFromArrayToPointer(OOClass cl, String memberName) {
	for (OOMember member : cl.getMembers()) {
	    if (member.getName().equals(memberName)) {
		changeTypeFromArrayToPointer(member);
		break;
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