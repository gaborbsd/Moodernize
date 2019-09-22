package hu.bme.aut.moodernize.c2j.dataholders;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.oogen.OOClass;

public class TransformationDataHolder {
    public static List<OOClass> createdClasses = new ArrayList<OOClass>();
    
    public static void clear() {
	createdClasses.clear();
    }
}