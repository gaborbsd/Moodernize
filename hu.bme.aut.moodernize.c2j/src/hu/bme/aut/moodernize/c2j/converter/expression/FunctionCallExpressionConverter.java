package hu.bme.aut.moodernize.c2j.converter.expression;

import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializerClause;

import hu.bme.aut.moodernize.c2j.converter.declaration.InitializerConverter;
import hu.bme.aut.moodernize.c2j.core.TransformationDataHolder;
import hu.bme.aut.moodernize.c2j.util.RemovedParameterRepository;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOStringLiteral;
import hu.bme.aut.oogen.OogenFactory;

public class FunctionCallExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    private boolean ownerSetToRemovedParameter = false;

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

	functionCall.setFunctionName(((IASTIdExpression) functionNameExpression).getName().resolveBinding().getName());
    }

    private void handleFunctionArguments(OOFunctionCallExpression functionCall, IASTFunctionCallExpression call) {
	InitializerConverter converter = new InitializerConverter();
	IASTInitializerClause[] arguments = call.getArguments();
	for (int i = 0; i < arguments.length; i++) {
	    IASTInitializerClause argument = arguments[i];
	    OOExpression convertedArgument = converter.convertInitializerClause(argument);

	    if (parameterWasRemovedAtIndex(functionCall.getFunctionName(), i)) {
		functionCall.setOwnerExpression(convertedArgument);
		ownerSetToRemovedParameter = true;
	    } else {
		functionCall.getArgumentExpressions().add(convertedArgument);
	    }
	}
    }

    private void handleFunctionOwner(OOFunctionCallExpression functionCall, IASTFunctionCallExpression call) {
	if (ownerSetToRemovedParameter) {
	    return;
	}

	OOClass containingClass = getContainingClass(functionCall.getFunctionName());
	OOClass mainClass = TransformUtil.getMainClassFromClasses(TransformationDataHolder.getCreatedClasses());
	OOMethod calledFunction = getCalledFunction(mainClass.getMethods(), functionCall.getFunctionName());
	if (calledFunction.isStatic()) {
	    OOStringLiteral ownerExpression = factory.createOOStringLiteral();
	    ownerExpression.setValue(containingClass.getName());
	    functionCall.setOwnerExpression(ownerExpression);
	    return;
	}
    }

    private OOClass getContainingClass(String functionName) {
	OOClass containingClass = TransformUtil.getContainerClass(functionName,
		TransformationDataHolder.getCreatedClasses());
	if (containingClass != null) {
	    return containingClass;
	}

	throw new UnsupportedOperationException("Function call encountered that belongs to no class: " + functionName);
    }

    private OOMethod getCalledFunction(List<OOMethod> globalFunctions, String functionName) {
	OOMethod calledFunction = TransformUtil.getMethodFromClasses(TransformationDataHolder.getCreatedClasses(),
		functionName);
	if (calledFunction != null) {
	    return calledFunction;
	}

	calledFunction = TransformUtil.getFunctionByName(globalFunctions, functionName);
	if (calledFunction != null) {
	    return calledFunction;
	}

	throw new UnsupportedOperationException("Function call encountered that belongs to no class: " + functionName);
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
