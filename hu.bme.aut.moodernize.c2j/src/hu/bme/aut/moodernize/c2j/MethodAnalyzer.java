package hu.bme.aut.moodernize.c2j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OogenFactory;
import util.Callgraph;
import util.TransformUtil;

public class MethodAnalyzer {
	private static OogenFactory factory = OogenFactory.eINSTANCE;

	private static List<Boolean> shouldVisitFlags;
	private static int maxInDegree;
	private static List<String> nodes;
	private static int minClassSize;
	private static Callgraph cg;
	private static List<OOMethod> functions;

	public static Set<OOClass> analyze(List<OOClass> structs, List<OOMethod> globalFunctions, Callgraph callGraph) {
		cg = callGraph;
		functions = globalFunctions;
		int numberOfFunctions = functions.size() - 1;
		
		checkParameterLists(structs);
		return checkCallHierarchy(numberOfFunctions);
	}

	private static void checkParameterLists(List<OOClass> structs) {
		Iterator<OOMethod> iter = functions.iterator();
		while (iter.hasNext()) {
			OOMethod m = iter.next();
			OOType returnType = m.getReturnType();

			// TODO: Cleanup: Extract body to a method.
			if (TransformUtil.isReferenceType(returnType)) {
				OOClass correspondingStruct = TransformUtil.getClassFromStructs(structs, returnType.getClassType());
				if (correspondingStruct != null) {
					addMethodToClass(m, correspondingStruct);
					iter.remove();
					cg.removeNodeIfExists(m.getName());
				}
			} else {
				List<OOClass> refParams = getReferenceTypeParameters(m);
				if (refParams.size() == 1) {
					OOClass correspondingStruct = TransformUtil.getClassFromStructs(structs, refParams.get(0));
					if (correspondingStruct != null) {
						addMethodToClass(m, correspondingStruct);
						iter.remove();
						cg.removeNodeIfExists(m.getName());
					}
				}
			}
		}
	}

	private static Set<OOClass> checkCallHierarchy(int numberOfFunctions) {
		nodes = cg.getDistinctNodes();
		shouldVisitFlags = new ArrayList<Boolean>();
		for (int i = 0; i < nodes.size(); i++) {
			shouldVisitFlags.add(true);
		}
		maxInDegree = cg.calculateMaximumInDegree(numberOfFunctions);
		minClassSize = cg.calculateMinimumClassSize(numberOfFunctions);
		Set<OOClass> classes = new HashSet<OOClass>();

		for (int i = 0; i < nodes.size(); i++) {
			if (shouldVisitFlags.get(i)) {
				String f = nodes.get(i);
				OOClass cl = factory.createOOClass();
				cl.setName(f.toUpperCase());
				cl = analyzeChain(f, cl);

				if (cl.getMethods().size() >= minClassSize) {
					classes.add(cl);
				} else {
					for (OOMethod m : cl.getMethods()) {
						shouldVisitFlags.set(nodes.indexOf(m.getName()), true);
					}
				}
			}
		}

		for (OOClass cl : classes) {
			for (OOMethod m : cl.getMethods()) {
				Iterator<OOMethod> iter = functions.iterator();
				while (iter.hasNext()) {
					OOMethod f = iter.next();
					if (m.getName().equals(f.getName())) {
						iter.remove();
					}
				}
			}
		}
		return classes;
	}

	private static OOClass analyzeChain(String node, OOClass cl) {
		shouldVisitFlags.set(nodes.indexOf(node), false);
		int inDegree = cg.getInDegree(node);

		if (inDegree > maxInDegree) {
			return cl;
		}
		OOMethod method = TransformUtil.getMethodByName(functions, node);
		if (method != null) {
			cl.getMethods().add(method);
		}

		for (String child : cg.getChildren(node)) {
			if (shouldVisitFlags.get(nodes.indexOf(child))) {
				analyzeChain(child, cl);
			}
		}

		return cl;
	}

	private static void addMethodToClass(OOMethod m, OOClass cl) {
		OOMethod newMethod = (OOMethod) EcoreUtil.copy(m);
		OOType returnType = newMethod.getReturnType();
		if (returnType != null && TransformUtil.isReferenceType(returnType)) {
			newMethod.setReturnType(null);
		}
		removeReferenceTypeParameter(newMethod, cl);
		cl.getMethods().add(newMethod);
	}

	private static List<OOClass> getReferenceTypeParameters(OOMethod m) {
		List<OOClass> refParams = new ArrayList<OOClass>();
		for (OOVariable param : m.getParameters()) {
			OOType paramType = param.getType();
			if (TransformUtil.isReferenceType(paramType) && !refParams.contains(paramType.getClassType())) {
				refParams.add(paramType.getClassType());
			}
		}
		return refParams;
	}

	private static void removeReferenceTypeParameter(OOMethod from, OOClass ref) {
		if (from == null || ref == null) {
			return;
		}
		Iterator<OOVariable> iter = from.getParameters().iterator();
		while (iter.hasNext()) {
			OOVariable param = iter.next();
			if (TransformUtil.isReferenceType(param.getType()) && param.getType().getClassType().equals(ref)) {
				iter.remove();
			}
		}
	}
}
