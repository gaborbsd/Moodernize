package hu.bme.aut.moodernize.c2j.converter.statement;

import org.eclipse.cdt.core.dom.ast.IASTASMDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarationStatement;
import org.eclipse.cdt.core.dom.ast.IASTProblemDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;

import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OogenFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DeclarationStatementConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    
    public OOStatement convertDeclarationStatement(IASTDeclarationStatement statement) {
	if (statement instanceof IASTASMDeclaration) {
	    return convertASMDeclaration((IASTASMDeclaration) statement);
	} else if (statement instanceof IASTProblemDeclaration) {
	    return convertProblemDeclaration((IASTProblemDeclaration) statement);
	} else if (statement instanceof IASTSimpleDeclaration) {
	    return convertSimpleDeclaration((IASTSimpleDeclaration) statement);
	} else {
	    throw new UnsupportedOperationException("Unsupported DeclarationStatement encountered: " + statement);
	}
    }
    
    private OOStatement convertASMDeclaration(IASTASMDeclaration declaration) {
	throw new NotImplementedException();
    }
    
    private OOStatement convertProblemDeclaration(IASTProblemDeclaration declaration) {
	throw new NotImplementedException();
    }
    
    private OOStatement convertSimpleDeclaration(IASTSimpleDeclaration declaration) {
	throw new NotImplementedException();
    }
}
