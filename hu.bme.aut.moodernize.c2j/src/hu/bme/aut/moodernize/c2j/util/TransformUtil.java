package hu.bme.aut.moodernize.c2j.util;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTNode;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;

public class TransformUtil {
    public static boolean isReferenceType(OOType type) {
	return type == null ? false : type.getClassType() != null && type.getArrayDimensions() == 0;
    }

    public static boolean listContainsVariable(List<OOVariable> variables, OOVariable var) {
	for (OOVariable v : variables) {
	    if (v.getName().toUpperCase().equals(var.getName().toUpperCase())) {
		return true;
	    }
	}

	return false;
    }
    
    public static boolean listContainsClass(List<OOClass> classes, OOClass cl) {
	for (OOClass c : classes) {
	    if (c.getName().toUpperCase().equals(cl.getName().toUpperCase())) {
		return true;
	    }
	}

	return false;
    }
    
    public static boolean listContainsMethod(List<OOMethod> methods, OOMethod method) {
	for (OOMethod m : methods) {
	    if (m.getName().toUpperCase().equals(method.getName().toUpperCase())) {
		return true;
	    }
	}

	return false;
    }

    public static OOClass getClassFromClasses(List<OOClass> classes, OOClass cl) {
	for (OOClass c : classes) {
	    if (c.getName().toUpperCase().equals(cl.getName().toUpperCase())) {
		return c;
	    }
	}

	return null;
    }

    public static OOMethod getFunctionByName(List<OOMethod> functions, String name) {
	for (OOMethod function : functions) {
	    if (function.getName().equals(name)) {
		return function;
	    }
	}

	return null;
    }

    public static boolean isCorrectClassName(String name) {
	if (name == null) {
	    return false;
	}
	name = name.toUpperCase();
	boolean isIncorrect = name.equals("ABSTRACT") || name.equals("ASSERT") || name.equals("BOOLEAN")
		|| name.equals("CATCH") || name.equals("CLASS") || name.equals("ENUM") || name.equals("EXTENDS")
		|| name.equals("FINAL") || name.equals("FINALLY") || name.equals("IMPLEMENTS") || name.equals("IMPORT")
		|| name.equals("INSTANCEOF") || name.equals("INTERFACE") || name.equals("NATIVE") || name.equals("NEW")
		|| name.equals("PACKAGE") || name.equals("PRIVATE") || name.equals("PROTECTED") || name.equals("PUBLIC")
		|| name.equals("STATIC") || name.equals("STRICTFP") || name.equals("SUPER")
		|| name.equals("SYNCHRONIZED") || name.equals("THIS") || name.equals("THROW") || name.equals("THROWS")
		|| name.equals("TRANSIENT") || name.equals("TRY") || name.equals("VOLATILE") || name.equals("");

	return !isIncorrect;
    }
    
    public static String getContainingFunctionName(IASTNode node) {
	while (node != null && !(node instanceof IASTFunctionDefinition)) {
	    node = node.getParent();
	}
	
	if (node != null) {
	    return ((IASTFunctionDefinition) node).getDeclarator().getName().resolveBinding().getName();
	} else {
	    return null;
	}
    }
    
    public static OOMethod findAndGetMethodFromClasses(List<OOClass> classes, String methodName) {
	for (OOClass cl : classes) {
	    for (OOMethod method : cl.getMethods()) {
		if (method.getName().equals(methodName)) {
		    return method;
		}
	    }
	}

	return null;
    }
}
