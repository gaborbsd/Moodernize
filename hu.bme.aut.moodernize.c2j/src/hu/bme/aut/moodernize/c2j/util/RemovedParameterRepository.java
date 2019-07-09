package hu.bme.aut.moodernize.c2j.util;

import java.util.HashMap;
import java.util.Map;

public class RemovedParameterRepository {
    private static Map<String, String> functionsAndRemovedParameters = new HashMap<String, String>();
    
    public static void addEntry(String functionName, String parameterName) {
	functionsAndRemovedParameters.put(functionName, parameterName);
    }
    
    public static void clearEntries() {
	functionsAndRemovedParameters.clear();
    }
    
    public static Map<String, String> getEntries() {
	return functionsAndRemovedParameters;
    }
}
