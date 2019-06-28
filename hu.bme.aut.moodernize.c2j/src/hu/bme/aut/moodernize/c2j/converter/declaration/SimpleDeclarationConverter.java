package hu.bme.aut.moodernize.c2j.converter.declaration;

import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTInitializer;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;

import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableDeclarationList;
import hu.bme.aut.oogen.OogenFactory;

public class SimpleDeclarationConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    
    public OOVariableDeclarationList convertSimpleDeclaration(IASTSimpleDeclaration declaration) {
	OOVariableDeclarationList declarationList = factory.createOOVariableDeclarationList();
	for (IASTDeclarator declarator : declaration.getDeclarators()) {
	    OOVariable declaredVariable = factory.createOOVariable();
	    handleDeclarator(declaredVariable, declarator);
	    handleSpecifier(declaredVariable, declaration.getDeclSpecifier());
	    declarationList.getVariableDeclarations().add(declaredVariable);
	}
		
	return declarationList;
    }

    // TODO: Handle declarators: arraydeclarator, pointeroperator, nested declarator
    private void handleDeclarator(OOVariable declaredVariable, IASTDeclarator declarator) {
	declaredVariable.setName(declarator.getName().resolveBinding().getName());

	IASTInitializer init = declarator.getInitializer();
	if (init != null) {
	    InitializerConverter converter = new InitializerConverter();
	    declaredVariable.setInitializerExpression(converter.convertInitializer(declarator.getInitializer()));
	}
    }

    private void handleSpecifier(OOVariable declaredVariable, IASTDeclSpecifier specifier) {
	declaredVariable.setType(new DeclaratorConverter().convertSpecifier(specifier));
    }
}
