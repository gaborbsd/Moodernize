package hu.bme.aut.moodernize.c2j.converter.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializerClause;

import hu.bme.aut.moodernize.c2j.apitransform.ApiTransformModelInterpreter;
import hu.bme.aut.moodernize.c2j.converter.declaration.InitializerConverter;
import hu.bme.aut.moodernize.c2j.core.CToJavaTransformer;
import hu.bme.aut.moodernize.c2j.dataholders.FunctionCallExpressionDataHolder;
import hu.bme.aut.moodernize.c2j.dataholders.RemovedParameterDataHolder;
import hu.bme.aut.moodernize.c2j.dataholders.TransformationDataHolder;
import hu.bme.aut.moodernize.c2j.util.OOExpressionWithPrecedingStatements;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOEmptyExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OONewClass;
import hu.bme.aut.oogen.OOStringLiteral;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;
import hu.bme.aut.oogen.OogenFactory;

public class FunctionCallExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    private boolean ownerSetToRemovedParameter = false;

    public OOExpressionWithPrecedingStatements convertFunctionCallExpression(IASTFunctionCallExpression call) {
	ApiTransformModelInterpreter interpreter = new ApiTransformModelInterpreter(CToJavaTransformer.apiModel);
	String functionName = createFunctionName(call);
	
	OOExpressionWithPrecedingStatements interpreterResult = interpreter.interpret(functionName, getFunctionArguments(call));
	if (interpreterResult != null) {
	    return interpreterResult;
	}
	
	OOFunctionCallExpression functionCall = factory.createOOFunctionCallExpression();
	functionCall.setFunctionName(functionName);
	handleFunctionArguments(functionCall, call);
	handleFunctionOwner(functionCall, call);

	if (!(functionCall.getOwnerExpression() instanceof OOEmptyExpression) && !(functionCall.getOwnerExpression() == null)) {
	    storeFunctionCall(functionCall, TransformUtil.getContainingFunctionName(call));
	}

	return new OOExpressionWithPrecedingStatements(functionCall);
    }
    
    private String createFunctionName(IASTFunctionCallExpression call) {
	IASTExpression functionNameExpression = call.getFunctionNameExpression();
	return functionNameExpression instanceof IASTIdExpression
		? ((IASTIdExpression) functionNameExpression).getName().resolveBinding().getName()
		: "ERROR_UNRECOGNIZED_FUNCTIONNAME";
    }
    
    private List<OOExpression> getFunctionArguments(IASTFunctionCallExpression call) {
	List<OOExpression> result = new ArrayList<OOExpression>();
	for (IASTInitializerClause argument : call.getArguments()) {
	    result.add(new InitializerConverter().convertInitializerClause(argument));
	}
	
	return result;
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
	try {
	    OOClass containingClass = getContainingClass(functionCall.getFunctionName());
	    OOClass mainClass = TransformUtil.getMainClassFromClasses(TransformationDataHolder.createdClasses);
	    OOMethod calledFunction = getCalledFunction(mainClass.getMethods(), functionCall.getFunctionName());

	    if (calledFunction.isStatic()) {
		OOStringLiteral ownerExpression = factory.createOOStringLiteral();
		ownerExpression.setValue(containingClass.getName());
		functionCall.setOwnerExpression(ownerExpression);
		return;
	    }

	    String containingFunctionName = TransformUtil.getContainingFunctionName(call);
	    OOMethod containingFunction = TransformUtil.getMethodFromClasses(TransformationDataHolder.createdClasses,
		    containingFunctionName);

	    if (!checkIfFunctionContainsPreviousOwnerAndSetAsNewOwner(functionCall, containingFunction)) {
		createNewInstanceAndSetAsOwner(functionCall, containingClass, containingFunction);
	    }
	} catch (UnsupportedOperationException e) {
	    TransformUtil.createAndAttachNotRecognizedErrorComment(functionCall,
		    "Error: Function owner not recognized");
	    functionCall.setOwnerExpression(null);
	}
    }

    private void storeFunctionCall(OOFunctionCallExpression functionCall, String containingFunctionName) {
	if (!FunctionCallExpressionDataHolder.getFunctionName().equals(containingFunctionName)) {
	    FunctionCallExpressionDataHolder.clearFunctionCalls(containingFunctionName);
	}
	FunctionCallExpressionDataHolder.addFunctionCall(functionCall);
    }

    private boolean checkIfFunctionContainsPreviousOwnerAndSetAsNewOwner(OOFunctionCallExpression functionCall,
	    OOMethod containingFunction) {
	if (!FunctionCallExpressionDataHolder.getFunctionName().equals(containingFunction.getName())) {
	    return false;
	}
	for (OOFunctionCallExpression previousCall : FunctionCallExpressionDataHolder
		.getPreviousFunctionCallsInFunction()) {
	    // TODO: overloaded functions?
	    if (previousCall.getFunctionName().equals(functionCall.getFunctionName())) {
		functionCall.setOwnerExpression(previousCall.getOwnerExpression());
		return true;
	    }
	}

	return false;
    }

    private void createNewInstanceAndSetAsOwner(OOFunctionCallExpression functionCall, OOClass classType,
	    OOMethod containingFunction) {
	OOVariable ownerDeclaration = factory.createOOVariable();
	ownerDeclaration.setName(TransformUtil.getWithLowerCaseFirstCharacter(classType.getName()));

	OOType type = factory.createOOType();
	classType.setName(classType.getName());
	type.setClassType(classType);
	type.setBaseType(OOBaseType.OBJECT);
	ownerDeclaration.setType(type);

	OONewClass newExpression = factory.createOONewClass();
	newExpression.setClassName(classType.getName());
	ownerDeclaration.setInitializerExpression(newExpression);

	containingFunction.getStatements().add(ownerDeclaration);

	OOVariableReferenceExpression reference = factory.createOOVariableReferenceExpression();
	reference.setVariable(ownerDeclaration);
	functionCall.setOwnerExpression(reference);
    }

    private OOClass getContainingClass(String functionName) {
	OOClass containingClass = TransformUtil.getContainerClass(functionName,
		TransformationDataHolder.createdClasses);
	if (containingClass != null) {
	    return containingClass;
	}

	throw new UnsupportedOperationException("Function call encountered that belongs to no class: " + functionName);
    }

    private OOMethod getCalledFunction(List<OOMethod> globalFunctions, String functionName) {
	OOMethod calledFunction = TransformUtil.getMethodFromClasses(TransformationDataHolder.createdClasses,
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
	for (Map.Entry<String, Integer> indexEntry : RemovedParameterDataHolder.getRemovedParameterIndices()) {
	    if (indexEntry.getKey().equals(functionName) && indexEntry.getValue() == index) {
		return true;
	    }
	}

	return false;
    }
}
