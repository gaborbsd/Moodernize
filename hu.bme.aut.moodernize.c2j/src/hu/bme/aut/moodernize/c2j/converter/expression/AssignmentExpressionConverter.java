package hu.bme.aut.moodernize.c2j.converter.expression;

import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;
import org.eclipse.cdt.core.dom.ast.IASTFieldReference;

import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OogenFactory;

public class AssignmentExpressionConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    // Prerequisite: operator is op_assignment
    public OOExpression convertAssignmentExpression(IASTBinaryExpression assignmentExpression) {
	if (lhsIsFieldReference(assignmentExpression)) {
	    OOFunctionCallExpression setterCall = new FieldReferenceConverter().getSetMethodCallForFieldReference(
		    (IASTFieldReference) assignmentExpression.getOperand1(), assignmentExpression.getOperand2());

	    return setterCall;
	} else {
	    ExpressionConverter converter = new ExpressionConverter();
	    OOExpression lhs = converter.convertExpression(assignmentExpression.getOperand1());
	    OOExpression rhs = converter.convertExpression(assignmentExpression.getOperand2());

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
