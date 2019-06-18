package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IBasicType;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.c.ICArrayType;

import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;
import hu.bme.aut.oogen.OogenFactory;

public class IdExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOExpression convertIdExpression(IASTIdExpression idExpression) {
	return handleByType(idExpression.getExpressionType(), idExpression.getName().resolveBinding().getName());
    }

    private OOExpression handleByType(IType type, String id) {
	if (type instanceof IBasicType || type instanceof ICArrayType) {
	    OOVariable referredVariable = factory.createOOVariable();
	    referredVariable.setName(id);
	    referredVariable.setType(TypeConverter.convertCDTTypeToOOgenType(type));

	    OOVariableReferenceExpression referenceExpression = factory.createOOVariableReferenceExpression();
	    referenceExpression.setVariable(referredVariable);

	    return referenceExpression;
	}
	throw new UnsupportedOperationException("Unsupported IdExpression type encountered: " + type);
    }
}
