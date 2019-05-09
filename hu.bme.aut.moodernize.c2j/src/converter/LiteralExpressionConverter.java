package converter;

import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;

import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OogenFactory;

public class LiteralExpressionConverter {
	private static OogenFactory factory = OogenFactory.eINSTANCE;

	public OOExpression convertLiteralExpression(IASTLiteralExpression literalExpression) {
		return handleByKind(literalExpression, literalExpression.getKind());
	}

	private OOExpression handleByKind(IASTLiteralExpression literalExpression, int kind) {
		String valueString = new String(literalExpression.getValue());
		switch (kind) {
		case IASTLiteralExpression.lk_integer_constant: {
			int value = Integer.parseInt(valueString);
			OOIntegerLiteral integerLiteral = factory.createOOIntegerLiteral();
			integerLiteral.setValue(value);
			return integerLiteral;
		}

		// TODO: Throw NotSupportedException instead of returning null
		default:
			return null;
		}
	}
}
