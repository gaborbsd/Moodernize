package hu.bme.aut.moodernize.c2j.util;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOStatement;

public class OOExpressionWithPrecedingStatements {
    public List<OOStatement> precedingStatements = new ArrayList<OOStatement>();
    public OOExpression expression;

    public OOExpressionWithPrecedingStatements(List<OOStatement> precedingStatements, OOExpression expression) {
	this(expression);
	this.precedingStatements = precedingStatements;
    }

    public OOExpressionWithPrecedingStatements(OOExpression expression) {
	super();
	this.expression = expression;
    }
}
