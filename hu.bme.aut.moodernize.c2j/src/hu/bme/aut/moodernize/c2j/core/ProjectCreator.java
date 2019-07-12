package hu.bme.aut.moodernize.c2j.core;

import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOAssignmentExpression;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOPackage;
import hu.bme.aut.oogen.OOReturn;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVariableReferenceExpression;
import hu.bme.aut.oogen.OOVisibility;
import hu.bme.aut.oogen.OogenFactory;

public class ProjectCreator {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    public static String DEFAULTCLASSNAME = "ModernizedCProgram";
    
    public void createProjectHierarchy(OOModel model, List<OOClass> createdClasses) {
	OOPackage mainPackage = createPackageHierarchy(model);
	OOClass mainClass = createMainClass(mainPackage);
	createGlobalFunctions(model, mainClass);
	createGlobalVariables(model, mainClass);
	createAllClasses(mainPackage, createdClasses);
    }
    
    private OOPackage createPackageHierarchy(OOModel model) {
	OOPackage mainPackage = factory.createOOPackage();
	mainPackage.setName("prog");
	model.getPackages().add(mainPackage);
	
	return mainPackage;
    }
    
    private OOClass createMainClass(OOPackage mainPackage) {
	OOClass mainClass = factory.createOOClass();
	mainClass.setName(DEFAULTCLASSNAME);
	mainClass.setPackage(mainPackage);
	
	return mainClass;
    }
    
    private void createGlobalFunctions(OOModel model, OOClass mainClass) {
	for (OOMethod globalFunction : model.getGlobalFunctions()) {
	    mainClass.getMethods().add(EcoreUtil.copy(globalFunction));
	}
	model.getGlobalFunctions().clear();
    }
    
    private void createGlobalVariables(OOModel model, OOClass mainClass) {
	for (OOVariable globalVariable : model.getGlobalVariables()) {
	    OOMember globalVariableCopy = factory.createOOMember();
	    globalVariableCopy.setName(globalVariable.getName());
	    globalVariableCopy.setType(globalVariable.getType());
	    globalVariableCopy.setVisibility(OOVisibility.PRIVATE);
	    globalVariableCopy.setTransient(globalVariable.isTransient());
	    globalVariableCopy.setInitializerExpression(globalVariable.getInitializerExpression());
	    globalVariableCopy.setStatic(true);
	    mainClass.getMembers().add(globalVariableCopy);
	}
	model.getGlobalVariables().clear();
	createGettersAndSetters(mainClass);
    }
    
    private void createAllClasses(OOPackage mainPackage, List<OOClass> createdClasses) {
	for (OOClass newClass : createdClasses) {
	    OOClass classCopy = EcoreUtil.copy(newClass);
	    createGettersAndSetters(classCopy);
	    addClassToPackage(classCopy, mainPackage);
	}
    }
    
    private void addClassToPackage(OOClass newClass, OOPackage pack) {
	newClass.setPackage(pack);
	pack.getClasses().add(newClass);
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
