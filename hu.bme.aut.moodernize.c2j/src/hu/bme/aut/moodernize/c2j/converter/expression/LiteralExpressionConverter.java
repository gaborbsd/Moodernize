package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;

import hu.bme.aut.oogen.OOBoolLiteral;
import hu.bme.aut.oogen.OODoubleLiteral;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOLogicalLiteral;
import hu.bme.aut.oogen.OogenFactory;

public class LiteralExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOExpression convertLiteralExpression(IASTLiteralExpression literalExpression) {
	String valueString = new String(literalExpression.getValue());
	switch (literalExpression.getKind()) {
	case IASTLiteralExpression.lk_integer_constant:
	    int valueInt = Integer.parseInt(valueString);
	    OOIntegerLiteral integerLiteral = factory.createOOIntegerLiteral();
	    integerLiteral.setValue(valueInt);
	    return integerLiteral;

	case IASTLiteralExpression.lk_false:
	case IASTLiteralExpression.lk_true:
	    boolean valueBoolean = Boolean.parseBoolean(valueString);
	    OOLogicalLiteral logicalLiteral = factory.createOOLogicalLiteral();
	    logicalLiteral.setValue(valueBoolean);
	    return logicalLiteral;

	case IASTLiteralExpression.lk_float_constant:
	    double valueDouble = Double.parseDouble(valueString);
	    OODoubleLiteral doubleLiteral = factory.createOODoubleLiteral();
	    doubleLiteral.setValue(valueDouble);
	    return doubleLiteral;

	default:
	    throw new UnsupportedOperationException("Unsupported LiteralExpression encountered: " + literalExpression);
	}
    }
}
