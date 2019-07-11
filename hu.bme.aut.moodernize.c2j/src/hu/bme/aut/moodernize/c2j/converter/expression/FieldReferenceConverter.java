package hu.bme.aut.moodernize.c2j.converter.expression;

import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTFieldReference;

import hu.bme.aut.moodernize.c2j.util.RemovedParameterRepository;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFieldReferenceExpression;
import hu.bme.aut.oogen.OOVariableReferenceExpression;
import hu.bme.aut.oogen.OogenFactory;

public class FieldReferenceConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOFieldReferenceExpression convertFieldReference(IASTFieldReference fieldReference) {
	OOFieldReferenceExpression ooFieldReference = factory.createOOFieldReferenceExpression();
	ooFieldReference.setFieldName(fieldReference.getFieldName().resolveBinding().getName());
	setFieldOwner(ooFieldReference, fieldReference);

	return ooFieldReference;
    }

    private void setFieldOwner(OOFieldReferenceExpression ooFieldReference, IASTFieldReference fieldReference) {
	String containingFunctionName = TransformUtil.getContainingFunctionName(fieldReference);
	OOExpression ownerExpression = new ExpressionConverter().convertExpression(fieldReference.getFieldOwner());

	if (ownerExpression instanceof OOVariableReferenceExpression) {
	    String referredName = ((OOVariableReferenceExpression) ownerExpression).getVariable().getName();
	    for (Map.Entry<String, String> removedParameterEntry : RemovedParameterRepository.getRemovedParameterNames().entrySet()) {
		if (removedParameterEntry.getKey().equals(containingFunctionName)
			&& removedParameterEntry.getValue().equals(referredName)) {
		    ooFieldReference.setFieldOwner(factory.createOOThisLiteral());
		    return;
		}
	    }
	}
	
	ooFieldReference.setFieldOwner(ownerExpression);
    }
}
