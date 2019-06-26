package hu.bme.aut.moodernize.c2j.converter.statement;

import org.eclipse.cdt.core.dom.ast.IASTBreakStatement;
import org.eclipse.cdt.core.dom.ast.IASTCaseStatement;
import org.eclipse.cdt.core.dom.ast.IASTContinueStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclarationStatement;
import org.eclipse.cdt.core.dom.ast.IASTDefaultStatement;
import org.eclipse.cdt.core.dom.ast.IASTDoStatement;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTForStatement;
import org.eclipse.cdt.core.dom.ast.IASTGotoStatement;
import org.eclipse.cdt.core.dom.ast.IASTIfStatement;
import org.eclipse.cdt.core.dom.ast.IASTLabelStatement;
import org.eclipse.cdt.core.dom.ast.IASTNullStatement;
import org.eclipse.cdt.core.dom.ast.IASTReturnStatement;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTSwitchStatement;
import org.eclipse.cdt.core.dom.ast.IASTWhileStatement;

import hu.bme.aut.moodernize.c2j.converter.expression.ExpressionConverter;
import hu.bme.aut.oogen.OOCaseStatement;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFor;
import hu.bme.aut.oogen.OOIf;
import hu.bme.aut.oogen.OOReturn;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOWhile;
import hu.bme.aut.oogen.OogenFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class StatementConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOStatement convertStatement(IASTStatement statement) {
	if (statement instanceof IASTBreakStatement) {
	    return convertBreakStatement((IASTBreakStatement) statement);
	} else if (statement instanceof IASTCaseStatement) {
	    return convertCaseStatement((IASTCaseStatement) statement);
	} else if (statement instanceof IASTContinueStatement) {
	    return convertContinueStatement((IASTContinueStatement) statement);
	} else if (statement instanceof IASTDeclarationStatement) {
	    return convertDeclarationStatement((IASTDeclarationStatement) statement);
	} else if (statement instanceof IASTDefaultStatement) {
	    return convertDefaultStatement((IASTDefaultStatement) statement);
	} else if (statement instanceof IASTDoStatement) {
	    return convertDoStatement((IASTDoStatement) statement);
	} else if (statement instanceof IASTExpressionStatement) {
	    return convertExpressionStatement((IASTExpressionStatement) statement);
	} else if (statement instanceof IASTForStatement) {
	    return convertForStatetement((IASTForStatement) statement);
	} else if (statement instanceof IASTGotoStatement) {
	    return convertGotoStatement((IASTGotoStatement) statement);
	} else if (statement instanceof IASTIfStatement) {
	    return convertIfStatement((IASTIfStatement) statement);
	} else if (statement instanceof IASTLabelStatement) {
	    return convertLabelStatement((IASTLabelStatement) statement);
	} else if (statement instanceof IASTNullStatement) {
	    return convertNullStatement((IASTNullStatement) statement);
	} else if (statement instanceof IASTReturnStatement) {
	    return convertReturnStatement((IASTReturnStatement) statement);
	} else if (statement instanceof IASTSwitchStatement) {
	    return convertSwitchStatement((IASTSwitchStatement) statement);
	} else if (statement instanceof IASTWhileStatement) {
	    return convertWhileStatement((IASTWhileStatement) statement);
	}

	else {
	    throw new UnsupportedOperationException("Not supported statement encountered: " + statement);
	}
    }

    private OOStatement convertBreakStatement(IASTBreakStatement statement) {
	return factory.createOOBreakStatement();
    }

    private OOStatement convertCaseStatement(IASTCaseStatement statement) {
	OOCaseStatement caseStatement = factory.createOOCaseStatement();
	caseStatement.setExpression(new ExpressionConverter().convertExpression(statement.getExpression()));
	
	return caseStatement;
    }

    private OOStatement convertContinueStatement(IASTContinueStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertDeclarationStatement(IASTDeclarationStatement statement) {
	DeclarationStatementConverter converter = new DeclarationStatementConverter();
	return converter.convertDeclarationStatement(statement);
    }

    private OOStatement convertDefaultStatement(IASTDefaultStatement statement) {
	return factory.createOODefaultStatement();
    }

    private OOStatement convertDoStatement(IASTDoStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertExpressionStatement(IASTExpressionStatement statement) {
	ExpressionConverter converter = new ExpressionConverter();
	return converter.convertExpression(statement.getExpression());
    }

    private OOStatement convertForStatetement(IASTForStatement statement) {
	CompoundStatementConverter converter = new CompoundStatementConverter();
	ExpressionConverter expressionConverter = new ExpressionConverter();
	OOFor forStatement = factory.createOOFor();

	forStatement.setCondition(converter.convertConditionExpression(statement.getConditionExpression()));
	converter.addStatementsToBody(statement.getBody(), forStatement.getBodyStatements());
	forStatement.setIncrementExpression(expressionConverter.convertExpression(statement.getIterationExpression()));
	forStatement.setInitStatement(convertStatement(statement.getInitializerStatement()));

	return forStatement;
    }

    private OOStatement convertGotoStatement(IASTGotoStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertIfStatement(IASTIfStatement statement) {
	CompoundStatementConverter converter = new CompoundStatementConverter();
	OOIf ifStatement = factory.createOOIf();

	ifStatement.setCondition(converter.convertConditionExpression(statement.getConditionExpression()));
	converter.addStatementsToBody(statement.getThenClause(), ifStatement.getBodyStatements());
	converter.addStatementsToBody(statement.getElseClause(), ifStatement.getElseStatements());

	return ifStatement;
    }

    private OOStatement convertLabelStatement(IASTLabelStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertNullStatement(IASTNullStatement statement) {
	return factory.createOOEmptyStatement();
    }

    private OOStatement convertReturnStatement(IASTReturnStatement statement) {
	OOExpression returnExpression = new ExpressionConverter().convertExpression(statement.getReturnValue());
	OOReturn returnStatement = factory.createOOReturn();
	returnStatement.setReturnedExpresssion(returnExpression);
	return returnStatement;
    }

    private OOStatement convertSwitchStatement(IASTSwitchStatement statement) {
	SwitchStatementConverter converter = new SwitchStatementConverter();
	return converter.convertSwitchStatement(statement);
    }

    private OOStatement convertWhileStatement(IASTWhileStatement statement) {
	CompoundStatementConverter converter = new CompoundStatementConverter();
	OOWhile whileStatement = factory.createOOWhile();

	whileStatement.setCondition(converter.convertConditionExpression(statement.getCondition()));
	converter.addStatementsToBody(statement.getBody(), whileStatement.getBodyStatements());

	return whileStatement;
    }
}