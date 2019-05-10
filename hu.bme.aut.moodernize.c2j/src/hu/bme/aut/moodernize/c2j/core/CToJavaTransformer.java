package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.moodernize.c2j.visitor.AbstractBaseVisitor;
import hu.bme.aut.moodernize.c2j.visitor.FunctionBodyVisitor;
import hu.bme.aut.moodernize.c2j.visitor.FunctionVisitor;
import hu.bme.aut.moodernize.c2j.visitor.GlobalVariableVisitor;
import hu.bme.aut.moodernize.c2j.visitor.StructVisitor;
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
	private List<OOClass> classes = new ArrayList<OOClass>();
	//private Callgraph callGraph = new Callgraph();
	
	@Override
	public OOModel transform(Set<IASTTranslationUnit> asts) {		
		traverseAsts(asts);
		assignFunctionsToClassesBySignature();
		//analyzeCallchains();
		createClasses(model, classes);
		
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
		visitors.add(new StructVisitor(containingFilename, classes));
		visitors.add(new FunctionVisitor(containingFilename, model.getGlobalFunctions()));
		visitors.add(new FunctionBodyVisitor(containingFilename, model.getGlobalFunctions()));
		
		for (AbstractBaseVisitor visitor : visitors) {
			ast.accept(visitor);
		}
	}
	
	private void assignFunctionsToClassesBySignature() {
		FunctionToClassAssigner assigner = new FunctionToClassAssigner(classes, model.getGlobalFunctions());
		assigner.assignFunctionsToClasses();
	}
	
	/*private void analyzeCallchains() {
		Callgraph callGraph = TransformationDataRepository.getCallGraph();
		removeNonCustomFunctionsFromCallgraph(callGraph, model.getGlobalFunctions());
		Set<OOClass> callChainClasses = CallChainAnalyzer.analyze(classes, model.getGlobalFunctions(), callGraph);
		for (OOClass callChainClass : callChainClasses) {
			classes.add(callChainClass);
		}
	}
	
	private void removeNonCustomFunctionsFromCallgraph(Callgraph cg, List<OOMethod> globalFunctions) {
		for (String node : cg.getDistinctNodes()) {
			boolean contains = false;
			for (OOMethod m : globalFunctions) {
				if (m.getName().equals(node)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				cg.removeNodeIfExists(node);
			}
		}
	}*/
	
	private void createClasses(OOModel model, List<OOClass> classes) {
		OOPackage mainPackage = factory.createOOPackage();
		mainPackage.setName("prog");
		model.getPackages().add(mainPackage);
		
		OOClass mainClass = factory.createOOClass();
		mainClass.setName("ModernizedCProgram");
		mainClass.setPackage(mainPackage);
		
		mainPackage.getClasses().add(mainClass);
		
		for (OOMethod globalFunction : model.getGlobalFunctions()) {
			OOMethod method = EcoreUtil.copy(globalFunction);
			mainClass.getMethods().add(method);
		}
		model.getGlobalFunctions().clear();
		
		for (OOVariable globalVariable : model.getGlobalVariables()) {
			OOMember member = factory.createOOMember();
			member.setName(globalVariable.getName());
			member.setType(globalVariable.getType());
			member.setVisibility(OOVisibility.PRIVATE);
			mainClass.getMembers().add(member);
		}
		model.getGlobalVariables().clear();
		
		for (OOClass s : classes) {
			OOClass struct = EcoreUtil.copy(s);
			struct.setPackage(mainPackage);
			mainPackage.getClasses().add(struct);
		}
	}
}