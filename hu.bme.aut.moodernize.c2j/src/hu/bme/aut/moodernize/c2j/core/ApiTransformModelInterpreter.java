package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import hu.bme.aut.apitransform.apiTransform.Function;
import hu.bme.aut.apitransform.apiTransform.Model;
import hu.bme.aut.apitransform.apiTransform.Transformation;
import hu.bme.aut.moodernize.c2j.util.OOExpressionWithPrecedingStatements;

public class ApiTransformModelInterpreter {
    private Model model;
    private List<String> apiCalls = new ArrayList<String>();
    
    public ApiTransformModelInterpreter(Model model) {
	this.model = model;
	collectApiCallList();
    }
    
    public OOExpressionWithPrecedingStatements interpret() {
	return null;
    }
    
    public boolean functionIsApiCall(String functionName) {
	return apiCalls.contains(functionName);
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
