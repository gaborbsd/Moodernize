package hu.bme.aut.moodernize.c2j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.index.IIndex;

public class TransformC2J {
	private static Pattern PATH_FILE_EXT_PATTERN = Pattern.compile("(.*)(/|\\\\)+(.*).c");
	
	public static Map<String, String> transform(IIndex index, Set<IASTTranslationUnit> sources) {
		Map<String, String> classes = new HashMap<>();
		for (IASTTranslationUnit ast : sources) {
			String className = classNamePerFile(ast);
			String classDef = classDefPerFile(ast, className);
			classes.put("app." + className , classDef);
		}
		return classes;
	}
	
	private static String classDefPerFile(IASTTranslationUnit ast, String className) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("package app;\n\n");
		sb.append("public class " + className + "{\n\n");
		
		// TODO
		
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
