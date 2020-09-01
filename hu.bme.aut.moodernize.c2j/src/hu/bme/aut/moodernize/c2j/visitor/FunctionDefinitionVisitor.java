package hu.bme.aut.moodernize.c2j.visitor;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTStatement;

import hu.bme.aut.moodernize.c2j.commentmapping.CommentProcessor;
import hu.bme.aut.moodernize.c2j.converter.statement.StatementConverter;
import hu.bme.aut.moodernize.c2j.dataholders.CommentMappingDataHolder;
import hu.bme.aut.moodernize.c2j.dataholders.FunctionSymbolTable;
import hu.bme.aut.moodernize.c2j.pointerconversion.PointerConversionDataHolder;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableDeclarationList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class FunctionDefinitionVisitor extends AbstractBaseVisitor {
    private List<OOMethod> functions;
    
    public FunctionDefinitionVisitor(String fileName, List<OOMethod> functions) {
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
	    IASTFunctionDefinition function = (IASTFunctionDefinition) declaration;
	    String functionName = function.getDeclarator().getName().resolveBinding().getName();
	    FunctionSymbolTable.clear();
	    OOMethod correspondingFunction = TransformUtil.getFunctionByName(functions, functionName);
	    
	    if (correspondingFunction == null) {
		return PROCESS_SKIP;
	    }
	    
	    for (OOVariable parameter : correspondingFunction.getParameters()) {
		FunctionSymbolTable.parameters.add(parameter);
	    }

	    IASTStatement[] statements = ((IASTCompoundStatement) function.getBody()).getStatements();
	    StatementConverter converter = new StatementConverter();
	    for (IASTStatement statement : statements) {
		try {
		    OOStatement convertedStatement = converter.convertStatement(statement);
		    CommentProcessor.attachOwnedCommentsToOwner(convertedStatement,
			    CommentMappingDataHolder.findAllOwnedComments(statement));
		    addToDataHolderIfVariableDeclaration(convertedStatement, functionName);

		    correspondingFunction.getStatements().add(convertedStatement);
		} catch (UnsupportedOperationException | NotImplementedException e) {
		    continue;
		}
	    }
	}
	return PROCESS_CONTINUE;
    }

    private void addToDataHolderIfVariableDeclaration(OOStatement statement, String containingFunctionName) {
	if (statement instanceof OOVariableDeclarationList) {
	    for (OOVariable declaredVariable : ((OOVariableDeclarationList) statement).getVariableDeclarations()) {
		FunctionSymbolTable.addDeclaration(declaredVariable);
		PointerConversionDataHolder.addDeclaration(declaredVariable, "FUNCTION_" + containingFunctionName);
	    }
	}
    }
}
