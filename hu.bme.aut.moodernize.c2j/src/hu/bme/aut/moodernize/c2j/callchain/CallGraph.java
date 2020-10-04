package hu.bme.aut.moodernize.c2j.callchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOMethod;

public class CallGraph {
    private List<Edge> edgeList = new ArrayList<Edge>();
    private List<Vertex> vertexList = new ArrayList<Vertex>();

    public CallGraph(Map<OOMethod, List<OOMethod>> functionCalls, List<OOMethod> globalFunctions) {
	createGraph(functionCalls, globalFunctions);
    }

    private void createGraph(Map<OOMethod, List<OOMethod>> functionCalls, List<OOMethod> globalFunctions) {
	for (Entry<OOMethod, List<OOMethod>> entry : functionCalls.entrySet()) {
	    Vertex source = new Vertex(entry.getKey().getName());
	    if (!vertexList.contains(source)) {
		vertexList.add(source);
	    }
	    for (OOMethod targetMethod : entry.getValue()) {
		Vertex target = new Vertex(targetMethod.getName());
		if (TransformUtil.listContainsMethod(globalFunctions, targetMethod) && !vertexList.contains(target)) {
		    vertexList.add(target);
		    edgeList.add(new Edge(source, target));
		}
	    }
	}
    }

    public List<Vertex> getChildren(Vertex function) {
	List<Vertex> children = new ArrayList<Vertex>();
	for (Edge edge : edgeList) {
	    if (edge.getSource().equals(function) && !(children.contains(edge.getTarget()))) {
		children.add(edge.getTarget());
	    }
	}

	return children;
    }

    public long getSize() {
	return vertexList.size();
    }

    public List<Vertex> getVertexList() {
	return new ArrayList<Vertex>(vertexList);
    }
}
