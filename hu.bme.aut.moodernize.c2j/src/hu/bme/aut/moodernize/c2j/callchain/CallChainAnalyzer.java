package hu.bme.aut.moodernize.c2j.callchain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CallChainAnalyzer {
    private long maximumInboundCalls;
    private long minimumClassSize;
    private CallGraph callGraph;

    public Set<Set<Vertex>> createClasses(CallGraph callGraph, Map<String, String> outsideFunctionCalls) {
	this.callGraph = callGraph;
	long numberOfAllFunctions = callGraph.getSize() + outsideFunctionCalls.keySet().stream().distinct().count();
	maximumInboundCalls = calculateMaximumInboundCalls(numberOfAllFunctions);
	minimumClassSize = calculateMinimumClassSize(numberOfAllFunctions);

	Set<Set<Vertex>> createdClasses = new HashSet<Set<Vertex>>();
	Set<Vertex> functionsNotYetAssignedToAClass = callGraph.getVertexList();
	for (Vertex function : functionsNotYetAssignedToAClass) {
	    Set<Vertex> createdClass = analyze(functionsNotYetAssignedToAClass, function, new HashSet<Vertex>());
	    if (createdClass.size() >= minimumClassSize) {
		createdClasses.add(createdClass);
	    } else {
		functionsNotYetAssignedToAClass.addAll(createdClass);
	    }
	}

	return createdClasses;
    }

    private Set<Vertex> analyze(Set<Vertex> functionsNotYetAssignedToAClass, Vertex function,
	    Set<Vertex> currentlyCreatedClass) {
	functionsNotYetAssignedToAClass.remove(function);
	long numberOfInboundCalls = callGraph.calculateNumberOfInboundCalls(function);
	if (numberOfInboundCalls > maximumInboundCalls) {
	    return new HashSet<Vertex>();
	}
	currentlyCreatedClass.add(function);
	for (Vertex child : callGraph.getChildren(function)) {
	    if (functionsNotYetAssignedToAClass.contains(child)) {
		currentlyCreatedClass = analyze(functionsNotYetAssignedToAClass, child, currentlyCreatedClass);
	    }
	}

	return currentlyCreatedClass;
    }

    private int calculateMaximumInboundCalls(long numberOfAllFunctions) {
	return (int) (numberOfAllFunctions * 0.05) + 1 ;
    }

    private int calculateMinimumClassSize(long numberOfAllFunctions) {
	return 0;
    }
}
