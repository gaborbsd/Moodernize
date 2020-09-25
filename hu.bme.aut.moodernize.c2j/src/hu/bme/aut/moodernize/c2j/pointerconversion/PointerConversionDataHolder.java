package hu.bme.aut.moodernize.c2j.pointerconversion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOVariable;

public class PointerConversionDataHolder {
    private static boolean isEnabled = false;
    
    private static List<PointerAttributes> pointerConversionResults = new ArrayList<PointerAttributes>();

    public static void clear() {
	pointerConversionResults.clear();
    }

    public static void addDeclaration(OOVariable declaration, String scope) {
	if (!isEnabled) {
	    return;
	}
	
	
	if (declaration.getType().isDeclaredAsPointer() && !declarationExists(declaration, scope)) {
	    pointerConversionResults.add(new PointerAttributes(declaration, scope));
	}
    }

    public static void setPointerAttribute(OOExpression expressionPossiblyContainingPointer, boolean value,
	    PointerAttributeType type) {
	if (!isEnabled) {
	    return;
	}
	
	
	OOVariable variable = TransformUtil.extractVariableFromExpression(expressionPossiblyContainingPointer);
	if (variable == null) {
	    return;
	}
	PointerAttributes attributes = findAttributes(variable.getName(), "TODO:SCOPE");
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
	File file = new File("pointer_conversion_ai.txt");
	Writer writer = null;
	try {
	    file.createNewFile();
	    writer = new BufferedWriter(new FileWriter(file, true));
	    for (PointerAttributes attributes : pointerConversionResults) {
		writer.append(generateFileContent(attributes));
	    }
	    writer.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static boolean declarationExists(OOVariable declaration, String scope) {
	return findAttributes(declaration.getName(), scope) != null;
    }

    private static PointerAttributes findAttributes(String name, String scope) {
	for (PointerAttributes attributes : pointerConversionResults) {
	    if (attributes.pointerDeclaration.getName().equals(name) && attributes.scopeIdentifier.equals(scope)) {
		return attributes;
	    }
	}
	return null;
    }

    private static String generateFileContent(PointerAttributes attributes) {
	String content = "";

	content += attributes.indexOperatorUsed ? "1\t" : "0\t";
	content += attributes.pointerArithmeticsUsed ? "1\t" : "0\t";
	content += attributes.dereferOperatorUsed ? "1\t" : "0\t";
	content += attributes.isParameter ? "1\t" : "0\t";
	content += attributes.isReturnValue ? "1\t" : "0\t";
	content += attributes.dereferOperatorUsed ? "1\t" : "0\t";
	content += attributes.indexOperatorUsed ? "1\t" : "0\t";
	content += attributes.isTransformedAsArray ? "1\t" : "0\t";
	content += "1\n";

	return content;
    }
}