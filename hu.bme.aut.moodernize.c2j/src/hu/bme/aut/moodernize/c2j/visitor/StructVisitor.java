package hu.bme.aut.moodernize.c2j.visitor;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNameOwner;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.IField;

import hu.bme.aut.moodernize.c2j.commentmapping.CommentProcessor;
import hu.bme.aut.moodernize.c2j.dataholders.CommentMappingDataHolder;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOVisibility;

public class StructVisitor extends AbstractBaseVisitor {
    private List<OOClass> classes;

    public StructVisitor(String fileName, List<OOClass> classes) {
	super(fileName);
	this.classes = classes;
	shouldVisitNames = true;
    }

    @Override
    public int visit(IASTName name) {
	if (!isCorrectContainingFile(name)) {
	    return PROCESS_SKIP;
	}

	if (name.getRoleOfName(true) != IASTNameOwner.r_definition) {
	    return PROCESS_SKIP;
	}

	IBinding binding = name.resolveBinding();
	if (binding instanceof ICompositeType && ((ICompositeType) binding).getKey() == ICompositeType.k_struct) {
	    ICompositeType struct = (ICompositeType) binding;

	    if (!TransformUtil.isCorrectClassName(struct.getName())) {
		return PROCESS_SKIP;
	    }

	    OOClass newClass = factory.createOOClass();
	    newClass.setName(struct.getName());

	    for (IField structMember : struct.getFields()) {
		OOMember classMember = factory.createOOMember();
		classMember.setName(structMember.getName());
		classMember.setType(TypeConverter.convertCDTTypeToOOgenType(structMember.getType()));
		classMember.setVisibility(OOVisibility.PRIVATE);

		CommentProcessor.processOwnedComments(classMember, CommentMappingDataHolder.findAllOwnedComments(structMember));

		newClass.getMembers().add(classMember);
	    }

	    CommentProcessor.processOwnedComments(newClass, CommentMappingDataHolder.findAllOwnedComments(struct));

	    if (!TransformUtil.listContainsClass(classes, newClass)) {
		classes.add(newClass);
	    }
	    return PROCESS_CONTINUE;
	}

	return PROCESS_SKIP;
    }
}
