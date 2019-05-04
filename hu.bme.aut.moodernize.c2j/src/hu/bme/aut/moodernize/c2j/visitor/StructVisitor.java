package hu.bme.aut.moodernize.c2j.visitor;

import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.IField;

import hu.bme.aut.moodernize.c2j.core.TransformationDataRepository;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOVisibility;

public class StructVisitor extends AbstractBaseVisitor {
	public StructVisitor(String fileName) {
		super(fileName);
		shouldVisitNames = true;
	}

	public int visit(IASTName name) {
		if (!isCorrectContainingFile(name)) {
			return PROCESS_SKIP;
		}
		
		IBinding binding = name.resolveBinding();
		if (binding instanceof ICompositeType && ((ICompositeType) binding).getKey() == ICompositeType.k_struct) {
			ICompositeType composite = (ICompositeType) binding;
			// TODO: What to do with incorrect class names? Replace all references or ignore?
			if (!TransformUtil.isCorrectClassName(composite.getName())) {
				return PROCESS_SKIP;
			}

			OOClass newClass = factory.createOOClass();
			newClass.setName(composite.getName());
			
			IField[] members = composite.getFields();
			for (IField var : members) {
				OOMember member = factory.createOOMember();
				member.setName(var.getName());
				member.setType(TypeConverter.convertCDTTypeToOOgenType(var.getType()));
				member.setVisibility(OOVisibility.PRIVATE);
				newClass.getMembers().add(member);
			}
			
			TransformationDataRepository.addClass(newClass);
			return PROCESS_CONTINUE;
		}

		return PROCESS_SKIP;
	}
}
