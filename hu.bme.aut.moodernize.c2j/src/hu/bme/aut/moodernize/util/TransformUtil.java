package hu.bme.aut.moodernize.util;

import java.util.List;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;

public class TransformUtil {
	public static boolean isReferenceType(OOType type) {
		return type == null ? false : type.getClassType() != null && !type.isArray();
	}
	
	public static boolean listContainsVariable(List<OOVariable> variables, OOVariable var) {
		for (OOVariable v : variables) {
			if (v.getName().equals(var.getName())) {
				return true;
			}
		}
		
		return false;
	}

	public static OOClass getClassFromClasses(List<OOClass> classes, OOClass cl) {
		for (OOClass c : classes) {
			if (c.equals(cl)) {
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
}
