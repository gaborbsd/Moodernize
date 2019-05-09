package hu.bme.aut.moodernize.c2j.visitor;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTStatement;

import converter.ExpressionConverter;
import hu.bme.aut.moodernize.c2j.callchain.Calledge;
import hu.bme.aut.moodernize.c2j.callchain.Callgraph;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOMethod;

public class FunctionBodyVisitor extends AbstractBaseVisitor {
	private Callgraph callGraph;
	private List<OOMethod> functions;

	public FunctionBodyVisitor(String fileName, Callgraph callGraph, List<OOMethod> functions) {
		super(fileName);
		this.callGraph = callGraph;
		this.functions = functions;

		shouldVisitDeclarations = true;
	}

	@Override
	public int visit(IASTDeclaration declaration) {
		if (!isCorrectContainingFile(declaration)) {
			return PROCESS_SKIP;
		}

		if (declaration instanceof IASTFunctionDefinition) {
			IASTFunctionDefinition func = (IASTFunctionDefinition) declaration;
			String functionName = func.getDeclarator().getName().resolveBinding().getName();

			IASTStatement[] statements = ((IASTCompoundStatement) func.getBody()).getStatements();
			for (IASTStatement statement : statements) {
				if (statement instanceof IASTExpressionStatement) {
					OOMethod currentFunction = TransformUtil.getFunctionByName(functions, functionName);
					handleExpressionStatement(currentFunction, (IASTExpressionStatement) statement);
				}
			}
		}
		return PROCESS_CONTINUE;
	}

	private void handleExpressionStatement(OOMethod currentFunction, IASTExpressionStatement expressionStatement) {
		IASTExpression expression = expressionStatement.getExpression();
		ExpressionConverter converter = new ExpressionConverter();
		OOExpression convertedExpression = converter.convertExpression(expression);
		//TODO: Create the statement and add it to the current function.
		
	}

	private void handleFunctionCallExpression(OOMethod currentFunction, IASTFunctionCallExpression functionCallExpression) {
		IASTExpression functionNameExpression = (IASTIdExpression) functionCallExpression.getFunctionNameExpression();
		if (functionNameExpression != null && functionNameExpression instanceof IASTIdExpression) {
			IASTIdExpression idExpression = (IASTIdExpression) functionNameExpression;
			String calledName = idExpression.getName().resolveBinding().getName();
			callGraph.add(new Calledge(currentFunction.getName(), calledName));
		}
	}
}
