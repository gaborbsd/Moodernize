package hu.bme.aut.moodernize.c2j.core;

import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import hu.bme.aut.apitransform.apiTransform.Model;
import hu.bme.aut.oogen.OOModel;

public interface ICToJavaTransformer {
    public OOModel transform(Set<IASTTranslationUnit> sources, Model model);
}
