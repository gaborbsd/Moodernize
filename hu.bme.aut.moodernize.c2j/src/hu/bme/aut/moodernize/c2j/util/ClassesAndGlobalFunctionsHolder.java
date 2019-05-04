package hu.bme.aut.moodernize.c2j.util;

import java.util.List;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;

public class ClassesAndGlobalFunctionsHolder {
	public List<OOClass> classes;
	public List<OOMethod> functions;
	
	public ClassesAndGlobalFunctionsHolder(List<OOClass> classes, List<OOMethod> functions) {
		this.classes = classes;
		this.functions = functions;
	}
}
