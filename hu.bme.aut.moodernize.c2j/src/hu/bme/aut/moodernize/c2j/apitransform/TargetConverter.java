package hu.bme.aut.moodernize.c2j.apitransform;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import hu.bme.aut.apitransform.apiTransform.Function;
import hu.bme.aut.apitransform.apiTransform.Transformation;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;
import hu.bme.aut.oogen.OogenFactory;

class TargetConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public List<OOFunctionCallExpression> createTargets(Transformation transformation) {
	List<OOFunctionCallExpression> result = new ArrayList<OOFunctionCallExpression>();
	for (EObject target : transformation.getTargets()) {
	    if (!(target instanceof Function)) {
		continue;
	    }
	    result.add(createFunctionCallForTarget((Function) target));
	}

	return result;
    }

    private OOFunctionCallExpression createFunctionCallForTarget(Function target) {
	OOFunctionCallExpression result = factory.createOOFunctionCallExpression();
	result.setFunctionName(target.getName());
	result.setOwnerExpression(createVariableReference(getOwnerName(target)));
	result.getArgumentExpressions().addAll(new ParameterListConverter().createParameters(target.getParameters()));

	return result;
    }

    private OOVariableReferenceExpression createVariableReference(String referredName) {
	OOVariableReferenceExpression owner = factory.createOOVariableReferenceExpression();
	OOVariable referredOwner = factory.createOOVariable();
	referredOwner.setName(referredName);
	owner.setVariable(referredOwner);

	return owner;
    }

    private String getOwnerName(Function target) {
	String result = "";
	if (target.getOwner() != null) {
	    for (String prefix : target.getOwner().getPrefixes()) {
		result += prefix + ".";
	    }
	    result += target.getOwner().getName();
	}

	return result;
    }
}
