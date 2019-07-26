package hu.bme.aut.moodernize.c2j.dataholders;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.oogen.OOFunctionCallExpression;

public class FunctionCallExpressionDataHolder {
    private static String functionName = "";
    private static List<OOFunctionCallExpression> previousFunctionCallsInFunction = new ArrayList<OOFunctionCallExpression>();
    
    public static String getFunctionName() {
        return functionName;
    }
    
    public static List<OOFunctionCallExpression> getPreviousFunctionCallsInFunction() {
	return previousFunctionCallsInFunction;
    }
    
    public static void addFunctionCall(OOFunctionCallExpression functionCall) {
	previousFunctionCallsInFunction.add(functionCall);
    }
    
    public static void clearFunctionCalls(String newFunctionName) {
	functionName = newFunctionName;
	previousFunctionCallsInFunction.clear();
    }
}
