package hu.bme.aut.moodernize.c2j;

import java.util.Map;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

public interface ICToJavaTransformer {
	public Map<String, String> transform(Set<IASTTranslationUnit> sources);
}
