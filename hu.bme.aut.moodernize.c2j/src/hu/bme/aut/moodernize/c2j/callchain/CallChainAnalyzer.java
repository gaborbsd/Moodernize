package hu.bme.aut.moodernize.c2j.callchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hu.bme.aut.oogen.OOMethod;

public class CallChainAnalyzer {
    private long maximumInboundCalls;
    private long minimumClassSize;
    private CallGraph callGraph;
    private Map<OOMethod, List<OOMethod>> outsideFunctionCalls = new HashMap<OOMethod, List<OOMethod>>();

    private List<Vertex> vertexList;
    List<Boolean> shouldVisitFlags = new ArrayList<Boolean>();

    public List<List<Vertex>> createClasses(CallGraph callGraph, Map<OOMethod, List<OOMethod>> outsideFunctionCalls) {
	this.callGraph = callGraph;
	this.outsideFunctionCalls = outsideFunctionCalls;
	vertexList = callGraph.getVertexList();

	long numberOfAllFunctions = callGraph.getSize() + outsideFunctionCalls.keySet().stream().distinct().count();
	maximumInboundCalls = calculateMaximumInboundCalls(numberOfAllFunctions);
	minimumClassSize = calculateMinimumClassSize(numberOfAllFunctions);

	List<List<Vertex>> createdClasses = new ArrayList<List<Vertex>>();
	vertexList.forEach(vertex -> shouldVisitFlags.add(true));
	for (int i = 0; i < vertexList.size(); i++) {
	    if (shouldVisitFlags.get(i)) {
		List<Vertex> createdClass = analyze(vertexList.get(i), new ArrayList<Vertex>());
		if (createdClass.size() >= minimumClassSize) {
		    createdClasses.add(createdClass);
		} else {
		    createdClass.forEach(function ->  {
			shouldVisitFlags.set(vertexList.indexOf(function), true);
		    });
		}
	    }
	}

	return createdClasses;
    }

    private List<Vertex> analyze(Vertex function, List<Vertex> currentlyCreatedClass) {
	shouldVisitFlags.set(vertexList.indexOf(function), false);
	long numberOfInboundCalls = calculateNumberOfInboundCalls(function);
	if (numberOfInboundCalls > maximumInboundCalls) {
	    return new ArrayList<Vertex>();
	}
	currentlyCreatedClass.add(function);
	for (Vertex child : callGraph.getChildren(function)) {
	    if (shouldVisitFlags.get(vertexList.indexOf(child))) {
		currentlyCreatedClass = analyze(child, currentlyCreatedClass);
	    }
	}

	return currentlyCreatedClass;
    }

    private long calculateNumberOfInboundCalls(Vertex function) {
	long count = 0;
	for (Entry<OOMethod, List<OOMethod>> entry : outsideFunctionCalls.entrySet()) {
	    for (OOMethod target : entry.getValue()) {
		if (target.getName().equals(function.getName())) {
		    count++;
		}
	    }
	}
	
	return count;
    }

    private int calculateMaximumInboundCalls(long numberOfAllFunctions) {
	return (int) (numberOfAllFunctions * 0.05) + 1;
    }

    private int calculateMinimumClassSize(long numberOfAllFunctions) {
	return 3;
    }
}
