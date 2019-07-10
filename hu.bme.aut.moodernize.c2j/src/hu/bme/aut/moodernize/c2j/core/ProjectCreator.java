package hu.bme.aut.moodernize.c2j.core;

import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOPackage;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVisibility;
import hu.bme.aut.oogen.OogenFactory;

public class ProjectCreator {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    private static String DEFAULTCLASSNAME = "ModernizedCProgram";
    
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
    }
    
    private void createAllClasses(OOPackage mainPackage, List<OOClass> createdClasses) {
	for (OOClass newClass : createdClasses) {
	    addClassToPackage(EcoreUtil.copy(newClass), mainPackage);
	}
    }
    
    private void addClassToPackage(OOClass newClass, OOPackage pack) {
	newClass.setPackage(pack);
	pack.getClasses().add(newClass);
    }
}
