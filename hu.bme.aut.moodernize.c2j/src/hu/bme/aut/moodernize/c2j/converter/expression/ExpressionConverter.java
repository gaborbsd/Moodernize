package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTArraySubscriptExpression;
import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTCastExpression;
import org.eclipse.cdt.core.dom.ast.IASTConditionalExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpressionList;
import org.eclipse.cdt.core.dom.ast.IASTFieldReference;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializerClause;
import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;
import org.eclipse.cdt.core.dom.ast.IASTUnaryExpression;

import hu.bme.aut.moodernize.c2j.converter.declaration.DeclaratorConverter;
import hu.bme.aut.oogen.OOCollectionIndex;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFieldReferenceExpression;
import hu.bme.aut.oogen.OOTernaryOperator;
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
	} else if (expression instanceof IASTUnaryExpression) {
	    return convertUnaryExpression((IASTUnaryExpression) expression);
	} else {
	    throw new UnsupportedOperationException("Unsupported expression encountered: " + expression);
	}
    }

    private OOExpression convertArraySubscriptExpression(IASTArraySubscriptExpression expression) {
	IASTInitializerClause argument = expression.getArgument();
	if (!(argument instanceof IASTExpression)) {
	    throw new UnsupportedOperationException(
		    "The argument of an ArraySubscriptException must be of type Expression but found: " + argument);
	}
	OOCollectionIndex collectionIndex = factory.createOOCollectionIndex();
	collectionIndex.setCollectionExpression(convertExpression(expression.getArrayExpression()));
	collectionIndex.setIndexExpression(convertExpression((IASTExpression) argument));
	
	return collectionIndex;
    }

    private OOExpression convertBinaryExpression(IASTBinaryExpression expression) {
	BinaryExpressionConverter binaryConverter = new BinaryExpressionConverter();
	return binaryConverter.convertBinaryExpression(expression);
    }

    private OOExpression convertCastExpression(IASTCastExpression expression) {
	OOTypeCast typeCast = factory.createOOTypeCast();
	typeCast.setExpression(convertExpression(expression.getOperand()));
	typeCast.setType(new DeclaratorConverter().convertSpecifier(expression.getTypeId().getDeclSpecifier()));
	return typeCast;
    }

    private OOExpression convertConditionalExpression(IASTConditionalExpression expression) {
	OOTernaryOperator ternary = factory.createOOTernaryOperator();
	ExpressionConverter converter = new ExpressionConverter();

	ternary.setCondition(converter.convertExpression(expression.getLogicalConditionExpression()));
	ternary.setPositiveBranch(converter.convertExpression(expression.getPositiveResultExpression()));
	ternary.setNegativeBranch(converter.convertExpression(expression.getNegativeResultExpression()));

	return ternary;
    }

    private OOExpression convertExpressionList(IASTExpressionList expression) {
	throw new NotImplementedException();
    }

    private OOExpression convertFieldReference(IASTFieldReference expression) {
	OOFieldReferenceExpression fieldReference = factory.createOOFieldReferenceExpression();
	fieldReference.setFieldOwner(new ExpressionConverter().convertExpression(expression.getFieldOwner()));
	fieldReference.setFieldName(expression.getFieldName().resolveBinding().getName());

	return fieldReference;
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

    private OOExpression convertUnaryExpression(IASTUnaryExpression expression) {
	UnaryExpressionConverter converter = new UnaryExpressionConverter();
	return converter.convertUnaryExpression(expression);
    }
}
