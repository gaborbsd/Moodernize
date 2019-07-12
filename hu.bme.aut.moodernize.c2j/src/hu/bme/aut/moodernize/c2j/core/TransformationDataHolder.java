package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;

public class TransformationDataHolder {
    private static List<OOClass> createdClasses = new ArrayList<OOClass>();
    private static List<OOMethod> functions = new ArrayList<OOMethod>();
    
    public static void clear() {
	createdClasses.clear();
	functions.clear();
    }
    
    public static List<OOClass> getCreatedClasses() {
        return createdClasses;
    }
    
    public static List<OOMethod> getFunctions() {
        return functions;
    }
    
    public static void addClass(OOClass cl) {
	createdClasses.add(cl);
    }
    
    public static void addFunction(OOMethod function) {
	functions.add(function);
    }
}