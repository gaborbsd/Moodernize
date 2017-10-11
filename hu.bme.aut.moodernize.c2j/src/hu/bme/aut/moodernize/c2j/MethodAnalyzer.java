package hu.bme.aut.moodernize.c2j;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;

public class MethodAnalyzer {
	public static void analyze(List<OOClass> structs, List<OOMethod> globalFunctions) {
		checkParameterLists(structs, globalFunctions);
	}
	
	/* If formal parameter list contains exactly one reference type,
	 * add that method to the corresponding reference type's to-be-generated class.
	 * */	
	private static void checkParameterLists(List<OOClass> structs, List<OOMethod> globalFunctions) {
		//We need this to avoid concurrent modification exception
		//TODO: Copy workaround concurrent exceptionre?
		List<OOMethod> toRemove = new ArrayList<OOMethod>();
		for (OOMethod m : globalFunctions) {
			List<OOVariable> refParams = getReferenceTypeParameters(m);
			if (refParams.size() == 1) {
				OOVariable refParam = refParams.get(0);
				OOClass correspondingStruct = getStructByName(refParam.getType().getClassType().getName(), structs);
				if (correspondingStruct != null) {
					m.getParameters().remove(refParam);
					correspondingStruct.getMethods().add((OOMethod)EcoreUtil.copy(m));
					toRemove.add(m);
				}
			}
		}
		for (OOMethod m : toRemove) {
			globalFunctions.remove(m);
		}
	}

	private static List<OOVariable> getReferenceTypeParameters(OOMethod m) {
		List<OOVariable> refParams = new ArrayList<OOVariable>();
		for (OOVariable param : m.getParameters()) {
			OOType paramType = param.getType();
			if (!paramType.isArray() && paramType.getClassType() != null) {
				refParams.add(param);
			}
		}
		return refParams;
	}
	
	private static OOClass getStructByName(String name, List<OOClass> structs) {
		for (OOClass struct : structs) {
			if (struct.getName().equals(name)) {
				return struct;
			}
		}
		return null;
	}
}
