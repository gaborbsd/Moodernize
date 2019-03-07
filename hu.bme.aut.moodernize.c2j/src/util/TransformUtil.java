package util;

import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;

public class TransformUtil {
	public static boolean isReferenceType(OOType type) {
		return type == null ? false : type.getClassType() != null && !type.isArray();
	}
	
	public static boolean listContainsOOVariable(List<OOVariable> list, OOVariable var) {
		for (OOVariable element : list) {
			if (element.equals(var)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static OOClass getClassFromStructs(List<OOClass> structs, OOClass cl) {
		for (OOClass c : structs) {
			if (c.equals(cl)) {
				return c;
			}
		}

		return null;
	}
	
	public static OOMethod getMethodByName(List<OOMethod> methods, String name) {
		for (OOMethod m : methods) {
			if (m.getName().equals(name)) {
				return (OOMethod)EcoreUtil.copy(m);
			}
		}
		return null;
	}
	
	public static String capitalizeFirst(String string) {
		if (string == null || string.isEmpty()) {
			return string;
		}
		if (string.length() == 1) {
			return string.toUpperCase();
		}
		
		return string.substring(0, 1).toUpperCase() + string.substring(1);
	}
	
	public static boolean isCorrectClassName(String name) {
		if (name == null) {
			return false;
		}
		name = name.toUpperCase();
		boolean isIncorrect = 
				   name.equals("ABSTRACT") || name.equals("ASSERT") || name.equals("BOOLEAN")
				|| name.equals("CATCH") || name.equals("CLASS" ) ||name.equals("ENUM")
				|| name.equals("EXTENDS") || name.equals("FINAL") || name.equals("FINALLY")
				|| name.equals("IMPLEMENTS") || name.equals("IMPORT") || name.equals("INSTANCEOF") || name.equals("INTERFACE")
				|| name.equals("NATIVE") || name.equals("NEW") || name.equals("PACKAGE") 
				|| name.equals("PRIVATE") || name.equals("PROTECTED") || name.equals("PUBLIC")
				|| name.equals("STATIC") || name.equals("STRICTFP") || name.equals("SUPER")
				|| name.equals("SYNCHRONIZED") || name.equals("THIS") || name.equals("THROW")
				|| name.equals("THROWS") || name.equals("TRANSIENT") || name.equals("TRY")
				|| name.equals("VOLATILE") || name.equals("");
		
		return !isIncorrect;
	}
}
