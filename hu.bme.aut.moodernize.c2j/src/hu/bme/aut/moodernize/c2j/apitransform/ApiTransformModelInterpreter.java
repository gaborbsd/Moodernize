package hu.bme.aut.moodernize.c2j.apitransform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;

import hu.bme.aut.apitransform.apiTransform.Function;
import hu.bme.aut.apitransform.apiTransform.Model;
import hu.bme.aut.apitransform.apiTransform.Parameter;
import hu.bme.aut.apitransform.apiTransform.Transformation;
import hu.bme.aut.moodernize.c2j.util.OOExpressionWithPrecedingStatements;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OOVariable;

public class ApiTransformModelInterpreter {
    private Model model;
    private List<String> apiCalls = new ArrayList<String>();
    static Map<String, OOExpression> sourceArguments = new HashMap<String, OOExpression>();
    
    public ApiTransformModelInterpreter(Model model) {
	this.model = model;
	collectApiCallList();
	sourceArguments.clear();
    }

    public OOExpressionWithPrecedingStatements interpret(String functionName, List<OOExpression> arguments) {
	if (!functionIsApiCall(functionName)) {
	    return null;
	}
	Transformation transformation = findTransformation(functionName);
	if (transformation == null) {
	    return null;
	}
	mapSourceArguments(transformation, arguments);

	List<OOVariable> instances = new InstanceConverter().createInstances(transformation);
	List<OOFunctionCallExpression> targets = new TargetConverter().createTargets(transformation);
	OOExpressionWithPrecedingStatements result;
	if (!targets.isEmpty()) {
	    result = new OOExpressionWithPrecedingStatements(targets.get(targets.size() - 1));
	    result.precedingStatements.addAll(instances);
	    result.precedingStatements.addAll(targets.subList(0, targets.size() - 1));
	} else {
	    result = new OOExpressionWithPrecedingStatements(null);
	    result.precedingStatements.addAll(instances);
	}

	// Instance-ok létrehozása OOVariable-ként, mennek a statementekbe
	// Minden targethez egy OOFunctionCallExpression. A legutolsó az expression, a
	// többi statement.
	// Operátorok kezelése paraméterként. "'plus' | 'minus' | 'multiply' | 'divide'
	// | 'assign'".

	return result;
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
    
    private void mapSourceArguments(Transformation transformation, List<OOExpression> arguments) {
	if (!(transformation.getSource() instanceof Function)) {
	    return;
	}
	List<Parameter> parameters = ((Function)transformation.getSource()).getParameters();
	for(Parameter parameter : parameters) {
	    int index = parameters.indexOf(parameter);
	    if (index > -1 && arguments.size() - 1 >= index && !sourceArguments.containsKey(parameter.getName())) {
		sourceArguments.put(parameter.getName(), arguments.get(index));
	    }
	}
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
