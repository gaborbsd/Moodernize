package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTCastExpression;

import hu.bme.aut.moodernize.c2j.converter.declaration.DeclaratorSpecifierConverter;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOTypeCast;
import hu.bme.aut.oogen.OogenFactory;

public class TypeCastConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOExpression convertTypeCast(IASTCastExpression cast) {
	OOTypeCast typeCast = factory.createOOTypeCast();
	typeCast.setExpression(TransformUtil.convertExpressionAndProcessPrecedingStatements(new ExpressionConverter(),
		cast.getOperand()));
	typeCast.setType(new DeclaratorSpecifierConverter().convertSpecifier(cast.getTypeId().getDeclSpecifier()));
	
	if (shouldBeConvertedToNullLiteral(typeCast.getExpression(), typeCast.getType())) {
	    return factory.createOONullLiteral();
	}

	return typeCast;
    }

    private boolean shouldBeConvertedToNullLiteral(OOExpression operand, OOType castType) {
	return operand instanceof OOIntegerLiteral && ((OOIntegerLiteral) operand).getValue() == 0
		&& castType.getBaseType() == OOBaseType.OBJECT && castType.getClassType() == null;
    }
}
