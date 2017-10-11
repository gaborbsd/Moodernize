package hu.bme.aut.moodernize.c2j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.index.IIndex;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOPackage;
import hu.bme.aut.oogen.OogenFactory;
import hu.bme.aut.oogen.java.OOCodeGeneratorTemplatesJava;

public class TransformC2J {
	//private static Pattern PATH_FILE_EXT_PATTERN = Pattern.compile("(.*)(/|\\\\)+(.*).c");
	private static OogenFactory factory = OogenFactory.eINSTANCE;

	public static Map<String, String> transform(IIndex index, Set<IASTTranslationUnit> sources) {
		// TODO: "Generált import hu.bme.aut.protokit.runtime.ProtoUtil;"
		// TODO: Cleanup
		Map<String, String> classes = new HashMap<>();
		OOModel model = factory.createOOModel();

		for (IASTTranslationUnit ast : sources) {
			CDT2OOgenTransform visitor = new CDT2OOgenTransform(ast.getContainingFilename(), model);
			ast.accept(visitor);
		}
		List<OOClass> structs = CDT2OOgenTransform.getStructs();
		Map<String, ArrayList<String>> functionCallHierarchy = CDT2OOgenTransform.getFunctionCallHierarchy();
		
		for (String caller : functionCallHierarchy.keySet()) {
			System.out.println("Caller: " + caller);
			for (String called : functionCallHierarchy.get(caller)) {
				System.out.println("	Called: " + called);
			}
		}
		
		MethodAnalyzer.analyze(structs, model.getGlobalFunctions());
		
		RewriteOOGen.rewrite(model, structs);
		
		OOCodeGeneratorTemplatesJava template = OOCodeGeneratorTemplatesJava.getInstance();
		for (OOPackage pkg : model.getPackages()) {
			for (OOClass cl : pkg.getClasses()) {
				classes.put(pkg.getName() + "." + cl.getName(), template.generate(cl));
			}
		}

		return classes;
	}
}