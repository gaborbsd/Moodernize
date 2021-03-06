package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTFieldReference;

import hu.bme.aut.moodernize.c2j.util.IntegerLiteralToBooleanConverter;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OogenFactory;

public class AssignmentExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    
    public OOExpression convertAssignmentExpression(IASTBinaryExpression assignmentExpression) {
	if (lhsIsFieldReference(assignmentExpression)) {
	    OOFunctionCallExpression setterCall = new FieldReferenceConverter().getSetMethodCallForFieldReference(
		    (IASTFieldReference) assignmentExpression.getOperand1(), assignmentExpression.getOperand2());
	    IntegerLiteralToBooleanConverter.handleIntToBoolConversion(setterCall);
	    return setterCall;
	} else {
	    ExpressionConverter converter = new ExpressionConverter();
	    OOExpression lhs = TransformUtil.convertExpressionAndProcessPrecedingStatements(converter, assignmentExpression.getOperand1());
	    OOExpression rhs = TransformUtil.convertExpressionAndProcessPrecedingStatements(converter, assignmentExpression.getOperand2());	
	    
	    OOAssignmentExpression ooAssignmentExpression = factory.createOOAssignmentExpression();
	    ooAssignmentExpression.setLeftSide(lhs);
	    ooAssignmentExpression.setRightSide(rhs);

	    IntegerLiteralToBooleanConverter.handleIntToBoolConversion(ooAssignmentExpression);

	    return ooAssignmentExpression;
	}
    }

    private boolean lhsIsFieldReference(IASTBinaryExpression assignmentExpression) {
	return assignmentExpression.getOperand1() instanceof IASTFieldReference;
    }
}
