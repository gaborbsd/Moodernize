package hu.bme.aut.moodernize.c2j.projectcreation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hu.bme.aut.moodernize.c2j.callchain.FunctionCallMapHolder;
import hu.bme.aut.moodernize.c2j.callchain.Vertex;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOVisibility;
import hu.bme.aut.oogen.OogenFactory;

public class CallChainClassCreator {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public List<OOClass> createCallChainClasses(List<List<Vertex>> createdClasses, List<OOMethod> globalFunctions,
	    FunctionCallMapHolder functionCalls) {
	List<OOClass> callChainClasses = new ArrayList<OOClass>();
	for (List<Vertex> createdClass : createdClasses) {
	    if (createdClasses == null || createdClasses.isEmpty()) {
		continue;
	    }
	    OOClass callChainClass = factory.createOOClass();
	    callChainClass.setName(
		    TransformUtil.getWithUpperCaseFirstCharacter(createdClass.get(0).getName()) + "To" + TransformUtil
			    .getWithUpperCaseFirstCharacter(createdClass.get(createdClass.size() - 1).getName()));
	    for (Vertex function : createdClass) {
		OOMethod correspondingFunction = TransformUtil.getFunctionByName(globalFunctions, function.getName());
		if (correspondingFunction == null) {
		    continue;
		}
		correspondingFunction.setStatic(true);
		correspondingFunction
			.setVisibility(calculateVisibility(correspondingFunction, functionCalls, createdClass));

		callChainClass.getMethods().add(correspondingFunction);
		globalFunctions.remove(correspondingFunction);
	    }
	    callChainClasses.add(callChainClass);
	}

	return callChainClasses;
    }

    private OOVisibility calculateVisibility(OOMethod correspondingFunction, FunctionCallMapHolder functionCalls,
	    List<Vertex> createdClass) {
	if (correspondingFunction.getName().equals("main")) {
	    return OOVisibility.PUBLIC;
	}
	if (functionIsCalledByFunctions(correspondingFunction, functionCalls.getCallGraphFunctionCalls(), createdClass)
		|| functionIsCalledByFunctions(correspondingFunction, functionCalls.getOutsideFunctionCalls(), createdClass)) {
	    return OOVisibility.PUBLIC;
	}
	
	return OOVisibility.PRIVATE;
    }
    
    private boolean functionIsCalledByFunctions(OOMethod correspondingFunction, Map<OOMethod, List<OOMethod>> functionCalls,
	    List<Vertex> createdClass) {
	for (Entry<OOMethod, List<OOMethod>> entry : functionCalls.entrySet()) {
	    if (isInCreatedClass(entry.getKey().getName(), createdClass)) {
		continue;
	    }
	    if (entry.getValue().stream().filter(target -> target.getName().equals(correspondingFunction.getName()))
		    .count() >= 1) {
		return true;
	    }
	}
	
	return false;
    }

    private boolean isInCreatedClass(String name, List<Vertex> createdClass) {
	return createdClass.stream().filter(function -> function.getName().equals(name)).count() >= 1;
    }
}
