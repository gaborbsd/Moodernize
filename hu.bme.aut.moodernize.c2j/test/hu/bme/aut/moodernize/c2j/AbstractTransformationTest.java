package hu.bme.aut.moodernize.c2j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;

import hu.bme.aut.moodernize.core.CToJavaTransformer;
import hu.bme.aut.moodernize.core.ICToJavaTransformer;
import hu.bme.aut.oogen.OOModel;

public abstract class AbstractTransformationTest {
	protected ICToJavaTransformer transformer;
	@Before
	public void setup() {
		transformer = new CToJavaTransformer();
	}
	
	protected OOModel getModelBySourceCode(String sourceCode) {
		try {
			IASTTranslationUnit ast = getIASTTranslationUnit(sourceCode.toString().toCharArray());
			return transformer.transform(new HashSet<IASTTranslationUnit>(Arrays.asList(ast)));
		} catch(CoreException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	private IASTTranslationUnit getIASTTranslationUnit(char[] code) throws CoreException {
		FileContent fc = FileContent.create("", code);
		Map<String, String> macroDefinitions = new HashMap<>();
		String[] includeSearchPaths = new String[0];
		IScannerInfo si = new ScannerInfo(macroDefinitions, includeSearchPaths);
		IncludeFileContentProvider ifcp = IncludeFileContentProvider.getEmptyFilesProvider();
		IIndex idx = null;
		int options = ILanguage.OPTION_IS_SOURCE_UNIT;
		IParserLogService log = new DefaultLogService();

		return GPPLanguage.getDefault().getASTTranslationUnit(fc, si, ifcp, idx, options, log);
	}
}
