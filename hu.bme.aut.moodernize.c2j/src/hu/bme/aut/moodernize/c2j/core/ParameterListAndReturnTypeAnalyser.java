package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.moodernize.c2j.util.RemovedParameterRepository;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;

public class ParameterListAndReturnTypeAnalyser {
    private List<OOClass> classes;
    private List<OOMethod> functions;
    private List<OOMethod> toRemove = new ArrayList<OOMethod>();
    private boolean transformedByReturnType = false;

    public ParameterListAndReturnTypeAnalyser(List<OOClass> classes, List<OOMethod> functions) {
	super();
	this.classes = classes;
	this.functions = functions;
    }

    public void assignFunctionsToClasses() {
	for (OOMethod function : functions) {
	    checkReturnType(function);
	    checkParameterList(function);
	    transformedByReturnType = false;
	}
	removeAssignedFunctionsFromGlobalFunctions();
	for (OOMethod remainingFunction : functions) {
	    remainingFunction.setStatic(true);
	}
    }

    private void checkReturnType(OOMethod function) {
	OOType returnType = function.getReturnType();
	if (returnType != null) {
	    OOClass referenceReturnType = returnType.getClassType();
	    if (referenceReturnType != null) {
		OOClass target = getTargetClass(referenceReturnType);
		if (target != null) {
		    assignFunctionToClass(function, TransformUtil.getClassFromClasses(classes, target));
		    transformedByReturnType = true;
		}
	    }
	}
    }

    private void checkParameterList(OOMethod function) {
	List<OOVariable> parameters = function.getParameters();
	List<OOClass> parameterReferenceTypes = new ArrayList<OOClass>();
	
	for (OOVariable parameter : parameters) {
	    OOClass referenceType = parameter.getType().getClassType();
	    if (referenceType != null && !TransformUtil.listContainsClass(parameterReferenceTypes, referenceType)) {
		parameterReferenceTypes.add(referenceType);
	    }
	}
	
	if (!transformedByReturnType && parameterReferenceTypes.size() == 1) {
	    OOClass target = parameterReferenceTypes.get(0);
	    removeTargetClassParametersFirstOccurenceFromMethod(target, function);
	    assignFunctionToClass(function, TransformUtil.getClassFromClasses(classes, target));
	} else if (transformedByReturnType) {
	    OOClass returnClassType = function.getReturnType().getClassType();
	    OOClass matchingTypeParameter = TransformUtil.getClassFromClasses(parameterReferenceTypes, returnClassType);
	    if (matchingTypeParameter != null) {
		removeTargetClassParametersFirstOccurenceFromMethod(matchingTypeParameter, function);
	    }
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

    private void assignFunctionToClass(OOMethod function, OOClass target) {
	target.getMethods().add(EcoreUtil.copy(function));
	toRemove.add(function);
    }

    private void removeTargetClassParametersFirstOccurenceFromMethod(OOClass toRemove, OOMethod from) {
	if (from == null || toRemove == null) {
	    return;
	}

	OOMethod method;
	if (transformedByReturnType) {
	    method = TransformUtil.findAndGetMethodFromClasses(classes, from.getName());
	} else {
	    method = TransformUtil.getFunctionByName(functions, from.getName());
	}

	Iterator<OOVariable> iterator = method.getParameters().iterator();
	int index = 0;
	while (iterator.hasNext()) {
	    OOVariable parameter = iterator.next();
	    if (TransformUtil.isReferenceType(parameter.getType())
		    && parameter.getType().getClassType().getName().equals(toRemove.getName())) {
		storeRemovedParameter(from.getName(), parameter.getName(), index);
		iterator.remove();
		break;
	    }
	    index++;
	}
    }

    private void storeRemovedParameter(String functionName, String parameterName, Integer index) {
	RemovedParameterRepository.addRemovedParameterName(functionName, parameterName);
	RemovedParameterRepository.addRemovedParameterIndex(functionName, index);
    }
}
