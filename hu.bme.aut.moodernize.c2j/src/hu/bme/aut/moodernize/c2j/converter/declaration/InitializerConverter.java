package hu.bme.aut.moodernize.c2j.converter.declaration;

import java.security.InvalidParameterException;

import org.eclipse.cdt.core.dom.ast.IASTEqualsInitializer;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTInitializerClause;
import org.eclipse.cdt.core.dom.ast.IASTInitializerList;

import hu.bme.aut.moodernize.c2j.converter.expression.ExpressionConverter;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOInitializerList;
import hu.bme.aut.oogen.OogenFactory;

public class InitializerConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    
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
	OOInitializerList initializerList = factory.createOOInitializerList();
	for (IASTInitializerClause clause : initList.getClauses()) {
	    initializerList.getInitializerExpressions().add(convertInitializerClause(clause));
	}
	
	return initializerList;
    }
}
