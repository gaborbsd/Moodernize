package hu.bme.aut.moodernize.c2j.visitor;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTNode;

import hu.bme.aut.oogen.OogenFactory;

public class CdtBaseVisitor extends ASTVisitor {
	protected String containingFileName;
	protected static OogenFactory factory = OogenFactory.eINSTANCE;
	
	public CdtBaseVisitor(String fileName) {
		this.containingFileName = fileName;
	}
	
	protected boolean isCorrectContainingFile(IASTNode node) {
		return node.getContainingFilename().equals(this.containingFileName);
	}
}
