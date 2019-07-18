package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.oogen.OOClass;

public class TransformationDataHolder {
    private static List<OOClass> createdClasses = new ArrayList<OOClass>();
    
    public static void clear() {
	createdClasses.clear();
    }
    
    public static List<OOClass> getCreatedClasses() {
        return createdClasses;
    }
    
    public static void addClass(OOClass cl) {
	createdClasses.add(cl);
    }
}