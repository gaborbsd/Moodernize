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
import org.eclipse.cdt.core.dom.ast.IASTProblemStatement;
import org.eclipse.cdt.core.dom.ast.IASTReturnStatement;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTSwitchStatement;
import org.eclipse.cdt.core.dom.ast.IASTWhileStatement;

import hu.bme.aut.moodernize.c2j.converter.expression.ExpressionConverter;
import hu.bme.aut.oogen.OOStatement;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class StatementConverter {
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
	} else if (statement instanceof IASTProblemStatement) {
	    return convertProblemStatement((IASTProblemStatement) statement);
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

    private OOStatement convertExpressionStatement(IASTExpressionStatement statement) {
	ExpressionConverter converter = new ExpressionConverter();
	return converter.convertExpression(statement.getExpression());
    }

    private OOStatement convertBreakStatement(IASTBreakStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertCaseStatement(IASTCaseStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertCompoundStatement(IASTCompoundStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertContinueStatement(IASTContinueStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertDeclarationStatement(IASTDeclarationStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertDefaultStatement(IASTDefaultStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertDoStatement(IASTDoStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertForStatetement(IASTForStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertGotoStatement(IASTGotoStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertIfStatement(IASTIfStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertLabelStatement(IASTLabelStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertNullStatement(IASTNullStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertProblemStatement(IASTProblemStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertReturnStatement(IASTReturnStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertSwitchStatement(IASTSwitchStatement statement) {
	throw new NotImplementedException();
    }

    private OOStatement convertWhileStatement(IASTWhileStatement statement) {
	throw new NotImplementedException();
    }
}