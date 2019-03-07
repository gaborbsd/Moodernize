package hu.bme.aut.moodernize.c2j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOPackage;
import hu.bme.aut.oogen.OogenFactory;
import hu.bme.aut.oogen.java.OOCodeGeneratorTemplatesJava;
import util.Callgraph;

public class CToJavaTransformer implements ICToJavaTransformer {
	private static OogenFactory factory = OogenFactory.eINSTANCE;

	public Map<String, String> transform(Set<IASTTranslationUnit> sources) {
		Map<String, String> classes = new HashMap<>();
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
		
		RewriteOOGen.rewrite(model, structs);
		
		OOCodeGeneratorTemplatesJava template = OOCodeGeneratorTemplatesJava.getInstance();
		for (OOPackage pkg : model.getPackages()) {
			for (OOClass cl : pkg.getClasses()) {
				classes.put(pkg.getName() + "." + cl.getName(), template.generate(cl));
			}
		}

		return classes;
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