package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTArraySubscriptExpression;
import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTBinaryTypeIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTCastExpression;
import org.eclipse.cdt.core.dom.ast.IASTConditionalExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpressionList;
import org.eclipse.cdt.core.dom.ast.IASTFieldReference;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;
import org.eclipse.cdt.core.dom.ast.IASTProblemExpression;
import org.eclipse.cdt.core.dom.ast.IASTTypeIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTTypeIdInitializerExpression;
import org.eclipse.cdt.core.dom.ast.IASTUnaryExpression;

import hu.bme.aut.moodernize.c2j.converter.declaration.DeclaratorConverter;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOTypeCast;
import hu.bme.aut.oogen.OogenFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOExpression convertExpression(IASTExpression expression) {
	if (expression instanceof IASTArraySubscriptExpression) {
	    return convertArraySubscriptExpression((IASTArraySubscriptExpression) expression);
	} else if (expression instanceof IASTBinaryExpression) {
	    return convertBinaryExpression((IASTBinaryExpression) expression);
	} else if (expression instanceof IASTBinaryTypeIdExpression) {
	    return convertBinaryTypeIdExpression((IASTBinaryExpression) expression);
	} else if (expression instanceof IASTCastExpression) {
	    return convertCastExpression((IASTCastExpression) expression);
	} else if (expression instanceof IASTConditionalExpression) {
	    return convertConditionalExpression((IASTConditionalExpression) expression);
	} else if (expression instanceof IASTExpressionList) {
	    return convertExpressionList((IASTExpressionList) expression);
	} else if (expression instanceof IASTFieldReference) {
	    return convertFieldReference((IASTFieldReference) expression);
	} else if (expression instanceof IASTFunctionCallExpression) {
	    return convertFunctionCallExpression((IASTFunctionCallExpression) expression);
	} else if (expression instanceof IASTIdExpression) {
	    return convertIdExpression((IASTIdExpression) expression);
	} else if (expression instanceof IASTLiteralExpression) {
	    return convertLiteralExpression((IASTLiteralExpression) expression);
	} else if (expression instanceof IASTProblemExpression) {
	    return convertProblemExpression((IASTProblemExpression) expression);
	} else if (expression instanceof IASTTypeIdExpression) {
	    return convertTypeIdExpression((IASTTypeIdExpression) expression);
	} else if (expression instanceof IASTTypeIdInitializerExpression) {
	    return convertTypeIdInitializerExpression((IASTTypeIdInitializerExpression) expression);
	} else if (expression instanceof IASTUnaryExpression) {
	    return convertUnaryExpression((IASTUnaryExpression) expression);
	} else {
	    throw new UnsupportedOperationException("Unsupported expression encountered: " + expression);
	}
    }

    private OOExpression convertArraySubscriptExpression(IASTArraySubscriptExpression expression) {
	throw new NotImplementedException();
    }

    private OOExpression convertBinaryExpression(IASTBinaryExpression expression) {
	BinaryExpressionConverter binaryConverter = new BinaryExpressionConverter();
	return binaryConverter.convertBinaryExpression(expression);
    }

    private OOExpression convertBinaryTypeIdExpression(IASTBinaryExpression expression) {
	throw new NotImplementedException();
    }

    private OOExpression convertCastExpression(IASTCastExpression expression) {
	OOTypeCast typeCast = factory.createOOTypeCast();
	typeCast.setExpression(convertExpression(expression.getOperand()));
	typeCast.setType(new DeclaratorConverter().convertSpecifier(expression.getTypeId().getDeclSpecifier()));
	return typeCast;
    }

    private OOExpression convertConditionalExpression(IASTConditionalExpression expression) {
	throw new NotImplementedException();
    }

    private OOExpression convertExpressionList(IASTExpressionList expression) {
	throw new NotImplementedException();
    }

    private OOExpression convertFieldReference(IASTFieldReference expression) {
	throw new NotImplementedException();
    }

    private OOExpression convertFunctionCallExpression(IASTFunctionCallExpression expression) {
	throw new NotImplementedException();
    }

    private OOExpression convertIdExpression(IASTIdExpression expression) {
	IdExpressionConverter idConverter = new IdExpressionConverter();
	return idConverter.convertIdExpression(expression);
    }

    private OOExpression convertLiteralExpression(IASTLiteralExpression expression) {
	LiteralExpressionConverter literalConverter = new LiteralExpressionConverter();
	return literalConverter.convertLiteralExpression(expression);
    }

    private OOExpression convertProblemExpression(IASTProblemExpression expression) {
	throw new NotImplementedException();
    }

    private OOExpression convertTypeIdExpression(IASTTypeIdExpression expression) {
	throw new NotImplementedException();
    }

    private OOExpression convertTypeIdInitializerExpression(IASTTypeIdInitializerExpression expression) {
	throw new NotImplementedException();
    }

    private OOExpression convertUnaryExpression(IASTUnaryExpression expression) {
	throw new NotImplementedException();
    }
}
