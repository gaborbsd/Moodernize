package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IEnumeration;
import org.eclipse.cdt.core.dom.ast.IType;

import hu.bme.aut.moodernize.c2j.projectcreation.MainClassCreator;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFieldReferenceExpression;
import hu.bme.aut.oogen.OOStringLiteral;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;
import hu.bme.aut.oogen.OogenFactory;

public class IdExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOExpression convertIdExpression(IASTIdExpression idExpression) {
	IType type = idExpression.getExpressionType();
	String name = idExpression.getName().resolveBinding().getName();

	if (type instanceof IEnumeration) {
	    OOFieldReferenceExpression enumReference = factory.createOOFieldReferenceExpression();
	    enumReference.setFieldName(name);

	    OOStringLiteral ownerLiteral = factory.createOOStringLiteral();
	    ownerLiteral.setValue(((IEnumeration) type).getName());

	    enumReference.setFieldOwner(ownerLiteral);

	    return enumReference;
	} else if (TransformUtil.isGlobalVariable(name)) {
	    OOFieldReferenceExpression fieldReference = factory.createOOFieldReferenceExpression();
	    fieldReference.setFieldName(name);

	    OOStringLiteral ownerLiteral = factory.createOOStringLiteral();
	    ownerLiteral.setValue(MainClassCreator.MAINCLASSNAME);
	    fieldReference.setFieldOwner(ownerLiteral);

	    return fieldReference;
	} else {
	    OOVariable referredVariable = factory.createOOVariable();
	    referredVariable.setName(name);
	    referredVariable.setType(TypeConverter.convertCDTTypeToOOgenType(type));

	    OOVariableReferenceExpression referenceExpression = factory.createOOVariableReferenceExpression();
	    referenceExpression.setVariable(referredVariable);

	    return referenceExpression;
	}
    }
}
