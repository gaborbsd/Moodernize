package hu.bme.aut.moodernize.c2j.core;

import java.util.List;

import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOReturn;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;
import hu.bme.aut.oogen.OOVisibility;
import hu.bme.aut.oogen.OogenFactory;

public class SupplementingMethodCreator {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public void createSupplementingMethods(List<OOClass> classes) {
	for (OOClass cl : classes) {
	    createConstructor(cl);
	    createGettersAndSetters(cl);
	}
    }

    private void createConstructor(OOClass cl) {

    }

    private void createGettersAndSetters(OOClass cl) {
	List<OOMethod> methods = cl.getMethods();
	for (OOMember member : cl.getMembers()) {
	    methods.add(createGetter(member));
	    methods.add(createSetter(member));
	}
    }

    private OOMethod createGetter(OOMember member) {
	OOMethod getter = factory.createOOMethod();
	getter.setName("get" + TransformUtil.capitalizeFirstCharacter(member.getName()));
	getter.setReturnType(member.getType());
	getter.setVisibility(OOVisibility.PUBLIC);
	getter.setStatic(member.isStatic());

	OOVariableReferenceExpression reference = factory.createOOVariableReferenceExpression();
	reference.setVariable(member);
	OOReturn returnStatement = factory.createOOReturn();
	returnStatement.setReturnedExpresssion(reference);
	getter.getStatements().add(returnStatement);

	return getter;
    }

    private OOMethod createSetter(OOMember member) {
	OOMethod setter = factory.createOOMethod();
	setter.setName("set" + TransformUtil.capitalizeFirstCharacter(member.getName()));
	setter.setReturnType(null);
	setter.setVisibility(OOVisibility.PUBLIC);
	setter.setStatic(member.isStatic());

	OOVariable parameter = factory.createOOVariable();
	parameter.setName("new" + TransformUtil.capitalizeFirstCharacter(member.getName()));
	parameter.setType(member.getType());
	setter.getParameters().add(parameter);

	OOVariableReferenceExpression memberReference = factory.createOOVariableReferenceExpression();
	memberReference.setVariable(member);

	OOVariableReferenceExpression parameterReference = factory.createOOVariableReferenceExpression();
	parameterReference.setVariable(parameter);

	OOAssignmentExpression assignment = factory.createOOAssignmentExpression();
	assignment.setLeftSide(memberReference);
	assignment.setRightSide(parameterReference);

	setter.getStatements().add(assignment);

	return setter;
    }
}
