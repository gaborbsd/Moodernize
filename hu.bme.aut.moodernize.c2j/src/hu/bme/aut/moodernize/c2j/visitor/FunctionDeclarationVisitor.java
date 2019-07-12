package hu.bme.aut.moodernize.c2j.visitor;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IBasicType;
import org.eclipse.cdt.core.dom.ast.IBasicType.Kind;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IFunction;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IType;

import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVisibility;

public class FunctionDeclarationVisitor extends AbstractBaseVisitor {
    private List<OOMethod> globalFunctions;

    public FunctionDeclarationVisitor(String fileName, List<OOMethod> globalFunctions) {
	super(fileName);
	this.globalFunctions = globalFunctions;
	shouldVisitNames = true;
    }
   
    @Override
    public int visit(IASTName name) {
	if (!isCorrectContainingFile(name)) {
	    return PROCESS_SKIP;
	}

	
	IBinding binding = name.resolveBinding();
	if (binding instanceof IFunction) {
	    IFunction cdtFunction = (IFunction) binding;

	    OOMethod ooFunction = factory.createOOMethod();
	    ooFunction.setName(cdtFunction.getName());
	    handleVisibility(ooFunction, cdtFunction);

	    IType returnType = cdtFunction.getType().getReturnType();
	    if (returnType instanceof IBasicType && ((IBasicType) returnType).getKind() == Kind.eVoid) {
		ooFunction.setReturnType(null);
	    } else {
		ooFunction.setReturnType(TypeConverter.convertCDTTypeToOOgenType(returnType));
	    }

	    for (IParameter cdtParameter : cdtFunction.getParameters()) {
		OOVariable ooParameter = factory.createOOVariable();
		ooParameter.setName(cdtParameter.getName());
		ooParameter.setType(TypeConverter.convertCDTTypeToOOgenType(cdtParameter.getType()));
		ooFunction.getParameters().add(ooParameter);
	    }
	    if (!TransformUtil.listContainsMethod(globalFunctions, ooFunction)) {
		globalFunctions.add(ooFunction);
	    }
	    
	    return PROCESS_CONTINUE;
	}

	return PROCESS_SKIP;
    }
    
    private void handleVisibility(OOMethod ooFunction, IFunction cdtFunction) {
	if (cdtFunction.isStatic()) {
	    ooFunction.setVisibility(OOVisibility.PRIVATE);
	} else {
	    ooFunction.setVisibility(OOVisibility.PUBLIC);
	}
    }
}
