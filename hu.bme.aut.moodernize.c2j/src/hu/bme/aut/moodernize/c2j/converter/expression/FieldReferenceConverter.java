package hu.bme.aut.moodernize.c2j.converter.expression;

import java.rmi.UnexpectedException;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFieldReference;

import hu.bme.aut.moodernize.c2j.dataholders.FunctionSymbolTable;
import hu.bme.aut.moodernize.c2j.dataholders.RemovedParameterDataHolder;
import hu.bme.aut.moodernize.c2j.dataholders.TransformationDataHolder;
import hu.bme.aut.moodernize.c2j.util.OOExpressionWithPrecedingStatements;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOBracketedExpression;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOExpression;
import hu.bme.aut.oogen.OOFunctionCallExpression;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;
import hu.bme.aut.oogen.OogenFactory;

// Ha meg nincs kirakva valtozoba, akkor deklaracio + arra referencia megy vissza
// Egyebkent csak a referencia a meglevo deklaraciora
public class FieldReferenceConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public OOExpressionWithPrecedingStatements convertFieldReference(IASTFieldReference fieldReference) {
	String fieldName = fieldReference.getFieldName().resolveBinding().getName();
	String searchName = "generated" + TransformUtil.getWithUpperCaseFirstCharacter(fieldName);
	OOVariable previousDeclaration = FunctionSymbolTable.getFieldReferenceDeclarationByName(searchName);

	if (previousDeclaration == null) {
	    OOFunctionCallExpression getterCall = factory.createOOFunctionCallExpression();
	    getterCall.setFunctionName("get" + TransformUtil.getWithUpperCaseFirstCharacter(fieldName));
	    setOwnerExpression(getterCall, fieldReference);

	    OOVariable declaration;
	    try {
		declaration = generatePrecedingDeclarationForFieldReference(fieldReference, getterCall);
	    } catch (UnexpectedException e) {
		return new OOExpressionWithPrecedingStatements(getterCall);
	    }
	    if (declaration == null) {
		// TODO: Failed to get the variable reference expression
		return new OOExpressionWithPrecedingStatements(getterCall);
	    }
	    FunctionSymbolTable.fieldReferencePrecedingDeclarations.add(declaration);

	    OOVariableReferenceExpression declarationReference = factory.createOOVariableReferenceExpression();
	    declarationReference.setVariable(declaration);
	    OOExpressionWithPrecedingStatements declarationAndReference = new OOExpressionWithPrecedingStatements(
		    declarationReference);
	    declarationAndReference.precedingStatements.add(declaration);

	    FunctionSymbolTable.variableDeclarations.add(declaration);
	    return declarationAndReference;
	} else {
	    OOVariableReferenceExpression declarationReference = factory.createOOVariableReferenceExpression();
	    declarationReference.setVariable(previousDeclaration);

	    return new OOExpressionWithPrecedingStatements(declarationReference);
	}
    }

    private void setOwnerExpression(OOFunctionCallExpression getterCall, IASTFieldReference fieldReference) {
	String containingFunctionName = TransformUtil.getContainingFunctionName(fieldReference);
	OOExpression ownerExpression = TransformUtil.convertExpressionAndProcessPrecedingStatements(
		new ExpressionConverter(), fieldReference.getFieldOwner());

	if (ownerExpression instanceof OOVariableReferenceExpression) {
	    String referredName = ((OOVariableReferenceExpression) ownerExpression).getVariable().getName();
	    for (Map.Entry<String, String> removedParameterNameEntry : RemovedParameterDataHolder
		    .getRemovedParameterNames()) {
		if (removedParameterNameEntry.getKey().equals(containingFunctionName)
			&& removedParameterNameEntry.getValue().equals(referredName)) {
		    getterCall.setOwnerExpression((factory.createOOThisLiteral()));
		    return;
		}
	    }
	}

	getterCall.setOwnerExpression(ownerExpression);
    }

    private OOVariable generatePrecedingDeclarationForFieldReference(IASTFieldReference fieldReference,
	    OOFunctionCallExpression getterCall) throws UnexpectedException {
	OOExpression ownerExpression = TransformUtil.convertExpressionAndProcessPrecedingStatements(
		new ExpressionConverter(), fieldReference.getFieldOwner());

	while (ownerExpression instanceof OOBracketedExpression) {
	    ownerExpression = ((OOBracketedExpression) ownerExpression).getOperand();
	}
	if (ownerExpression instanceof OOVariableReferenceExpression) {
	    try {
		return createGeneratedDeclarationForVariableReference((OOVariableReferenceExpression) ownerExpression,
			fieldReference, getterCall);
	    } catch (UnexpectedException e) {
		throw e;
	    }

	}

	return null;
    }

    private OOVariable createGeneratedDeclarationForVariableReference(OOVariableReferenceExpression varRef,
	    IASTFieldReference fieldRef, OOFunctionCallExpression getterCall) throws UnexpectedException {
	OOVariable referredVariable = varRef.getVariable();
	String fieldName = fieldRef.getFieldName().resolveBinding().getName();
	String namePrefix = "generated";
	String namePostFix = TransformUtil.getWithUpperCaseFirstCharacter(fieldName);

	OOVariable declaration = factory.createOOVariable();
	declaration.setName(namePrefix + namePostFix);
	declaration.setInitializerExpression(getterCall);

	OOType type = referredVariable.getType();
	if (type == null || type.getClassType() == null) {
	    throw new UnexpectedException("The type of the referred field is incomplete!");
	}
	OOClass correspondingClass = TransformUtil.getClassByName(TransformationDataHolder.createdClasses,
		type.getClassType().getName());
	if (correspondingClass == null) {
	    OOType newType = factory.createOOType();
	    newType.setBaseType(OOBaseType.OBJECT);
	    declaration.setType(newType);
	} else {
	    OOVariable correspondingMember = TransformUtil.getVariableByNameFromMembers(correspondingClass.getMembers(),
		    fieldName);
	    if (correspondingMember != null) {
		declaration.setType(correspondingMember.getType());
	    } else {
		declaration.setType(TypeConverter.generateDefaultObjectType());
	    }
	}

	return declaration;
    }

    public OOFunctionCallExpression getSetMethodCallForFieldReference(IASTFieldReference fieldReference,
	    IASTExpression setArgument) {
	OOFunctionCallExpression setterCall = factory.createOOFunctionCallExpression();
	setterCall.setFunctionName("set" + TransformUtil
		.getWithUpperCaseFirstCharacter(fieldReference.getFieldName().resolveBinding().getName()));

	setterCall.getArgumentExpressions().add(
		TransformUtil.convertExpressionAndProcessPrecedingStatements(new ExpressionConverter(), setArgument));
	setOwnerExpression(setterCall, fieldReference);

	return setterCall;
    }
}
