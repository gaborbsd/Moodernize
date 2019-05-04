package hu.bme.aut.moodernize.c2j.visitor;

import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IField;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IVariable;

import hu.bme.aut.moodernize.c2j.core.TransformationDataRepository;
import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOVariable;

public class GlobalVariableVisitor extends CdtBaseVisitor {
	public GlobalVariableVisitor(String fileName) {
		super(fileName);
		shouldVisitNames = true;
	}
	
	public int visit(IASTName name) {
		if (!isCorrectContainingFile(name)) {
			return PROCESS_SKIP;
		}
		
		IBinding binding = name.resolveBinding();
		if (binding instanceof IVariable && !(binding instanceof IParameter) && !(binding instanceof IField)) {
			IVariable variable = (IVariable) binding;
			if (variable.getOwner() == null) {
				OOVariable var = factory.createOOVariable();
				var.setName(variable.getName());
				var.setType(TypeConverter.convertCDTTypeToOOgenType(variable.getType()));

				TransformationDataRepository.addGlobalVariable(var);
			}

			return PROCESS_CONTINUE;
		}
		
		return PROCESS_SKIP;
	}
}
