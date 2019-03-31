package hu.bme.aut.moodernize.c2j.callchain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Callgraph {
	List<Calledge> edges = new LinkedList<Calledge>();
	
	public void add(Calledge edge) {
		if (!edges.contains(edge)) {
			edges.add(edge);
		}
	}
	
	public void clear() {
		edges.clear();
	}
	
	public void removeNodeIfExists(String nodeName) {
		Iterator<Calledge> iter = edges.iterator();
		while (iter.hasNext()) {
			Calledge e = iter.next();
			if (e.getCaller().equals(nodeName) || e.getTarget().equals(nodeName)) {
				iter.remove();
			}
		}
	}
	
	public int calculateMaximumInDegree(int numberOfFunctions) {
		return (numberOfFunctions / 5) + 1;
	}
	
	public int calculateMinimumClassSize(int numberOfFunctions) {
		return (numberOfFunctions / 10) + 1 + 1;
	}
	
	public List<String> getDistinctNodes() {
		List<String> nodes = new ArrayList<String>();
		for (Calledge edge : edges) {
			String caller = edge.getCaller();
			String target = edge.getTarget();
			if (!nodes.contains(caller) ) {
				nodes.add(caller);
			}
			if (!nodes.contains(target) ) {
				nodes.add(target);
			}
		}
		return nodes;
	}
	
	public int getInDegree(String node) {
		int inDegree = 0;
		for (Calledge edge : edges) {
			if (!edge.getCaller().equals(node) && edge.getTarget().equals(node)) {
				inDegree++;
			}
		}
		return inDegree;
	}
	
	public List<String> getChildren(String node) {
		List<String> children = new ArrayList<String>();
		
		for (Calledge edge : edges) {
			if (edge.getCaller().equals(node)) {
				children.add(edge.getTarget());
			}
		}
		
		return children;
	}
}
