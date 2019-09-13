package hu.bme.aut.moodernize.c2j.converter.expression;

import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFieldReference;

import hu.bme.aut.moodernize.c2j.dataholders.RemovedParameterDataHolder;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OOVariableReferenceExpression;
import hu.bme.aut.oogen.OogenFactory;

public class FieldReferenceConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOFunctionCallExpression convertFieldReferenceToGetterCall(IASTFieldReference fieldReference) {
	OOFunctionCallExpression getterCall = factory.createOOFunctionCallExpression();
	getterCall.setFunctionName("get"
		+ TransformUtil.getWithUpperCaseFirstCharacter(fieldReference.getFieldName().resolveBinding().getName()));
	setOwnerExpression(getterCall, fieldReference);

	return getterCall;
    }

    private void setOwnerExpression(OOFunctionCallExpression getterCall, IASTFieldReference fieldReference) {
	String containingFunctionName = TransformUtil.getContainingFunctionName(fieldReference);
	OOExpression ownerExpression = new ExpressionConverter().convertExpression(fieldReference.getFieldOwner());

	if (ownerExpression instanceof OOVariableReferenceExpression) {
	    String referredName = ((OOVariableReferenceExpression) ownerExpression).getVariable().getName();
	    for (Map.Entry<String, String> removedParameterNameEntry : RemovedParameterDataHolder
		    .getRemovedParameterNames()) {
		if (removedParameterNameEntry.getKey().equals(containingFunctionName)
			&& removedParameterNameEntry.getValue().equals(referredName)) {
		    getterCall.setOwnerExpression((factory.createOOThisLiteral()));
		    return;
		}
	    }
	}

	getterCall.setOwnerExpression(ownerExpression);
    }

    public OOFunctionCallExpression getSetMethodCallForFieldReference(IASTFieldReference fieldReference,
	    IASTExpression setArgument) {
	OOFunctionCallExpression setterCall = factory.createOOFunctionCallExpression();
	setterCall.setFunctionName("set"
		+ TransformUtil.getWithUpperCaseFirstCharacter(fieldReference.getFieldName().resolveBinding().getName()));
	setterCall.getArgumentExpressions().add(new ExpressionConverter().convertExpression(setArgument));
	setOwnerExpression(setterCall, fieldReference);

	return setterCall;
    }
}
