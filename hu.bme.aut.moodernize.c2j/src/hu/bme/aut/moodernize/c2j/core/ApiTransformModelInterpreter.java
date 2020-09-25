package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import hu.bme.aut.apitransform.apiTransform.Function;
import hu.bme.aut.apitransform.apiTransform.Instance;
import hu.bme.aut.apitransform.apiTransform.Model;
import hu.bme.aut.apitransform.apiTransform.Transformation;
import hu.bme.aut.moodernize.c2j.util.OOExpressionWithPrecedingStatements;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OogenFactory;

public class ApiTransformModelInterpreter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    private Model model;
    private List<String> apiCalls = new ArrayList<String>();

    public ApiTransformModelInterpreter(Model model) {
	this.model = model;
	collectApiCallList();
    }

    public OOExpressionWithPrecedingStatements interpret(String functionName) {
	if (!functionIsApiCall(functionName)) {
	    return null;
	}
	Transformation transformation = findTransformation(functionName);
	if (transformation == null) {
	    return null;
	}
	
	
	// Instance-ok létrehozása OOVariable-ként, mennek a statementekbe
	// Minden targethez egy OOFunctionCallExpression. A legutolsó az expression, a
	// többi statement.
	// Operátorok kezelése paraméterként. "'plus' | 'minus' | 'multiply' | 'divide'
	// | 'assign'".

	OOFunctionCallExpression ooCall = factory.createOOFunctionCallExpression();
	ooCall.setFunctionName("APICALL");
	return new OOExpressionWithPrecedingStatements(ooCall);
    }

    public boolean functionIsApiCall(String functionName) {
	return apiCalls.contains(functionName);
    }

    private Transformation findTransformation(String functionName) {
	for (Transformation transformation : model.getTransformations()) {
	    if (transformation.getSource() instanceof Function
		    && ((Function) transformation.getSource()).getName().equals(functionName)) {
		return transformation;
	    }
	}
	
	return null;
    }
    
    private List<OOStatement> createInstances(Transformation transformation) {
	for (Instance instance: transformation.getInstances()) {
	    
	}
	
	return null;
    }

    private void collectApiCallList() {
	if (model == null || model.getTransformations() == null || model.getTransformations().isEmpty()) {
	    return;
	}
	for (Transformation transformation : model.getTransformations()) {
	    EObject source = transformation.getSource();
	    if (source instanceof Function) {
		apiCalls.add(((Function) source).getName());
	    }
	}
    }
}
