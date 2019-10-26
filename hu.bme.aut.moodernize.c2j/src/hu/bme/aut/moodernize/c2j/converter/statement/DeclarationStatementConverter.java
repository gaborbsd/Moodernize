package hu.bme.aut.moodernize.c2j.converter.statement;

import org.eclipse.cdt.core.dom.ast.IASTASMDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarationStatement;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;

import hu.bme.aut.moodernize.c2j.converter.declaration.SimpleDeclarationConverter;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OogenFactory;

public class DeclarationStatementConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOStatement convertDeclarationStatement(IASTDeclarationStatement statement) {
	IASTDeclaration declaration = statement.getDeclaration();
	if (declaration instanceof IASTASMDeclaration) {
	    return convertASMDeclaration((IASTASMDeclaration) declaration);
	} else if (declaration instanceof IASTSimpleDeclaration) {
	    return convertSimpleDeclaration((IASTSimpleDeclaration) declaration);
	} else {
	    return factory.createOOEmptyExpression();
	    // throw new UnsupportedOperationException("Unsupported DeclarationStatement
	    // encountered: " + declaration);
	}
    }

    private OOStatement convertASMDeclaration(IASTASMDeclaration declaration) {
	return factory.createOOEmptyExpression();
	// throw new NotImplementedException();
    }

    private OOStatement convertSimpleDeclaration(IASTSimpleDeclaration declaration) {
	SimpleDeclarationConverter converter = new SimpleDeclarationConverter();
	return converter.convertSimpleDeclaration(declaration);
    }
}
