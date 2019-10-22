package hu.bme.aut.moodernize.c2j.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOBracketedExpression;
import hu.bme.aut.oogen.OOComment;
import hu.bme.aut.oogen.OOComparatorExpression;
import hu.bme.aut.oogen.OOEqualsExpression;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OOInitializerList;
import hu.bme.aut.oogen.OOIntegerLiteral;
import hu.bme.aut.oogen.OOLogicalExpression;
import hu.bme.aut.oogen.OOLogicalLiteral;
import hu.bme.aut.oogen.OOMinusExpression;
import hu.bme.aut.oogen.OONotEqualsExpression;
import hu.bme.aut.oogen.OOOneOperandLogicalExpression;
import hu.bme.aut.oogen.OOTernaryOperator;
import hu.bme.aut.oogen.OOTwoOperandLogicalExpression;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableDeclarationList;
import hu.bme.aut.oogen.OOVariableReferenceExpression;
import hu.bme.aut.oogen.OogenFactory;

public class IntegerLiteralToBooleanConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public static OOExpression handleIntToBoolConversion(OOExpression expression) {
	if (expression instanceof OOTwoOperandLogicalExpression) {
	    convertTwoOperandLogicalExpression((OOTwoOperandLogicalExpression) expression);
	} else if (expression instanceof OOMinusExpression) {
	    OOExpression operand = ((OOMinusExpression) expression).getOperand();
	    operand = TransformUtil.getOperandInsideBrackets(operand);
	    if (operand != null && operand instanceof OOIntegerLiteral) {
		return createBoolFromLogicalInt((OOIntegerLiteral) operand);
	    }
	    else {
		return expression;
	    }
	} else if (expression instanceof OOOneOperandLogicalExpression) {
	    convertOneOperandLogicalExpression((OOOneOperandLogicalExpression) expression);
	} else if (expression instanceof OOTernaryOperator) {
	    convertTernaryOperator((OOTernaryOperator) expression);
	} else if (expression instanceof OOBracketedExpression) {
	    convertBracketedExpression((OOBracketedExpression) expression);
	} else if (expression instanceof OOAssignmentExpression) {
	    convertAssignmentExpression((OOAssignmentExpression) expression);
	} else if (expression instanceof OOComparatorExpression) {
	    convertComparatorExpression((OOComparatorExpression) expression);
	} else if (expression instanceof OOVariableDeclarationList) {
	    handleIntToBoolConversion((OOVariableDeclarationList) expression);
	} else if (expression instanceof OOFunctionCallExpression) {
	    convertSetterCallExpression((OOFunctionCallExpression) expression);
	} else if (expression instanceof OOInitializerList) {
	    convertInitializerList((OOInitializerList) expression);
	}
	if (expression instanceof OOIntegerLiteral) {
	    return createBoolFromLogicalInt((OOIntegerLiteral) expression);
	} else
	    return expression;
    }

    private static void convertTwoOperandLogicalExpression(OOTwoOperandLogicalExpression expression) {
	expression.setLeftSide(handleIntToBoolConversion(expression.getLeftSide()));
	expression.setRightSide(handleIntToBoolConversion(expression.getRightSide()));
    }

    private static void convertOneOperandLogicalExpression(OOOneOperandLogicalExpression expression) {
	expression.setOperand(handleIntToBoolConversion(expression.getOperand()));
    }

    private static void convertTernaryOperator(OOTernaryOperator expression) {
	expression.setCondition(handleIntToBoolConversion(expression.getCondition()));
	// expression.setPositiveBranch(handleIntToBoolConversion(expression.getPositiveBranch()));
	// expression.setNegativeBranch(handleIntToBoolConversion(expression.getNegativeBranch()));
    }

    private static void convertBracketedExpression(OOBracketedExpression expression) {
	expression.setOperand(handleIntToBoolConversion(expression.getOperand()));
    }

    private static void convertAssignmentExpression(OOAssignmentExpression expression) {
	OOExpression lhs = expression.getLeftSide();

	if (lhs instanceof OOVariableReferenceExpression) {
	    OOVariableReferenceExpression referredVariable = (OOVariableReferenceExpression) lhs;
	    if (referredVariable.getVariable().getType().getBaseType() == OOBaseType.BOOLEAN) {
		expression.setRightSide(handleIntToBoolConversion(expression.getRightSide()));
	    }
	}
    }

    private static void convertComparatorExpression(OOComparatorExpression expression) {
	if (expression instanceof OOEqualsExpression || expression instanceof OONotEqualsExpression) {
	    OOExpression lhs = expression.getLeftSide();
	    if (lhs instanceof OOVariableReferenceExpression) {
		OOVariableReferenceExpression referredVariable = (OOVariableReferenceExpression) lhs;
		if (referredVariable.getVariable().getType().getBaseType() == OOBaseType.BOOLEAN) {
		    expression.setRightSide(handleIntToBoolConversion(expression.getRightSide()));
		}
	    }
	}
    }

    public static void handleIntToBoolConversion(OOVariableDeclarationList expression) {
	List<OOVariable> variableDeclarations = expression.getVariableDeclarations();
	for (OOVariable declaration : variableDeclarations) {
	    if (declaration.getType().getBaseType() == OOBaseType.BOOLEAN) {
		declaration.setInitializerExpression(handleIntToBoolConversion(declaration.getInitializerExpression()));
	    }
	}
    }

    private static void convertSetterCallExpression(OOFunctionCallExpression expression) {
	OOExpression ownerExpression = expression.getOwnerExpression();
	if (ownerExpression instanceof OOVariableReferenceExpression) {
	    OOVariable referredVariable = ((OOVariableReferenceExpression) ownerExpression).getVariable();
	    if (referredVariable.getType().getBaseType() == OOBaseType.BOOLEAN) {
		OOExpression convertedArgument = handleIntToBoolConversion(expression.getArgumentExpressions().get(0));
		expression.getArgumentExpressions().clear();
		expression.getArgumentExpressions().add(convertedArgument);
	    }
	}
    }

    private static void convertInitializerList(OOInitializerList expression) {
	List<OOExpression> convertedExpressions = new ArrayList<OOExpression>();
	for (OOExpression initExp : expression.getInitializerExpressions()) {
	    convertedExpressions.add(handleIntToBoolConversion(initExp));
	}
	expression.getInitializerExpressions().clear();
	expression.getInitializerExpressions().addAll(convertedExpressions);
    }

    private static OOLogicalExpression createBoolFromLogicalInt(OOIntegerLiteral integerLiteral) {
	OOLogicalLiteral logicalLiteral = factory.createOOLogicalLiteral();
	if (integerLiteral.getValue() == 0) {
	    logicalLiteral.setValue(false);
	} else {
	    logicalLiteral.setValue(true);
	}

	for (OOComment comment : integerLiteral.getBeforeComments()) {
	    logicalLiteral.getBeforeComments().add(EcoreUtil.copy(comment));
	}

	for (OOComment comment : integerLiteral.getAfterComments()) {
	    logicalLiteral.getAfterComments().add(EcoreUtil.copy(comment));
	}

	return logicalLiteral;
    }
}
