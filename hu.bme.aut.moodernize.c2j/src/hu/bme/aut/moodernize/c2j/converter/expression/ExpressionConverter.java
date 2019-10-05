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
import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;
import org.eclipse.cdt.core.dom.ast.IASTUnaryExpression;

import hu.bme.aut.moodernize.c2j.commentmapping.CommentProcessor;
import hu.bme.aut.moodernize.c2j.converter.declaration.InitializerConverter;
import hu.bme.aut.moodernize.c2j.dataholders.CommentMappingDataHolder;
import hu.bme.aut.moodernize.c2j.util.IntegerLiteralToBooleanConverter;
import hu.bme.aut.moodernize.c2j.util.OOExpressionWithPrecedingStatements;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOIndexing;
import hu.bme.aut.oogen.OOTernaryOperator;
import hu.bme.aut.oogen.OogenFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOExpression convertExpression(IASTExpression expression) {
	OOExpressionWithPrecedingStatements convertedExpression = getConvertedExpression(expression);
	CommentProcessor.processOwnedComments(convertedExpression.expression,
		CommentMappingDataHolder.findAllOwnedComments(expression));
	return convertedExpression.expression;
    }

    private OOExpressionWithPrecedingStatements getConvertedExpression(IASTExpression expression) {
	if (expression instanceof IASTArraySubscriptExpression) {
	    return new OOExpressionWithPrecedingStatements(
		    convertArraySubscriptExpression((IASTArraySubscriptExpression) expression));
	} else if (expression instanceof IASTBinaryExpression) {
	    return new OOExpressionWithPrecedingStatements(convertBinaryExpression((IASTBinaryExpression) expression));
	} else if (expression instanceof IASTCastExpression) {
	    return new OOExpressionWithPrecedingStatements(convertCastExpression((IASTCastExpression) expression));
	} else if (expression instanceof IASTConditionalExpression) {
	    return new OOExpressionWithPrecedingStatements(
		    convertConditionalExpression((IASTConditionalExpression) expression));
	} else if (expression instanceof IASTExpressionList) {
	    return new OOExpressionWithPrecedingStatements(convertExpressionList((IASTExpressionList) expression));
	} else if (expression instanceof IASTFieldReference) {
	    return convertFieldReference((IASTFieldReference) expression);
	} else if (expression instanceof IASTFunctionCallExpression) {
	    return new OOExpressionWithPrecedingStatements(
		    convertFunctionCallExpression((IASTFunctionCallExpression) expression));
	} else if (expression instanceof IASTIdExpression) {
	    return new OOExpressionWithPrecedingStatements(convertIdExpression((IASTIdExpression) expression));
	} else if (expression instanceof IASTLiteralExpression) {
	    return new OOExpressionWithPrecedingStatements(
		    convertLiteralExpression((IASTLiteralExpression) expression));
	} else if (expression instanceof IASTUnaryExpression) {
	    return new OOExpressionWithPrecedingStatements(convertUnaryExpression((IASTUnaryExpression) expression));
	} else {
	    throw new UnsupportedOperationException("Unsupported expression encountered: " + expression);
	}
    }

    private OOExpression convertArraySubscriptExpression(IASTArraySubscriptExpression expression) {
	OOIndexing collectionIndex = factory.createOOIndexing();
	collectionIndex.setCollectionExpression(convertExpression(expression.getArrayExpression()));
	collectionIndex
		.setIndexExpression(new InitializerConverter().convertInitializerClause(expression.getArgument()));

	return collectionIndex;
    }

    private OOExpression convertBinaryExpression(IASTBinaryExpression expression) {
	BinaryExpressionConverter binaryConverter = new BinaryExpressionConverter();
	return binaryConverter.convertBinaryExpression(expression);
    }

    private OOExpression convertCastExpression(IASTCastExpression expression) {
	TypeCastConverter converter = new TypeCastConverter();
	return converter.convertTypeCast(expression);
    }

    private OOExpression convertConditionalExpression(IASTConditionalExpression expression) {
	OOTernaryOperator ternary = factory.createOOTernaryOperator();
	ExpressionConverter converter = new ExpressionConverter();

	ternary.setCondition(converter.convertExpression(expression.getLogicalConditionExpression()));
	ternary.setPositiveBranch(converter.convertExpression(expression.getPositiveResultExpression()));
	ternary.setNegativeBranch(converter.convertExpression(expression.getNegativeResultExpression()));

	IntegerLiteralToBooleanConverter.handleIntToBoolConversion(ternary);

	return ternary;
    }

    private OOExpression convertExpressionList(IASTExpressionList expression) {
	throw new NotImplementedException();
    }

    private OOExpressionWithPrecedingStatements convertFieldReference(IASTFieldReference expression) {
	FieldReferenceConverter converter = new FieldReferenceConverter();
	return converter.convertFieldReference(expression);
    }

    private OOExpression convertFunctionCallExpression(IASTFunctionCallExpression expression) {
	FunctionCallExpressionConverter converter = new FunctionCallExpressionConverter();
	return converter.convertFunctionCallExpression(expression);
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
