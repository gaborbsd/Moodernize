package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;

import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OODoubleLiteral;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOLogicalLiteral;
import hu.bme.aut.oogen.OOStringLiteral;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOTypeCast;
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

	case IASTLiteralExpression.lk_char_constant:
	    OOTypeCast typeCast = factory.createOOTypeCast();

	    OOType type = factory.createOOType();
	    type.setBaseType(OOBaseType.BYTE);
	    typeCast.setType(type);

	    OOStringLiteral charLiteral = factory.createOOStringLiteral();
	    charLiteral.setValue(valueString);
	    typeCast.setExpression(charLiteral);

	    return typeCast;

	case IASTLiteralExpression.lk_string_literal:
	    OOStringLiteral stringLiteral = factory.createOOStringLiteral();
	    stringLiteral.setValue(valueString);
	    return stringLiteral;

	case IASTLiteralExpression.lk_nullptr:
	    return factory.createOONullLiteral();

	default:
	    throw new UnsupportedOperationException("Unsupported LiteralExpression encountered: " + literalExpression);
	}
    }
}
