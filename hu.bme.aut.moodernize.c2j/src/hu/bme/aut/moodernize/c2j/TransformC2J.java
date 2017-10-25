package hu.bme.aut.moodernize.c2j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.index.IIndex;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOPackage;
import hu.bme.aut.oogen.OogenFactory;
import hu.bme.aut.oogen.java.OOCodeGeneratorTemplatesJava;
import util.Callgraph;

public class TransformC2J {
	//private static Pattern PATH_FILE_EXT_PATTERN = Pattern.compile("(.*)(/|\\\\)+(.*).c");
	private static OogenFactory factory = OogenFactory.eINSTANCE;

	public static Map<String, String> transform(IIndex index, Set<IASTTranslationUnit> sources) {
		Map<String, String> classes = new HashMap<>();
		OOModel model = factory.createOOModel();
		CDT2OOgenTransform.resetDataStructures();
		
		for (IASTTranslationUnit ast : sources) {
			if (ast != null) {
				CDT2OOgenTransform visitor = new CDT2OOgenTransform(ast.getContainingFilename(), model);
				ast.accept(visitor);
			}
		}
		List<OOClass> structs = CDT2OOgenTransform.getStructs();
		Callgraph callGraph = CDT2OOgenTransform.getCallgraph();
		removeNonCustomFunctionsFromCFG(callGraph, model.getGlobalFunctions());
		
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
	
	private static void removeNonCustomFunctionsFromCFG(Callgraph CFG, List<OOMethod> globalFunctions) {
		for (String node : CFG.getDistinctNodes()) {
			boolean contains = false;
			for (OOMethod m : globalFunctions) {
				if (m.getName().equals(node)) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				CFG.removeNodeIfExists(node);
			}
		}
	}
}