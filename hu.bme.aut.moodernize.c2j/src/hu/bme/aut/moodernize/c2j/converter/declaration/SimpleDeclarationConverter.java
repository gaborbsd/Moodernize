package hu.bme.aut.moodernize.c2j.converter.declaration;

import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;

import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OogenFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimpleDeclarationConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOStatement convertSimpleDeclaration(IASTSimpleDeclaration declaration) {
	OOVariable declaredVariable = factory.createOOVariable();
	handleDeclarators(declaredVariable, declaration.getDeclarators());
	handleSpecifier(declaredVariable, declaration.getDeclSpecifier());
	return declaredVariable;
    }

    // TODO: Handle declarators: arraydeclarator, pointeroperator, nested declarator
    private void handleDeclarators(OOVariable declaredVariable, IASTDeclarator[] declarators) {
	// Local variable declaration will not have more than one declarator (hopefully)
	IASTDeclarator declarator = declarators[0];
	declaredVariable.setName(declarator.getName().resolveBinding().getName());

	IASTInitializer init = declarator.getInitializer();
	if (init != null) {
	    InitializerConverter converter = new InitializerConverter();
	    declaredVariable.setInitializerExpression(converter.convertInitializer(declarator.getInitializer()));
	}
    }

    private void handleSpecifier(OOVariable declaredVariable, IASTDeclSpecifier specifier) {
	if (specifier instanceof IASTSimpleDeclSpecifier) {
	    throw new NotImplementedException();
	}
    }
}
