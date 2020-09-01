package hu.bme.aut.moodernize.c2j.pointerconversion;

import javax.swing.JOptionPane;

import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;

public class PointerConverter {    
    public void handlePointerConversion(OOExpression operandPossiblyContainingPointer) {
	OOVariable variable = TransformUtil.extractVariableFromExpression(operandPossiblyContainingPointer);
	if (variable != null) {
	    changeTypeFromPointerToArray(variable);
	}
    }

    private void changeTypeFromPointerToArray(OOVariable toChange) {
	OOType type = toChange.getType();
	int indirections = type.getPointerIndirections();
	if (indirections > 0) {
	    for (int i = 1; i <= indirections; i++) {
		type.setArrayDimensions(type.getArrayDimensions() + 1);
		type.setPointerIndirections(type.getPointerIndirections() - 1);
	    }
	}
    }

    private void showPointerDialog(String fileName, String pointerName, boolean isTransformedAsArray) {
	int dialogResult = JOptionPane.showConfirmDialog(null, "File: " + fileName + "\nPointer name: " + pointerName,
		"warning", JOptionPane.YES_NO_OPTION);
	if (dialogResult == JOptionPane.YES_OPTION) {
	    // Saving code here
	} else if (dialogResult == JOptionPane.NO_OPTION) {

	}
    }
}