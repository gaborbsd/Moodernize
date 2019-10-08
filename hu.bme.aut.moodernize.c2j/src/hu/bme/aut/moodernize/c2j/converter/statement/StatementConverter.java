package hu.bme.aut.moodernize.c2j.converter.statement;

import org.eclipse.cdt.core.dom.ast.IASTBreakStatement;
import org.eclipse.cdt.core.dom.ast.IASTCaseStatement;
import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
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
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOCase;
import hu.bme.aut.oogen.OOCompoundStatement;
import hu.bme.aut.oogen.OODoWhile;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFor;
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
	} else if (statement instanceof IASTCompoundStatement) {
	    return convertCompoundStatement((IASTCompoundStatement) statement);
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
	return factory.createOOBreak();
    }

    private OOStatement convertCaseStatement(IASTCaseStatement statement) {
	OOCase caseStatement = factory.createOOCase();
	caseStatement.setExpression(TransformUtil
		.convertExpressionAndProcessPrecedingStatements(new ExpressionConverter(), statement.getExpression()));

	return caseStatement;
    }

    private OOStatement convertCompoundStatement(IASTCompoundStatement statement) {
	OOCompoundStatement compoundStatement = factory.createOOCompoundStatement();
	new CompoundStatementConverter().addStatementsToBody(statement.getStatements(),
		compoundStatement.getBodyStatements());

	return compoundStatement;
    }

    private OOStatement convertContinueStatement(IASTContinueStatement statement) {
	return factory.createOOContinue();
    }

    private OOStatement convertDeclarationStatement(IASTDeclarationStatement statement) {
	DeclarationStatementConverter converter = new DeclarationStatementConverter();
	return converter.convertDeclarationStatement(statement);
    }

    private OOStatement convertDefaultStatement(IASTDefaultStatement statement) {
	return factory.createOODefault();
    }

    private OOStatement convertDoStatement(IASTDoStatement statement) {
	CompoundStatementConverter converter = new CompoundStatementConverter();
	OODoWhile doWhile = factory.createOODoWhile();

	doWhile.setCondition(converter.convertConditionExpression(statement.getCondition()));
	converter.addStatementsToBody(statement.getBody(), doWhile.getBodyStatements());

	return doWhile;
    }

    private OOStatement convertExpressionStatement(IASTExpressionStatement statement) {
	return TransformUtil.convertExpressionAndProcessPrecedingStatements(new ExpressionConverter(),
		statement.getExpression());
    }

    private OOStatement convertForStatetement(IASTForStatement statement) {
	CompoundStatementConverter converter = new CompoundStatementConverter();
	ExpressionConverter expressionConverter = new ExpressionConverter();
	OOFor forStatement = factory.createOOFor();

	forStatement.setCondition(converter.convertConditionExpression(statement.getConditionExpression()));
	converter.addStatementsToBody(statement.getBody(), forStatement.getBodyStatements());
	forStatement.setIncrementExpression(TransformUtil.convertExpressionAndProcessPrecedingStatements(
		expressionConverter, statement.getIterationExpression()));
	forStatement.setInitStatement(convertStatement(statement.getInitializerStatement()));

	return forStatement;
    }

    private OOStatement convertGotoStatement(IASTGotoStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertIfStatement(IASTIfStatement statement) {
	IfStatementConverter converter = new IfStatementConverter();
	return converter.convertIfStatement(statement);
    }

    private OOStatement convertLabelStatement(IASTLabelStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertNullStatement(IASTNullStatement statement) {
	return factory.createOOEmptyStatement();
    }

    private OOStatement convertReturnStatement(IASTReturnStatement statement) {
	OOExpression returnExpression = TransformUtil
		.convertExpressionAndProcessPrecedingStatements(new ExpressionConverter(), statement.getReturnValue());
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