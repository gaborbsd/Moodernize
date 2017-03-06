package hu.bme.aut.moodernize.c2j;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;

public class ASTWithoutIncludesVisitor extends ASTVisitor{
	private String fileName;
	private StringBuffer sb;
	private boolean lastDeclarationWasFunction;
	
	public ASTWithoutIncludesVisitor(String fn) {
		super();
		this.fileName = fn;
		sb = new StringBuffer();
		
		shouldVisitDeclarations = true;
		lastDeclarationWasFunction = false;
	}
	
	public int visit(IASTDeclaration declaration) {
		if (!(declaration.getContainingFilename().equals(fileName))) {
			return PROCESS_SKIP;
		}
		
		if (declaration instanceof IASTSimpleDeclaration && lastDeclarationWasFunction) {
			return PROCESS_SKIP;
		}
		
		if (declaration instanceof IASTFunctionDefinition) {
			lastDeclarationWasFunction = true;
		}
		
		else {
			lastDeclarationWasFunction = false;
		}
		
		sb.append(declaration.getRawSignature());
		return PROCESS_CONTINUE;
	}
	
	public String getData() {
		return sb.toString();
	}
}
