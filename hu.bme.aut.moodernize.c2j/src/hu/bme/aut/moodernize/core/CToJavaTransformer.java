package hu.bme.aut.moodernize.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.moodernize.visitor.AbstractBaseVisitor;
import hu.bme.aut.moodernize.visitor.FunctionDeclarationVisitor;
import hu.bme.aut.moodernize.visitor.FunctionDefinitionVisitor;
import hu.bme.aut.moodernize.visitor.GlobalVariableVisitor;
import hu.bme.aut.moodernize.visitor.StructVisitor;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOPackage;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVisibility;
import hu.bme.aut.oogen.OogenFactory;

public class CToJavaTransformer implements ICToJavaTransformer {
	private static OogenFactory factory = OogenFactory.eINSTANCE;
	
	private OOModel model = factory.createOOModel();
	private List<OOClass> createdClasses = new ArrayList<OOClass>();
	
	@Override
	public OOModel transform(Set<IASTTranslationUnit> asts) {		
		traverseAsts(asts);
		assignFunctionsToClassesBySignature();
		createProjectHierarchy(model, createdClasses);
		
		return model;
	}
		
	private void traverseAsts(Set<IASTTranslationUnit> asts) {
		for (IASTTranslationUnit ast : asts) {
			if (ast != null) {
				acceptVisitors(ast, ast.getContainingFilename());
			}
		}
	}
	
	private void acceptVisitors(IASTTranslationUnit ast, String containingFilename) {
		List<AbstractBaseVisitor> visitors = new ArrayList<AbstractBaseVisitor>();
		visitors.add(new GlobalVariableVisitor(containingFilename, model.getGlobalVariables()));
		visitors.add(new StructVisitor(containingFilename, createdClasses));
		visitors.add(new FunctionDeclarationVisitor(containingFilename, model.getGlobalFunctions()));
		visitors.add(new FunctionDefinitionVisitor(containingFilename, model.getGlobalFunctions()));
		
		for (AbstractBaseVisitor visitor : visitors) {
			ast.accept(visitor);
		}
	}
	
	private void assignFunctionsToClassesBySignature() {
		FunctionToClassAssigner assigner = new FunctionToClassAssigner(createdClasses, model.getGlobalFunctions());
		assigner.assignFunctionsToClasses();
	}
	
	private void createProjectHierarchy(OOModel model, List<OOClass> createdClasses) {
		OOPackage mainPackage = factory.createOOPackage();
		mainPackage.setName("prog");
		model.getPackages().add(mainPackage);
		
		OOClass mainClass = factory.createOOClass();
		mainClass.setName("ModernizedCProgram");
		mainClass.setPackage(mainPackage);
		
		mainPackage.getClasses().add(mainClass);
		
		for (OOMethod globalFunction : model.getGlobalFunctions()) {
			OOMethod globalFunctionCopy = EcoreUtil.copy(globalFunction);
			mainClass.getMethods().add(globalFunctionCopy);
		}
		model.getGlobalFunctions().clear();
		
		for (OOVariable globalVariable : model.getGlobalVariables()) {
			OOMember globalVariableCopy = factory.createOOMember();
			globalVariableCopy.setName(globalVariable.getName());
			globalVariableCopy.setType(globalVariable.getType());
			globalVariableCopy.setVisibility(OOVisibility.PRIVATE);
			mainClass.getMembers().add(globalVariableCopy);
		}
		model.getGlobalVariables().clear();
		
		for (OOClass newClass : createdClasses) {
			OOClass newClassCopy = EcoreUtil.copy(newClass);
			newClassCopy.setPackage(mainPackage);
			mainPackage.getClasses().add(newClassCopy);
		}
	}
}