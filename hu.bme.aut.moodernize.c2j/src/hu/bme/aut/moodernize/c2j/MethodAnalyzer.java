package hu.bme.aut.moodernize.c2j;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOVariable;

public class MethodAnalyzer {

	public static void moveToReferenceTypes(List<OOClass> structs, List<OOMethod> methods) {
		ArrayList<OOMethod> toRemove = new ArrayList<OOMethod>();
		for (OOMethod method : methods) {
			List<OOVariable> refParams = getReferenceTypeParameters(method);
			if (refParams.size() == 1) {
				String name = refParams.get(0).getType().getClassType().getName();
				for (OOClass struct : structs) {
					if (struct.getName().equals(name)) {
						OOMethod m = (OOMethod)EcoreUtil.copy(method);
						m.getParameters().remove(getReferenceTypeParameters(m).get(0));
						struct.getMethods().add(m);
						toRemove.add(method);
						break;
					}
				}
			}
		}
		for (OOMethod m : toRemove) {
			methods.remove(m);
		}
	}
	
	private static List<OOVariable> getReferenceTypeParameters(OOMethod method) {
		ArrayList<OOVariable> refParams = new ArrayList<OOVariable>();
		for (OOVariable param: method.getParameters()) {
			if (param.getType().getClassType() != null) {
				refParams.add(param);
			}
		}
		
		return refParams;
	}
}
