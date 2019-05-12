package hu.bme.aut.moodernize.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.moodernize.util.TransformUtil;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;

public class FunctionToClassAssigner {
	private List<OOClass> classes;
	private List<OOMethod> functions;
	private List<OOMethod> toRemove = new ArrayList<OOMethod>();


	public FunctionToClassAssigner(List<OOClass> classes, List<OOMethod> functions) {
		super();
		this.classes = classes;
		this.functions = functions;
	}

	public void assignFunctionsToClasses() {
		for (OOMethod function : functions) {
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
					assignFunctionToClass(TransformUtil.getClassFromClasses(classes, target), function);
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
			assignFunctionToClass(TransformUtil.getClassFromClasses(classes, target), function);
		}
	}

	private OOClass getTargetClass(OOClass target) {
		for (OOClass cl : classes) {
			if (cl.getName().equals(target.getName())) {
				return cl;
			}
		}

		return null;
	}

	private void removeAssignedFunctionsFromGlobalFunctions() {
		for (OOMethod function : toRemove) {
			functions.remove(function);
		}
	}

	private void assignFunctionToClass(OOClass target, OOMethod function) {
		OOMethod newMethod = EcoreUtil.copy(function);
		OOType returnType = newMethod.getReturnType();

		if (returnType != null && TransformUtil.isReferenceType(returnType)) {
			newMethod.setReturnType(null);
		}
		removeTargetClassParameterFromFunction(newMethod, target);

		target.getMethods().add(newMethod);
		toRemove.add(function);
	}

	private void removeTargetClassParameterFromFunction(OOMethod from, OOClass referenceType) {
		if (from == null || referenceType == null) {
			return;
		}
		Iterator<OOVariable> iterator = from.getParameters().iterator();
		while (iterator.hasNext()) {
			OOVariable parameter = iterator.next();
			if (TransformUtil.isReferenceType(parameter.getType()) && parameter.getType().getClassType().equals(referenceType)) {
				iterator.remove();
			}
		}
	}
}
