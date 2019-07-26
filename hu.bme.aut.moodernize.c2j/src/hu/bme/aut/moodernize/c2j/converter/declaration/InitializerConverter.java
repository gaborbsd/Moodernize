package hu.bme.aut.moodernize.c2j.converter.declaration;

import java.security.InvalidParameterException;

import org.eclipse.cdt.core.dom.ast.IASTEqualsInitializer;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTInitializerClause;
import org.eclipse.cdt.core.dom.ast.IASTInitializerList;

import hu.bme.aut.moodernize.c2j.converter.expression.ExpressionConverter;
import hu.bme.aut.moodernize.c2j.util.IntegerLiteralToBooleanConverter;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOInitializerList;
import hu.bme.aut.oogen.OONewArray;
import hu.bme.aut.oogen.OONewClass;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OogenFactory;

public class InitializerConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    private OOType type = null;
    private int initializerListConversionCount = 0;

    public InitializerConverter() {
    }

    public InitializerConverter(OOType type) {
	this.type = type;
    }

    public OOExpression convertInitializer(IASTInitializer init) {
	if (init instanceof IASTEqualsInitializer) {
	    return convertEqualsInitializer((IASTEqualsInitializer) init);
	} else if (init instanceof IASTInitializerList) {
	    return convertInitializerList((IASTInitializerList) init);
	} else {
	    throw new UnsupportedOperationException("Unsupported Initializer encountered: " + init);
	}
    }

    public OOExpression convertInitializerClause(IASTInitializerClause initClause) {
	if (initClause instanceof IASTExpression) {
	    ExpressionConverter converter = new ExpressionConverter();
	    OOExpression convertedExpression = converter.convertExpression((IASTExpression) initClause);
	    return convertedExpression;
	} else if (initClause instanceof IASTInitializerList) {
	    return convertInitializerList((IASTInitializerList) initClause);
	} else {
	    throw new InvalidParameterException("Invalid Initializer encountered " + initClause);
	}
    }

    private OOExpression convertEqualsInitializer(IASTEqualsInitializer equalsInit) {
	return convertInitializerClause(equalsInit.getInitializerClause());
    }

    private OOExpression convertInitializerList(IASTInitializerList initList) {
	initializerListConversionCount++;
	if (type == null) {
	    return createInitListFromClauses(initList.getClauses());
	} else {
	    if (initializerListConversionCount == 1) {
		if (type.getArrayDimensions() == 0) {
		    OOClass classType = type.getClassType();
		    if (classType != null) {
			OONewClass newClassExpression = factory.createOONewClass();
			newClassExpression.setClassName(classType.getName());
			for (IASTInitializerClause clause : initList.getClauses()) {
			    newClassExpression.getConstructorParameterExpressions()
				    .add(convertInitializerClause(clause));
			}
			return newClassExpression;
		    } else {
			return createInitListFromClauses(initList.getClauses());
		    }
		} else {
		    OONewArray newArrayExpression = factory.createOONewArray();
		    newArrayExpression.setArrayType(type);
		    newArrayExpression.setInitializerList(createInitListFromClauses(initList.getClauses()));
		    return newArrayExpression;
		}
	    } else {
		return createInitListFromClauses(initList.getClauses());
	    }
	}
    }

    private OOInitializerList createInitListFromClauses(IASTInitializerClause[] clauses) {
	OOInitializerList initializerList = factory.createOOInitializerList();
	for (IASTInitializerClause clause : clauses) {
	    initializerList.getInitializerExpressions().add(convertInitializerClause(clause));
	}

	if (type.getBaseType() == OOBaseType.BOOLEAN) {
	    IntegerLiteralToBooleanConverter.handleIntToBoolConversion(initializerList);
	}
	return initializerList;
    }
}
