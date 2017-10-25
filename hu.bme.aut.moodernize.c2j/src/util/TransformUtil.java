package util;

import java.util.List;

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
				return m;
			}
		}
		
		return null;
	}
}
