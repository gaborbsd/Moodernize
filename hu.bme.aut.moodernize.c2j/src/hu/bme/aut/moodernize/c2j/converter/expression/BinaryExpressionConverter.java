package hu.bme.aut.moodernize.c2j.converter.expression;

import java.util.Dictionary;

import org.eclipse.cdt.core.dom.ast.IASTBinaryExpression;

import hu.bme.aut.oogen.OOAdditionExpression;
import hu.bme.aut.oogen.OOAndExpression;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OODivisionExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOLogicalExpression;
import hu.bme.aut.oogen.OOMultiplicationExpression;
import hu.bme.aut.oogen.OOOrExpression;
import hu.bme.aut.oogen.OOSubtractionExpression;
import hu.bme.aut.oogen.OogenFactory;

public class BinaryExpressionConverter {
	private static OogenFactory factory = OogenFactory.eINSTANCE;

	public OOExpression convertBinaryExpression(IASTBinaryExpression binaryExpression) {
		ExpressionConverter converter = new ExpressionConverter();
		OOExpression lhs = converter.convertExpression(binaryExpression.getOperand1());
		OOExpression rhs = converter.convertExpression(binaryExpression.getOperand2());
		int operator = binaryExpression.getOperator();

		return handleByOperator(operator, lhs, rhs);
	}

	private OOExpression handleByOperator(int operator, OOExpression lhs, OOExpression rhs) {
		switch (operator) {
		case IASTBinaryExpression.op_assign:
			OOAssignmentExpression assignmentExpression = factory.createOOAssignmentExpression();
			assignmentExpression.setLeftSide(lhs);
			assignmentExpression.setRightSide(rhs);
			return assignmentExpression;
			
		case IASTBinaryExpression.op_plus:
			OOAdditionExpression additionExpression = factory.createOOAdditionExpression();
			additionExpression.setLeftSide(lhs);
			additionExpression.setRightSide(rhs);
			return additionExpression;
			
		case IASTBinaryExpression.op_minus:
			OOSubtractionExpression substractionExpression = factory.createOOSubtractionExpression();
			substractionExpression.setLeftSide(lhs);
			substractionExpression.setRightSide(rhs);
			return substractionExpression;
			
		case IASTBinaryExpression.op_multiply:
			OOMultiplicationExpression multiExpression = factory.createOOMultiplicationExpression();
			multiExpression.setLeftSide(lhs);
			multiExpression.setRightSide(rhs);
			return multiExpression;
			
		case IASTBinaryExpression.op_divide:
			OODivisionExpression divisionExpression = factory.createOODivisionExpression();
			divisionExpression.setLeftSide(lhs);
			divisionExpression.setRightSide(rhs);
			return divisionExpression;
						
		case IASTBinaryExpression.op_logicalAnd:
			if (!(lhs instanceof OOLogicalExpression) || !(rhs instanceof OOLogicalExpression)) {
				throw new IllegalArgumentException("Both sides of an AndExpression must be of type LogicalExpression.");
			}
			OOAndExpression andExpression = factory.createOOAndExpression();
			andExpression.setLeftSide((OOLogicalExpression) lhs);
			andExpression.setRightSide((OOLogicalExpression) rhs);
			return andExpression;
			
		case IASTBinaryExpression.op_logicalOr:
			if (!(lhs instanceof OOLogicalExpression) || !(rhs instanceof OOLogicalExpression)) {
				throw new IllegalArgumentException("Both sides of an OrExpression must be of type LogicalExpression.");
			}
			OOOrExpression orExpression = factory.createOOOrExpression();
			orExpression.setLeftSide((OOLogicalExpression) lhs);
			orExpression.setRightSide((OOLogicalExpression) rhs);
			return orExpression;			
			
		default: throw new UnsupportedOperationException("The following binary expression is not yet supported: " + operator);
		}
	}
}
