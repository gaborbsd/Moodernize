package hu.bme.aut.moodernize.c2j.visitor;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IField;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IVariable;

import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOVariable;

public class GlobalVariableVisitor extends CdtBaseVisitor {
	private Set<OOVariable> globalVariables = new HashSet<OOVariable>();
	
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

				globalVariables.add(var);
			}

			return PROCESS_CONTINUE;
		}
		
		return PROCESS_CONTINUE;
	}

	public Set<OOVariable> getGlobalVariables() {
		return globalVariables;
	}
}
