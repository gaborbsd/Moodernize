package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.moodernize.c2j.callchain.CallChainAnalyzer;
import hu.bme.aut.moodernize.c2j.callchain.Callgraph;
import hu.bme.aut.moodernize.c2j.visitor.CdtToOOgenTransformer;
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
	private List<OOClass> structs = new ArrayList<OOClass>();
	
	public OOModel transform(Set<IASTTranslationUnit> asts) {
		CdtToOOgenTransformer.resetCallgraph();
		
		traverseAsts(asts);
		analyzeCallchains();
		createClasses(model, structs);
		
		return model;
	}
		
	private void traverseAsts(Set<IASTTranslationUnit> asts) {
		for (IASTTranslationUnit ast : asts) {
			if (ast != null) {
				CdtToOOgenTransformer visitor = new CdtToOOgenTransformer(ast.getContainingFilename(), model);
				ast.accept(visitor);
				for (OOClass struct : visitor.getStructs()) {
					structs.add(struct);
					OOMethod m = factory.createOOMethod();
				}
			}
		}
	}
	
	private void analyzeCallchains() {
		Callgraph callGraph = CdtToOOgenTransformer.getCallgraph();
		removeNonCustomFunctionsFromCallgraph(callGraph, model.getGlobalFunctions());
		Set<OOClass> newClasses = CallChainAnalyzer.analyze(structs, model.getGlobalFunctions(), callGraph);
		for (OOClass cl : newClasses) {
			structs.add(cl);
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
	}
	
	private void createClasses(OOModel model, List<OOClass> structs) {
		OOPackage mainPackage = factory.createOOPackage();
		mainPackage.setName("prog");
		model.getPackages().add(mainPackage);
		
		OOClass mainClass = factory.createOOClass();
		mainClass.setName("ModernizedCProgram");
		mainClass.setPackage(mainPackage);
		
		mainPackage.getClasses().add(mainClass);
		
		for (OOMethod f : model.getGlobalFunctions()) {
			OOMethod m = (OOMethod)EcoreUtil.copy(f);
			mainClass.getMethods().add(m);
		}
		model.getGlobalFunctions().clear();
		
		for (OOVariable v : model.getGlobalVariables()) {
			OOMember m = factory.createOOMember();
			m.setName(v.getName());
			m.setType(v.getType());
			m.setVisibility(OOVisibility.PRIVATE);
			mainClass.getMembers().add(m);
		}
		model.getGlobalVariables().clear();
		
		for (OOClass s : structs) {
			OOClass struct = (OOClass)EcoreUtil.copy(s);
			struct.setPackage(mainPackage);
			mainPackage.getClasses().add(struct);
		}
	}
}