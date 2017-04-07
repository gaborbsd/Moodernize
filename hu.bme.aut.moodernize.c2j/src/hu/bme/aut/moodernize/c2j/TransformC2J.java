package hu.bme.aut.moodernize.c2j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.index.IIndex;

import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OogenFactory;
import hu.bme.aut.oogen.general.GeneratedFile;
import hu.bme.aut.oogen.java.OOCodeGeneratorJava;
import hu.bme.aut.oogen.java.OOCodeGeneratorTemplatesJava;

public class TransformC2J {
	private static Pattern PATH_FILE_EXT_PATTERN = Pattern.compile("(.*)(/|\\\\)+(.*).c");
	private static OogenFactory factory = OogenFactory.eINSTANCE;
	
	public static Map<String, String> transform(IIndex index, Set<IASTTranslationUnit> sources) {
		Map<String, String> classes = new HashMap<>();
		OOModel model = factory.createOOModel();
		
		for (IASTTranslationUnit ast : sources) {
			CDT2OOgenTransform visitor = new CDT2OOgenTransform(ast.getContainingFilename(), model);
			ast.accept(visitor);
		}
		RewriteOOGen.rewrite(model);
		OOCodeGeneratorTemplatesJava template = OOCodeGeneratorTemplatesJava.getInstance();
		classes.put("prog.ModernizedCProgram" , template.generate(model.getPackages().get(0).getClasses().get(0)));
		return classes;
	}
	
	private static String classDefPerFile(IASTTranslationUnit ast, String className) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("package app;\n\n");
		sb.append("public class " + className + "{\n\n");
		
		sb.append("}");
		
		return sb.toString();
	}
	
	private static String classNamePerFile(IASTTranslationUnit ast) {
		Matcher matcher = PATH_FILE_EXT_PATTERN.matcher(ast.getContainingFilename());
		if (!matcher.matches())
			return null;
		return TransformUtil.capitalizeFirst(matcher.group(3));
	}
	
}