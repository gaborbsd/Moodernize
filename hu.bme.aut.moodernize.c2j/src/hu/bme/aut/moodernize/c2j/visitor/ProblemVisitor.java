package hu.bme.aut.moodernize.c2j.visitor;

import org.eclipse.cdt.core.dom.ast.IASTProblem;
import org.eclipse.core.runtime.OperationCanceledException;

public class ProblemVisitor extends AbstractBaseVisitor {
    public ProblemVisitor(String fileName) {
	super(fileName);
	this.shouldVisitProblems = true;
    }
    
    public int visit(IASTProblem problem) {
	if (!isCorrectContainingFile(problem)) {
	    return PROCESS_SKIP;
	}
	throw new OperationCanceledException("Compilation erros exist in the given source project! Please compile your project and fix the errors.");
    }
}
