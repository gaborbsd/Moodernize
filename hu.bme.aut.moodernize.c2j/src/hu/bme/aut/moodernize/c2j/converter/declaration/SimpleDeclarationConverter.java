package hu.bme.aut.moodernize.c2j.converter.declaration;

import org.eclipse.cdt.core.dom.ast.IASTArrayDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTArrayModifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;

import hu.bme.aut.moodernize.c2j.converter.expression.ExpressionConverter;
import hu.bme.aut.moodernize.c2j.util.IntegerLiteralToBooleanConverter;
import hu.bme.aut.oogen.OONewClass;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableDeclarationList;
import hu.bme.aut.oogen.OogenFactory;

public class SimpleDeclarationConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    
    public OOVariableDeclarationList convertSimpleDeclaration(IASTSimpleDeclaration declaration) {
	OOVariableDeclarationList declarationList = factory.createOOVariableDeclarationList();
	for (IASTDeclarator declarator : declaration.getDeclarators()) {
	    OOVariable declaredVariable = factory.createOOVariable();
	    handleSpecifier(declaredVariable, declaration.getDeclSpecifier());
	    handleDeclarator(declaredVariable, declarator);
	    declarationList.getVariableDeclarations().add(declaredVariable);
	}
		
	IntegerLiteralToBooleanConverter.handleIntToBoolConversion(declarationList);
	return declarationList;
    }

    // TODO: Handle declarators: pointeroperator, nested declarator
    private void handleDeclarator(OOVariable declaredVariable, IASTDeclarator declarator) {
	declaredVariable.setName(declarator.getName().resolveBinding().getName());
	
	if (declarator instanceof IASTArrayDeclarator) {
	    IASTArrayDeclarator arrayDeclarator = (IASTArrayDeclarator) declarator;
	    OOType type = declaredVariable.getType();
	    ExpressionConverter converter = new ExpressionConverter();
	    for (IASTArrayModifier modifier : arrayDeclarator.getArrayModifiers()) {
		type.setArrayDimensions(type.getArrayDimensions() + 1);
		IASTExpression sizeExpression = modifier.getConstantExpression();
		//TODO: What to do if some dimensions are set and some are not?
		if (sizeExpression != null) {
		    type.getArraySizeExpressions().add(converter.convertExpression(sizeExpression));
		} else {
		    type.getArraySizeExpressions().add(factory.createOONullLiteral());
		}
	    }
	}
	handleInitializerExpression(declaredVariable, declarator);
    }

    private void handleInitializerExpression(OOVariable declaredVariable, IASTDeclarator declarator) {
	IASTInitializer initializer = declarator.getInitializer();
	if (initializer != null) {
	    InitializerConverter converter = new InitializerConverter(declaredVariable.getType());
	    declaredVariable.setInitializerExpression(converter.convertInitializer(declarator.getInitializer()));
	} else {
	    OOType declaredType = declaredVariable.getType();
	    if (declaredType.getClassType() != null) {
		OONewClass newExpression = factory.createOONewClass();
		newExpression.setClassName(declaredType.getClassType().getName());
		declaredVariable.setInitializerExpression(newExpression);
	    }
	}
    }
    
    private void handleSpecifier(OOVariable declaredVariable, IASTDeclSpecifier specifier) {
	declaredVariable.setType(new DeclaratorSpecifierConverter().convertSpecifier(specifier));
    }
}
