package hu.bme.aut.moodernize.c2j.pointerconversion;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOVariable;

public class PointerConversionDataHolder {
    private static List<PointerAttributes> pointerConversionResults = new ArrayList<PointerAttributes>();

    public static void clear() {
	pointerConversionResults.clear();
    }

    public static void addDeclaration(OOVariable declaration, String scope) {
	if (declaration.getType().isDeclaredAsPointer() && !declarationExists(declaration, scope)) {
	    pointerConversionResults.add(new PointerAttributes(declaration, scope));
	}
    }
    
    public static void setPointerAttribute(OOExpression expressionPossiblyContainingPointer, boolean value,
	    PointerAttributeType type) {
	OOVariable variable = TransformUtil.extractVariableFromExpression(expressionPossiblyContainingPointer);
	if (variable == null) {
	    return;
	}
	PointerAttributes attributes = findAttributesByName(variable.getName());
	if (attributes != null) {
	    switch (type) {
	    case DEREFER:
		attributes.dereferOperatorUsed = value;
		break;
	    case ARITHMETICS:
		attributes.pointerArithmeticsUsed = value;
		break;
	    case INDEX:
		attributes.indexOperatorUsed = value;
		break;
	    case PARAMETER:
		attributes.isParameter = value;
		break;
	    case RETURNVALUE:
		attributes.isReturnValue = value;
		break;
	    }
	}
    }

    public static void writeResultsToFile() {
	PrintWriter writer = null;
	String newLine = System.getProperty("line.separator");

	try {
	    writer = new PrintWriter(new FileWriter("pointer_conversion_ai.txt"));
	    for (PointerAttributes attributes : pointerConversionResults) {
		writer.println(attributes.pointerDeclaration.getName() + "\t" + attributes.indexOperatorUsed + "\t"
			+ attributes.pointerArithmeticsUsed + "\t" + attributes.dereferOperatorUsed + "\t"
			+ attributes.isParameter + "\t" + attributes.isReturnValue + newLine);
	    }
	} catch (IOException e) {

	}
    }
    
    private static boolean declarationExists(OOVariable declaration, String scope) {
	for (PointerAttributes attributes : pointerConversionResults) {
	    if (attributes.pointerDeclaration.getName().equals(declaration.getName()) && attributes.scopeIdentifier.equals(scope)) {
		return true;
	    }    
	}
	return false;
    }

    private static PointerAttributes findAttributesByName(String name) {
	for (PointerAttributes attributes : pointerConversionResults) {
	    if (attributes.pointerDeclaration.getName().equals(name)) {
		return attributes;
	    }
	}
	return null;
    }
}