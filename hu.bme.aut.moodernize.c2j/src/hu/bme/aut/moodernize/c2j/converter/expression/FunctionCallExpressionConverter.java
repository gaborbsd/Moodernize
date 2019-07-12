package hu.bme.aut.moodernize.c2j.converter.expression;

import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializerClause;

import hu.bme.aut.moodernize.c2j.converter.declaration.InitializerConverter;
import hu.bme.aut.moodernize.c2j.util.RemovedParameterRepository;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OogenFactory;

public class FunctionCallExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    private boolean ownerSet = false;

    public OOFunctionCallExpression convertFunctionCallExpression(IASTFunctionCallExpression call) {
	OOFunctionCallExpression functionCall = factory.createOOFunctionCallExpression();
	
	handleFunctionName(functionCall, call);
	handleFunctionArguments(functionCall, call);
	handleFunctionOwner(functionCall, call);
	
	return functionCall;
    }

    private void handleFunctionName(OOFunctionCallExpression functionCall, IASTFunctionCallExpression call) {
	IASTExpression functionNameExpression = call.getFunctionNameExpression();
	if (!(functionNameExpression instanceof IASTIdExpression)) {
	    throw new UnsupportedOperationException(
		    "Unexpected function call NameExpression encountered: " + functionNameExpression);
	}
	
	functionCall.setFunctionName(((IASTIdExpression)functionNameExpression).getName().resolveBinding().getName());
    }
    
    private void handleFunctionArguments(OOFunctionCallExpression functionCall, IASTFunctionCallExpression call) {
	InitializerConverter converter = new InitializerConverter();
	IASTInitializerClause[] arguments = call.getArguments();
	for (int i = 0; i < arguments.length; i++) {
	    IASTInitializerClause argument = arguments[i];
	    OOExpression convertedArgument = converter.convertInitializerClause(argument);
	    
	    if (parameterWasRemovedAtIndex(functionCall.getFunctionName(), i)) {
		functionCall.setOwnerExpression(convertedArgument);
		ownerSet = true;
	    } else {
		functionCall.getArgumentExpressions().add(convertedArgument);
	    }
	}
    }
    
    private void handleFunctionOwner(OOFunctionCallExpression functionCall, IASTFunctionCallExpression call) {
	if (ownerSet) {
	    return;
	}
	
	/* if (called function is static) owner = owningClassName
	 * 
	 * 
	 */
    }
    
    private boolean parameterWasRemovedAtIndex(String functionName, int index) {
	for (Map.Entry<String, Integer> indexEntry : RemovedParameterRepository.getRemovedParameterIndices()) {
	    if (indexEntry.getKey().equals(functionName) && indexEntry.getValue() == index) {
		return true;
	    }
	}
	
	return false;
    }
}
