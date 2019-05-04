package hu.bme.aut.moodernize.c2j.visitor;

import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTStatement;

import hu.bme.aut.moodernize.c2j.core.TransformationDataRepository;

public class FunctionBodyVisitor extends AbstractBaseVisitor {	
	public FunctionBodyVisitor(String fileName) {
		super(fileName);
		shouldVisitDeclarations = true;
	}
	
	public int visit(IASTDeclaration decl) {
		if (!isCorrectContainingFile(decl)) {
			return PROCESS_SKIP;
		}

		if (decl instanceof IASTFunctionDefinition) {
			IASTFunctionDefinition func = (IASTFunctionDefinition) decl;
			String callerName = func.getDeclarator().getName().resolveBinding().getName();

			IASTStatement[] statements = ((IASTCompoundStatement) func.getBody()).getStatements();
			for (IASTStatement statement : statements) {
				// TODO: Handle statements and expressions: all types, in their own funtions
				if (statement instanceof IASTFunctionCallExpression) {
					IASTFunctionCallExpression call = (IASTFunctionCallExpression) ((IASTExpressionStatement) statement)
							.getExpression();
					;
					IASTExpression functionNameExpression = call.getFunctionNameExpression();
					if (functionNameExpression != null && functionNameExpression instanceof IASTIdExpression) {
						IASTIdExpression idExpression = (IASTIdExpression) functionNameExpression;
						String calledName = idExpression.getName().resolveBinding().getName();
						TransformationDataRepository.addEdgeToCallgraph(callerName, calledName);
					}
				}
			}
		}
		return PROCESS_CONTINUE;
	}
}
