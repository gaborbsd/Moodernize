package hu.bme.aut.moodernize.c2j.visitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IVariable;

import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OogenFactory;

public class VariableVisitor extends ASTVisitor {
	private static OogenFactory factory = OogenFactory.eINSTANCE;
	private List<OOVariable> collectedVariables = new ArrayList<OOVariable>();
	
	public VariableVisitor() {
		shouldVisitNames = true;
	}
	
	public int visit(IASTName name) {
		IBinding binding = name.resolveBinding();
		if (binding instanceof IVariable) {
			IVariable cdtVariable = (IVariable) binding;
			OOVariable oogenVariable = factory.createOOVariable();
			
			oogenVariable.setTransient(false);
			oogenVariable.setName(cdtVariable.getName());
			oogenVariable.setType(TypeConverter.convertCDTTypeToOOgenType(cdtVariable.getType()));
			collectedVariables.add(oogenVariable);
		}
		
		return PROCESS_CONTINUE;
	}

	public List<OOVariable> getCollectedVariables() {
		return collectedVariables;
	}
}
