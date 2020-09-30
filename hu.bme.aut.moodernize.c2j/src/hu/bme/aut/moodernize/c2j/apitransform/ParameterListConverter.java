package hu.bme.aut.moodernize.c2j.apitransform;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.apitransform.apiTransform.Parameter;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;
import hu.bme.aut.oogen.OogenFactory;

class ParameterListConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public List<OOExpression> createParameters(List<Parameter> parameters) {
	List<OOExpression> result = new ArrayList<OOExpression>();
	for (Parameter parameter : parameters) {
	    OOExpression correspondingParameter = ApiTransformModelInterpreter.sourceArguments.get(parameter.getName());
	    if (correspondingParameter != null) {
		result.add(correspondingParameter);
	    } else {
		result.add(createVariableReference(parameter.getName()));
	    }
	}
	return result;
    }

    private OOVariableReferenceExpression createVariableReference(String referredName) {
	OOVariableReferenceExpression owner = factory.createOOVariableReferenceExpression();
	OOVariable referredOwner = factory.createOOVariable();
	referredOwner.setName(referredName);
	owner.setVariable(referredOwner);

	return owner;
    }
}
