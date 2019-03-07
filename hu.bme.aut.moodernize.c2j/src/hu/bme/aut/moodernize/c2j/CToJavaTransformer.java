package hu.bme.aut.moodernize.c2j;

import java.util.List;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOPackage;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVisibility;
import hu.bme.aut.oogen.OogenFactory;
import util.Callgraph;

public class CToJavaTransformer implements ICToJavaTransformer {
	private static OogenFactory factory = OogenFactory.eINSTANCE;

	public OOModel transform(Set<IASTTranslationUnit> sources) {
		OOModel model = factory.createOOModel();
		CDTToOOgenTransformer.resetDataStructures();
		
		for (IASTTranslationUnit ast : sources) {
			if (ast != null) {
				CDTToOOgenTransformer visitor = new CDTToOOgenTransformer(ast.getContainingFilename(), model);
				ast.accept(visitor);
			}
		}
		List<OOClass> structs = CDTToOOgenTransformer.getStructs();
		Callgraph callGraph = CDTToOOgenTransformer.getCallgraph();
		removeNonCustomFunctionsFromCG(callGraph, model.getGlobalFunctions());
		
		Set<OOClass> newClasses = MethodAnalyzer.analyze(structs, model.getGlobalFunctions(), callGraph);
		for (OOClass cl : newClasses) {
			structs.add(cl);
		}
		
		rewrite(model, structs);
		return model;
	}
	
	private static void rewrite(OOModel model, List<OOClass> structs) {
		OOPackage pkg = factory.createOOPackage();
		pkg.setName("prog");
		
		model.getPackages().add(pkg);
		
		OOClass cl = factory.createOOClass();
		cl.setName("ModernizedCProgram");
		cl.setPackage(pkg);
		
		pkg.getClasses().add(cl);
		
		for (OOMethod f : model.getGlobalFunctions()) {
			OOMethod m = (OOMethod)EcoreUtil.copy(f);
			cl.getMethods().add(m);
		}
		model.getGlobalFunctions().clear();
		
		for (OOVariable v : model.getGlobalVariables()) {
			OOMember m = factory.createOOMember();
			m.setName(v.getName());
			m.setType(v.getType());
			m.setVisibility(OOVisibility.PRIVATE);
			cl.getMembers().add(m);
		}
		model.getGlobalVariables().clear();
		
		for (OOClass s : structs) {
			OOClass struct = (OOClass)EcoreUtil.copy(s);
			struct.setPackage(pkg);
			pkg.getClasses().add(struct);
		}
	}
	
	private static void removeNonCustomFunctionsFromCG(Callgraph cg, List<OOMethod> globalFunctions) {
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
}