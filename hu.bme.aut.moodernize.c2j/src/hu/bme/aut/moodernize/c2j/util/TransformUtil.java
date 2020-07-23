package hu.bme.aut.moodernize.c2j.util;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTNode;

import hu.bme.aut.moodernize.c2j.converter.expression.ExpressionConverter;
import hu.bme.aut.moodernize.c2j.dataholders.FunctionSymbolTable;
import hu.bme.aut.moodernize.c2j.dataholders.TransformationDataHolder;
import hu.bme.aut.moodernize.c2j.projectcreation.MainClassCreator;
import hu.bme.aut.oogen.OOBracketedExpression;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOComment;
import hu.bme.aut.oogen.OOEnumeration;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOStatement;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OogenFactory;

public class TransformUtil {

    public static String getWithUpperCaseFirstCharacter(String s) {
	if (s == null || s.length() == 0) {
	    return "";
	}
	if (s.length() == 1) {
	    return s.substring(0, 1).toUpperCase();
	}
	return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String getWithLowerCaseFirstCharacter(String s) {
	if (s.length() == 1) {
	    return s.substring(0, 1).toLowerCase();
	}
	return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public static boolean isReferenceType(OOType type) {
	return type == null ? false : type.getClassType() != null && type.getArrayDimensions() == 0;
    }

    public static boolean listContainsVariable(List<OOVariable> variables, String varName) {
	return getVariableByName(variables, varName) != null;
    }

    public static boolean listContainsClass(List<OOClass> classes, OOClass cl) {
	for (OOClass c : classes) {
	    if (c.getName().toUpperCase().equals(cl.getName().toUpperCase())) {
		return true;
	    }
	}

	return false;
    }

    public static boolean listContainsMethod(List<OOMethod> methods, OOMethod method) {
	for (OOMethod m : methods) {
	    if (m.getName().toUpperCase().equals(method.getName().toUpperCase())) {
		return true;
	    }
	}

	return false;
    }

    public static OOMethod getMethodFromClass(OOClass cl, String methodName) {
	for (OOMethod m : cl.getMethods()) {
	    if (m.getName().equals(methodName)) {
		return m;
	    }
	}

	return null;
    }

    public static OOClass getClassByName(List<OOClass> classes, String className) {
	for (OOClass c : classes) {
	    if (c.getName().toUpperCase().equals(className.toUpperCase())) {
		return c;
	    }
	}

	return null;
    }

    public static OOClass getContainerClass(String functionName, List<OOClass> classes) {
	for (OOClass cl : classes) {
	    for (OOMethod m : cl.getMethods()) {
		if (m.getName().equals(functionName)) {
		    return cl;
		}
	    }
	}

	return null;
    }

    public static OOClass getMainClassFromClasses(List<OOClass> classes) {
	for (OOClass cl : classes) {
	    if (cl.getName().equals(MainClassCreator.MAINCLASSNAME)) {
		return cl;
	    }
	}

	throw new InvalidParameterException("MainClass not found in classes: " + classes);
    }

    public static OOMethod getFunctionByName(List<OOMethod> functions, String name) {
	for (OOMethod function : functions) {
	    if (function.getName().equals(name)) {
		return function;
	    }
	}

	return null;
    }

    public static boolean isCorrectClassName(String name) {
	if (name == null) {
	    return false;
	}
	name = name.toUpperCase();
	boolean isIncorrect = name.equals("ABSTRACT") || name.equals("ASSERT") || name.equals("BOOLEAN")
		|| name.equals("CATCH") || name.equals("CLASS") || name.equals("ENUM") || name.equals("EXTENDS")
		|| name.equals("FINAL") || name.equals("FINALLY") || name.equals("IMPLEMENTS") || name.equals("IMPORT")
		|| name.equals("INSTANCEOF") || name.equals("INTERFACE") || name.equals("NATIVE") || name.equals("NEW")
		|| name.equals("PACKAGE") || name.equals("PRIVATE") || name.equals("PROTECTED") || name.equals("PUBLIC")
		|| name.equals("STATIC") || name.equals("STRICTFP") || name.equals("SUPER")
		|| name.equals("SYNCHRONIZED") || name.equals("THIS") || name.equals("THROW") || name.equals("THROWS")
		|| name.equals("TRANSIENT") || name.equals("TRY") || name.equals("VOLATILE") || name.equals("");

	return !isIncorrect;
    }

    public static String getContainingFunctionName(IASTNode node) {
	while (node != null && !(node instanceof IASTFunctionDefinition)) {
	    node = node.getParent();
	}

	if (node != null) {
	    return ((IASTFunctionDefinition) node).getDeclarator().getName().resolveBinding().getName();
	} else {
	    return null;
	}
    }

    public static OOMethod getMethodFromClasses(List<OOClass> classes, String methodName) {
	for (OOClass cl : classes) {
	    for (OOMethod method : cl.getMethods()) {
		if (method.getName().equals(methodName)) {
		    return method;
		}
	    }
	}

	return null;
    }

    public static boolean functionBelongsToClass(String functionName, OOClass cl) {
	for (OOMethod method : cl.getMethods()) {
	    if (method.getName().equals(functionName)) {
		return true;
	    }
	}

	return false;
    }

    public static boolean listContainsEnum(List<OOEnumeration> enums, OOEnumeration enumeration) {
	for (OOEnumeration e : enums) {
	    if (e.getName().equals(enumeration.getName())) {
		return true;
	    }
	}

	return false;
    }

    public static boolean isGlobalVariable(String name) {
	OOClass mainClass = getClassByName(TransformationDataHolder.createdClasses, MainClassCreator.MAINCLASSNAME);
	if (mainClass == null) {
	    return false;
	}

	List<OOMember> globalMembers = mainClass.getMembers();
	List<String> globalVariableNames = new ArrayList<String>();
	for (OOMember member : globalMembers) {
	    globalVariableNames.add(member.getName());
	}

	return !listContainsVariable(FunctionSymbolTable.variableDeclarations, name)
		&& !listContainsVariable(FunctionSymbolTable.parameters, name) && globalVariableNames.contains(name);
    }

    public static OOVariable getVariableByName(List<OOVariable> vars, String varName) {
	for (OOVariable v : vars) {
	    if (v.getName().equals(varName)) {
		return v;
	    }
	}

	return null;
    }

    public static OOVariable getVariableByNameFromMembers(List<OOMember> vars, String varName) {
	for (OOMember v : vars) {
	    if (v.getName().equals(varName)) {
		return v;
	    }
	}

	return null;
    }

    private static void addStatementsToCurrentFunction(IASTNode node, List<OOStatement> statements) {
	String containingFunctionName = getContainingFunctionName(node);
	OOMethod containingFunction = TransformUtil.getMethodFromClasses(TransformationDataHolder.createdClasses,
		containingFunctionName);
	if (statements != null) {
	    for (OOStatement statement : statements) {
		if (statement != null && containingFunctionName != null) {
		    containingFunction.getStatements().add(statement);
		    if (statement instanceof OOVariable) {
			FunctionSymbolTable.variableDeclarations.add((OOVariable) statement);
		    }
		}
	    }
	}
    }

    public static OOExpression convertExpressionAndProcessPrecedingStatements(ExpressionConverter converter,
	    IASTExpression expression) {
	OOExpressionWithPrecedingStatements convertedExpression = converter.convertExpression(expression);
	TransformUtil.addStatementsToCurrentFunction(expression, convertedExpression.precedingStatements);

	return convertedExpression.expression;
    }

    public static OOExpression getOperandInsideBrackets(OOExpression expression) {
	while (expression instanceof OOBracketedExpression) {
	    expression = ((OOBracketedExpression) expression).getOperand();
	}

	return expression;
    }
    
    public static void createAndAttachNotRecognizedErrorComment(OOExpression expression, String errorMessage) {
	OogenFactory factory = OogenFactory.eINSTANCE;
	OOComment errorComment = factory.createOOComment();
	errorComment.setText("/*" + errorMessage + "*/");
	expression.getBeforeComments().add(errorComment);
    }
}
