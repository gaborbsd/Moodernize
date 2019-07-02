package hu.bme.aut.moodernize.c2j.converter.statement;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTSwitchStatement;

import hu.bme.aut.moodernize.c2j.converter.expression.ExpressionConverter;
import hu.bme.aut.oogen.OOCase;
import hu.bme.aut.oogen.OOCompoundStatement;
import hu.bme.aut.oogen.OODefault;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOSwitch;
import hu.bme.aut.oogen.OogenFactory;

public class SwitchStatementConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    private IASTSwitchStatement cdtSwitch;
    private OOSwitch ooSwitch = factory.createOOSwitch();

    private Map<OOCompoundStatement, List<OOStatement>> statementBodies = new HashMap<OOCompoundStatement, List<OOStatement>>();

    public OOSwitch convertSwitchStatement(IASTSwitchStatement statement) {
	this.cdtSwitch = statement;
	collectCaseAndDefaultStatements();
	handleControllerExpression();
	constructAndSetCaseAndDefaultStatements();
	return ooSwitch;
    }

    private void handleControllerExpression() {
	ExpressionConverter converter = new ExpressionConverter();
	ooSwitch.setControllerExpression(converter.convertExpression(cdtSwitch.getControllerExpression()));
    }

    private void collectCaseAndDefaultStatements() {
	IASTStatement[] bodyStatements = ((IASTCompoundStatement) cdtSwitch.getBody()).getStatements();
	StatementConverter converter = new StatementConverter();

	OOCompoundStatement key = null;
	for (IASTStatement bodyStatement : bodyStatements) {
	    if (bodyStatement != null) {
		OOStatement convertedStatement = converter.convertStatement(bodyStatement);
		if (convertedStatement instanceof OOCase || convertedStatement instanceof OODefault) {
		    OOCompoundStatement compoundStatement = (OOCompoundStatement) convertedStatement;
		    statementBodies.putIfAbsent(compoundStatement, new ArrayList<OOStatement>());
		    key = compoundStatement;
		} else {
		    statementBodies.get(key).add(convertedStatement);
		}
	    }
	}
    }

    private void constructAndSetCaseAndDefaultStatements() {
	for (Map.Entry<OOCompoundStatement, List<OOStatement>> entry : statementBodies.entrySet()) {
	    OOCompoundStatement compoundStatement = entry.getKey();
	    for (OOStatement statement : entry.getValue()) {
		compoundStatement.getBodyStatements().add(statement);
	    }
	    if (compoundStatement instanceof OODefault) {
		ooSwitch.setDefaultStatement((OODefault) compoundStatement);
	    } else if (compoundStatement instanceof OOCase) {
		ooSwitch.getCaseStatements().add((OOCase) compoundStatement);
	    } else {
		throw new InvalidParameterException(
			"Invalid compound statement found in switch body: " + compoundStatement);
	    }
	}
    }
}
