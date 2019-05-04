package hu.bme.aut.moodernize.c2j.util;

import java.util.List;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOType;

public class TransformUtil {
	public static boolean isReferenceType(OOType type) {
		return type == null ? false : type.getClassType() != null && !type.isArray();
	}

	public static OOClass getClassFromClasses(List<OOClass> classes, OOClass cl) {
		for (OOClass c : classes) {
			if (c.equals(cl)) {
				return c;
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
