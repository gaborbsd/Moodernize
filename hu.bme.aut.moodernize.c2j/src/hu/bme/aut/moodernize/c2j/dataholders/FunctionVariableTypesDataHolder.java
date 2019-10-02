package hu.bme.aut.moodernize.c2j.dataholders;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;

public class FunctionVariableTypesDataHolder {
    public static OOType returnType = null;
    public static List<OOVariable> parameters = new ArrayList<OOVariable>();
    public static List<OOVariable> variableDeclarations = new ArrayList<OOVariable>();

    public static OOVariable getCorrespondingVariable(OOVariable referredVariable) {
	for (OOVariable variable : variableDeclarations) {
	    if (variable.getName().equals(referredVariable.getName())) {
		return variable;
	    }
	}
	
	for (OOVariable variable : parameters) {
	    if (variable.getName().equals(referredVariable.getName())) {
		return variable;
	    }
	}
	
	return null;
    }
    
    public static void clear() {
	returnType = null;
	parameters.clear();
	variableDeclarations.clear();
    }
}