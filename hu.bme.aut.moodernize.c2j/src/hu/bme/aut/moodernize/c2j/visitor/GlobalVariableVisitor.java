package hu.bme.aut.moodernize.c2j.visitor;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTDeclarationStatement;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IField;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IVariable;

import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOVariable;

public class GlobalVariableVisitor extends AbstractBaseVisitor {
    private List<OOVariable> globalVariables;

    public GlobalVariableVisitor(String fileName, List<OOVariable> globalVariables) {
	super(fileName);
	this.globalVariables = globalVariables;
	shouldVisitNames = true;
    }
    
    public int visit(IASTDeclarationStatement declarationStatement) {
	return PROCESS_SKIP;
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
		OOVariable globalVariable = factory.createOOVariable();
		globalVariable.setName(variable.getName());
		globalVariable.setType(TypeConverter.convertCDTTypeToOOgenType(variable.getType()));
		
		if (!TransformUtil.listContainsVariable(globalVariables, globalVariable)) {
		    globalVariables.add(globalVariable);
		}
	    }
	    return PROCESS_SKIP;
	}
	return PROCESS_SKIP;
    }
}