package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.moodernize.c2j.callchain.Calledge;
import hu.bme.aut.moodernize.c2j.callchain.Callgraph;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOVariable;

public class TransformationDataRepository {
	private static List<OOClass> classes = new ArrayList<OOClass>();
	private static List<OOVariable> globalVariables = new ArrayList<OOVariable>();
	private static List<OOMethod> functions = new ArrayList<OOMethod>();
	private static Callgraph callGraph = new Callgraph();
	
	public static void resetTransformationData() {
		classes.clear();
		globalVariables.clear();
		functions.clear();
		callGraph.clear();
	}
	
	public static void addClass(OOClass newClass) {
		if (!classes.contains(newClass)) {
			classes.add(newClass);
		}
	}
	
	public static void addGlobalVariable(OOVariable globalVariable) {
		globalVariables.add(globalVariable);
	}
	
	public static void addFunction(OOMethod function) {
		functions.add(function);
	}
	
	public static void addEdgeToCallgraph(String callerName, String calledName) {
		callGraph.add(new Calledge(callerName, calledName));
	}
	
	public static List<OOClass> getClasses() {
		return classes;
	}

	public static List<OOVariable> getGlobalVariables() {
		return globalVariables;
	}

	public static List<OOMethod> getFunctions() {
		return functions;
	}

	public static Callgraph getCallGraph() {
		return callGraph;
	}
}
