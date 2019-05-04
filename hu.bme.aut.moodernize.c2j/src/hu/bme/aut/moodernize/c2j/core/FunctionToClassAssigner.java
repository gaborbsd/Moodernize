package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.moodernize.c2j.util.ClassesAndGlobalFunctionsHolder;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;

public class FunctionToClassAssigner {
	private ClassesAndGlobalFunctionsHolder classesAndFunctions;
	private List<OOMethod> toRemove = new ArrayList<OOMethod>();

	public FunctionToClassAssigner(ClassesAndGlobalFunctionsHolder classesAndFunctions) {
		this.classesAndFunctions = classesAndFunctions;
	}

	public void assignFunctionsToClasses() {
		for (OOMethod function : classesAndFunctions.functions) {
			boolean transformedByReturnType = checkReturnType(function);
			if (!transformedByReturnType) {
				checkParameterList(function);
			}
		}
		removeAssignedFunctionsFromGlobalFunctions();
	}

	private boolean checkReturnType(OOMethod function) {
		boolean transformedByReturnType = false;

		OOType returnType = function.getReturnType();
		if (returnType != null) {
			OOClass referenceReturnType = returnType.getClassType();
			if (referenceReturnType != null) {
				OOClass target = getTargetClass(referenceReturnType);
				if (target != null) {
					assignFunctionToClass(TransformUtil.getClassFromClasses(classesAndFunctions.classes, target), function);
					transformedByReturnType = true;
				}
			}
		}

		return transformedByReturnType;
	}

	private void checkParameterList(OOMethod function) {
		List<OOVariable> parameters = function.getParameters();
		List<OOClass> parameterReferenceTypes = new ArrayList<OOClass>();
		for (OOVariable parameter : parameters) {
			OOClass referenceType = parameter.getType().getClassType();
			if (referenceType != null) {
				parameterReferenceTypes.add(referenceType);
			}
		}

		if (parameterReferenceTypes.size() == 1) {
			OOClass target = parameterReferenceTypes.get(0);
			assignFunctionToClass(TransformUtil.getClassFromClasses(classesAndFunctions.classes, target), function);
		}
	}

	private OOClass getTargetClass(OOClass target) {
		for (OOClass cl : classesAndFunctions.classes) {
			if (cl.getName().equals(target.getName())) {
				return cl;
			}
		}

		return null;
	}

	private void removeAssignedFunctionsFromGlobalFunctions() {
		for (OOMethod function : toRemove) {
			classesAndFunctions.functions.remove(function);
		}
	}

	private void assignFunctionToClass(OOClass target, OOMethod function) {
		OOMethod newMethod = (OOMethod) EcoreUtil.copy(function);
		OOType returnType = newMethod.getReturnType();

		if (returnType != null && TransformUtil.isReferenceType(returnType)) {
			newMethod.setReturnType(null);
		}
		removeReferenceTypeParameter(newMethod, target);

		target.getMethods().add(newMethod);
		toRemove.add(function);
	}

	private void removeReferenceTypeParameter(OOMethod from, OOClass ref) {
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
