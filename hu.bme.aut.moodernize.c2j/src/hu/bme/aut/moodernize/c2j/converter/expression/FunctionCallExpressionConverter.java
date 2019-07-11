package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializerClause;

import hu.bme.aut.moodernize.c2j.converter.declaration.InitializerConverter;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OogenFactory;

public class FunctionCallExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOFunctionCallExpression convertFunctionCallExpression(IASTFunctionCallExpression call) {
	OOFunctionCallExpression functionCall = factory.createOOFunctionCallExpression();
	
	handleFunctionName(functionCall, call);
	handleFunctionArguments(functionCall, call);
	
	
	return functionCall;
    }

    private void handleFunctionName(OOFunctionCallExpression functionCall, IASTFunctionCallExpression call) {
	IASTExpression functionNameExpression = call.getFunctionNameExpression();
	if (!(functionNameExpression instanceof IASTIdExpression)) {
	    throw new UnsupportedOperationException(
		    "Unexpected function call NameExpression encountered: " + functionNameExpression);
	}
	
	functionCall.setFunctionName(((IASTIdExpression)call).getName().resolveBinding().getName());
    }
    
    private void handleFunctionArguments(OOFunctionCallExpression functionCall, IASTFunctionCallExpression call) {
	InitializerConverter converter = new InitializerConverter();
	IASTInitializerClause[] arguments = call.getArguments();
	for (int i = 0; i < arguments.length; i++) {
	    IASTInitializerClause argument = arguments[i];
	    OOExpression convertedArgument = converter.convertInitializerClause(argument);
	    functionCall.getArgumentExpressions().add(convertedArgument);
	}
    }
}
