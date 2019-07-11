package hu.bme.aut.moodernize.c2j.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RemovedParameterRepository {
    private static Map<String, String> removedParameterNames = new HashMap<String, String>();
    private static Map<String, Integer> removedParameterIndices = new HashMap<String, Integer>();
    
    public static void clearEntries() {
	removedParameterNames.clear();
	removedParameterIndices.clear();
    }
    
    public static void addRemovedParameterName(String functionName, String parameterName) {
	removedParameterNames.put(functionName, parameterName);
    }
    
    public static void addRemovedParameterIndex(String functionName, Integer index) {
	removedParameterIndices.put(functionName, index);
    }
    
    public static Set<Map.Entry<String, String>> getRemovedParameterNames() {
	return removedParameterNames.entrySet();
    }
    
    public static Set<Map.Entry<String, Integer>> getRemovedParameterIndices() {
	return removedParameterIndices.entrySet();
    }
}
