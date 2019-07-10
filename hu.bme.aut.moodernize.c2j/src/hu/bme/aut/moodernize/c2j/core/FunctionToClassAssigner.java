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

public class FunctionToClassAssigner {
    private List<OOClass> classes;
    private List<OOMethod> functions;
    private List<OOMethod> toRemove = new ArrayList<OOMethod>();
    private boolean transformed = false;

    public FunctionToClassAssigner(List<OOClass> classes, List<OOMethod> functions) {
	super();
	this.classes = classes;
	this.functions = functions;
    }

    public void assignFunctionsToClasses() {
	for (OOMethod function : functions) {
	    checkReturnType(function);
	    checkParameterList(function);
	}
	removeAssignedFunctionsFromGlobalFunctions();
    }

    private void checkReturnType(OOMethod function) {
	OOType returnType = function.getReturnType();
	if (returnType != null) {
	    OOClass referenceReturnType = returnType.getClassType();
	    if (referenceReturnType != null) {
		OOClass target = getTargetClass(referenceReturnType);
		if (target != null) {
		    assignFunctionToClass(function, TransformUtil.getClassFromClasses(classes, target));
		    transformed = true;
		}
	    }
	}
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
	    if (transformed) {
		OOClass returnClassType = function.getReturnType().getClassType();
		if (returnClassType != null && target.getName().equals(returnClassType.getName())) {
		    removeTargetClassParameterFromMethod(target, function);
		}
	    } else {
		assignFunctionToClass(function, TransformUtil.getClassFromClasses(classes, target));
		removeTargetClassParameterFromMethod(target, function);
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

    private void removeTargetClassParameterFromMethod(OOClass toRemove, OOMethod from) {
	if (from == null || toRemove == null) {
	    return;
	}

	OOMethod method = TransformUtil.findAndGetMethodFromClasses(classes, from.getName());
	if (method == null) {
	    method = TransformUtil.getFunctionByName(functions, from.getName());
	}

	Iterator<OOVariable> iterator = method.getParameters().iterator();
	while (iterator.hasNext()) {
	    OOVariable parameter = iterator.next();
	    if (TransformUtil.isReferenceType(parameter.getType())
		    && parameter.getType().getClassType().getName().equals(toRemove.getName())) {
		storeRemovedParameter(from.getName(), parameter.getName());
		iterator.remove();
		break;
	    }
	}
    }
    
    private void storeRemovedParameter(String functionName, String parameterName) {
	RemovedParameterRepository.addEntry(functionName, parameterName);
    }
}
