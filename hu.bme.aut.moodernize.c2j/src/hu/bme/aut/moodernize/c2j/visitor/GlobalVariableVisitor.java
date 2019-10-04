package hu.bme.aut.moodernize.c2j.visitor;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTCompositeTypeSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTEnumerationSpecifier;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.moodernize.c2j.converter.declaration.SimpleDeclarationConverter;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableDeclarationList;

public class GlobalVariableVisitor extends AbstractBaseVisitor {
    private List<OOVariable> globalVariables;

    public GlobalVariableVisitor(String fileName, List<OOVariable> globalVariables) {
	super(fileName);
	this.globalVariables = globalVariables;
	shouldVisitDeclarations = true;
    }

    // TODO: Performance?
    public int visit(IASTDeclaration declaration) {
	if (!isCorrectContainingFile(declaration)) {
	    return PROCESS_SKIP;
	}

	if (!(declaration instanceof IASTSimpleDeclaration)) {
	    return PROCESS_SKIP;
	}
	IASTSimpleDeclaration simpleDeclaration = (IASTSimpleDeclaration) declaration;
	IASTDeclSpecifier specifier = simpleDeclaration.getDeclSpecifier();
	if (specifier instanceof IASTCompositeTypeSpecifier || specifier instanceof IASTEnumerationSpecifier) {
	    return PROCESS_SKIP;
	}
	IASTDeclarator[] declarators = simpleDeclaration.getDeclarators();
	if (declarators.length == 0) {
	    return PROCESS_SKIP;
	}
	boolean isGlobal = declarators[0].getName().resolveBinding().getOwner() == null;
	if (!isGlobal) {
	    return PROCESS_SKIP;
	}
	SimpleDeclarationConverter converter = new SimpleDeclarationConverter();
	OOVariableDeclarationList declaredVariables = converter.convertSimpleDeclaration(simpleDeclaration);

	for (OOVariable variable : declaredVariables.getVariableDeclarations()) {
	    if (!TransformUtil.listContainsVariable(globalVariables, variable.getName())) {
		globalVariables.add(EcoreUtil.copy(variable));
	    }
	}

	return PROCESS_SKIP;
    }
}