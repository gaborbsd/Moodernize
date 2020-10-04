package hu.bme.aut.moodernize.c2j.callchain;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import hu.bme.aut.oogen.OOMethod;

public class FunctionCallMapHolder {
    private Map<OOMethod, List<OOMethod>> callGraphFunctionCalls = new HashMap<OOMethod, List<OOMethod>>();
    private Map<OOMethod, List<OOMethod>> outsideFunctionCalls = new HashMap<OOMethod, List<OOMethod>>();
    
    public void addCallGraphEntry(OOMethod source, List<OOMethod> targets) {
	List<OOMethod> targetList = callGraphFunctionCalls.get(source);
	if (targetList == null) {
	    targetList = new ArrayList<OOMethod>();
	    callGraphFunctionCalls.put(source, targetList);
	}
	targetList.addAll(targets);
    }
    
    public void addOutsideFunctionEntry(OOMethod source, List<OOMethod> targets) {
	List<OOMethod> targetList = outsideFunctionCalls.get(source);
	if (targetList == null) {
	    targetList = new ArrayList<OOMethod>();
	    outsideFunctionCalls.put(source, targetList);
	}
	targetList.addAll(targets);
    }
    
    public Map<OOMethod, List<OOMethod>> getCallGraphFunctionCalls() {
        return callGraphFunctionCalls;
    }

    public Map<OOMethod, List<OOMethod>> getOutsideFunctionCalls() {
        return outsideFunctionCalls;
    }
}
