package hu.bme.aut.moodernize.visitor;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IField;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IVariable;

import hu.bme.aut.moodernize.util.TransformUtil;
import hu.bme.aut.moodernize.util.TypeConverter;
import hu.bme.aut.oogen.OOVariable;

public class GlobalVariableVisitor extends AbstractBaseVisitor {
	private List<OOVariable> globalVariables;
	
	public GlobalVariableVisitor(String fileName, List<OOVariable> globalVariables) {
		super(fileName);
		this.globalVariables = globalVariables;
		shouldVisitNames = true;
	}
	
	@Override
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
				
				if (!TransformUtil.listContainsVariable(globalVariables, var)) {
					globalVariables.add(var);
				}
			}

			return PROCESS_SKIP;
		}
		
		return PROCESS_SKIP;
	}
}