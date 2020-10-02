package hu.bme.aut.moodernize.c2j.callchain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CallGraph {
    private List<Edge> edgeList = new ArrayList<Edge>();
    private List<Vertex> vertexList = new ArrayList<Vertex>();
    
    public CallGraph(Map<String, String> functionCalls) {
	createGraph(functionCalls);
    }

    public long calculateNumberOfInboundCalls(Vertex function) {
	return edgeList.stream().filter(edge -> edge.getTarget().getName().equals(function.getName())).count();
    }
    
    private void createGraph(Map<String, String> functionCalls) {
	for (Entry<String, String> entry : functionCalls.entrySet()) {
	    Vertex source = new Vertex(entry.getKey());
	    Vertex target = new Vertex(entry.getValue());
	 
	    if (!vertexList.contains(source)) {
		vertexList.add(source);
	    }
	    if (!vertexList.contains(target)) {
		vertexList.add(target);
	    }
	    edgeList.add(new Edge(source, target));
	}
    }
    
    public Set<Vertex> getChildren(Vertex function) {
	Set<Vertex> children = new HashSet<Vertex>();
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
    
    public Set<Vertex> getVertexList() {
        return new HashSet<Vertex>(vertexList);
    }
}
