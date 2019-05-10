package hu.bme.aut.moodernize.c2j.visitor;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTStatement;

import converter.statement.StatementConverter;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOStatement;

public class FunctionBodyVisitor extends AbstractBaseVisitor {
	private List<OOMethod> functions;

	public FunctionBodyVisitor(String fileName,  List<OOMethod> functions) {
		super(fileName);
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
			StatementConverter converter = new StatementConverter();
			for (IASTStatement statement : statements) {
				OOStatement convertedStatement = converter.convertStatement(statement);
				TransformUtil.getFunctionByName(functions, functionName).getStatements().add(convertedStatement);
			}
		}
		return PROCESS_CONTINUE;
	}

	/*private void handleFunctionCallExpression(OOMethod currentFunction, IASTFunctionCallExpression functionCallExpression) {
		IASTExpression functionNameExpression = (IASTIdExpression) functionCallExpression.getFunctionNameExpression();
		if (functionNameExpression != null && functionNameExpression instanceof IASTIdExpression) {
			IASTIdExpression idExpression = (IASTIdExpression) functionNameExpression;
			String calledName = idExpression.getName().resolveBinding().getName();
			callGraph.add(new Calledge(currentFunction.getName(), calledName));
		}
	}*/
}
