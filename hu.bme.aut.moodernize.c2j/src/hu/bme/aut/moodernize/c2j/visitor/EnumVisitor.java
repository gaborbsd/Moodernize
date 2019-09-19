package hu.bme.aut.moodernize.c2j.visitor;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNameOwner;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IEnumeration;
import org.eclipse.cdt.core.dom.ast.IEnumerator;

import hu.bme.aut.moodernize.c2j.commentmapping.CommentProcessor;
import hu.bme.aut.moodernize.c2j.dataholders.CommentMappingDataHolder;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOEnumeration;

public class EnumVisitor extends AbstractBaseVisitor {
    private List<OOEnumeration> enums;

    public EnumVisitor(String fileName, List<OOEnumeration> enums) {
	super(fileName);
	this.enums = enums;
	shouldVisitNames = true;
    }

    public int visit(IASTName name) {
	if (!isCorrectContainingFile(name)) {
	    return PROCESS_SKIP;
	}
	
	if (name.getRoleOfName(true) != IASTNameOwner.r_definition) {
	    return PROCESS_SKIP;
	}
	
	IBinding binding = name.resolveBinding();
	if (binding instanceof IEnumeration) {
	    String enumName = binding.getName();
	    if (!TransformUtil.isCorrectClassName(enumName)) {
		return PROCESS_SKIP;
	    }
	    IEnumeration enumeration = (IEnumeration) binding;

	    OOEnumeration ooEnum = factory.createOOEnumeration();
	    ooEnum.setName(enumName);
	    for (IEnumerator enumerator : enumeration.getEnumerators()) {
		ooEnum.getOptions().add(enumerator.getName());
	    }

	    CommentProcessor.processOwnedComments(ooEnum, CommentMappingDataHolder.findAllOwnedComments(enumeration));

	    if (!TransformUtil.listContainsEnum(enums, ooEnum)) {
		enums.add(ooEnum);
	    }

	    return PROCESS_CONTINUE;
	}

	return PROCESS_SKIP;
    }
}
