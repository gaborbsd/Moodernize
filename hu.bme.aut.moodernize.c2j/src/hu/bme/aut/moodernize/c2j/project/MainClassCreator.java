package hu.bme.aut.moodernize.c2j.project;

import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVisibility;
import hu.bme.aut.oogen.OogenFactory;

public class MainClassCreator {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    public static String MAINCLASSNAME = "ModernizedCProgram";
    
    public OOClass createMainClass(List<OOVariable> globalVariables, List<OOMethod> globalFunctions) {
	OOClass mainClass = factory.createOOClass();
	mainClass.setName(MAINCLASSNAME);
	
	addGlobalVariablesToMainClass(globalVariables, mainClass);
	addGlobalFunctionsToMainClass(globalFunctions, mainClass);
	
	return mainClass;
    }
   
    private void addGlobalVariablesToMainClass(List<OOVariable> globalVariables, OOClass mainClass) {
	for (OOVariable globalVariable : globalVariables) {
	    OOMember globalVariableCopy = factory.createOOMember();
	    globalVariableCopy.setName(globalVariable.getName());
	    globalVariableCopy.setType(globalVariable.getType());
	    globalVariableCopy.setVisibility(OOVisibility.PRIVATE);
	    globalVariableCopy.setTransient(globalVariable.isTransient());
	    globalVariableCopy.setInitializerExpression(globalVariable.getInitializerExpression());
	    globalVariableCopy.setStatic(true);
	    mainClass.getMembers().add(globalVariableCopy);
	}
    }
    
    private void addGlobalFunctionsToMainClass(List<OOMethod> globalFunctions, OOClass mainClass) {
	for (OOMethod globalFunction : globalFunctions) {
	    mainClass.getMethods().add(EcoreUtil.copy(globalFunction));
	}
	globalFunctions.clear();
    }
}
