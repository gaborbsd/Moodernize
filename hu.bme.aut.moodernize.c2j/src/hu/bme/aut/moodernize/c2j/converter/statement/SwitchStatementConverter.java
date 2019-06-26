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
import hu.bme.aut.oogen.OOCaseStatement;
import hu.bme.aut.oogen.OOCompoundStatement;
import hu.bme.aut.oogen.OODefaultStatement;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOSwitchStatement;
import hu.bme.aut.oogen.OogenFactory;

public class SwitchStatementConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    private IASTSwitchStatement cdtSwitch;
    private OOSwitchStatement ooSwitch = factory.createOOSwitchStatement();

    private Map<OOCompoundStatement, List<OOStatement>> statementBodies = new HashMap<OOCompoundStatement, List<OOStatement>>();

    public OOSwitchStatement convertSwitchStatement(IASTSwitchStatement statement) {
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
		if (convertedStatement instanceof OOCaseStatement || convertedStatement instanceof OODefaultStatement) {
		    OOCompoundStatement compoundStatement = (OOCompoundStatement) convertedStatement;
		    statementBodies.putIfAbsent(compoundStatement, new ArrayList<OOStatement>());
		    key = compoundStatement;
		}  else {
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
	    if (compoundStatement instanceof OODefaultStatement) {
		ooSwitch.setDefaultStatement((OODefaultStatement) compoundStatement);
	    } else if (compoundStatement instanceof OOCaseStatement) {
		ooSwitch.getCaseStatements().add((OOCaseStatement) compoundStatement);
	    } else {
		throw new InvalidParameterException(
			"Invalid compound statement found in switch body: " + compoundStatement);
	    }
	}
    }
}
