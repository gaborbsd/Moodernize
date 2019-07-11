package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTCastExpression;

import hu.bme.aut.moodernize.c2j.converter.declaration.DeclaratorSpecifierConverter;
import hu.bme.aut.oogen.OOTypeCast;
import hu.bme.aut.oogen.OogenFactory;

public class TypeCastConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOTypeCast convertTypeCast(IASTCastExpression cast) {
	OOTypeCast typeCast = factory.createOOTypeCast();
	typeCast.setExpression(new ExpressionConverter().convertExpression(cast.getOperand()));
	typeCast.setType(new DeclaratorSpecifierConverter().convertSpecifier(cast.getTypeId().getDeclSpecifier()));
	return typeCast;
    }
}
