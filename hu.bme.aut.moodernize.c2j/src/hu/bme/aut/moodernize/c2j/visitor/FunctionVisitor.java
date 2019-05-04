package hu.bme.aut.moodernize.c2j.visitor;

import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNameOwner;
import org.eclipse.cdt.core.dom.ast.IBasicType;
import org.eclipse.cdt.core.dom.ast.IBasicType.Kind;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IFunction;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IType;

import hu.bme.aut.moodernize.c2j.util.TypeConverter;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVisibility;

public class FunctionVisitor extends AbstractBaseVisitor {	
	private List<OOMethod> globalFunctions;
	
	public FunctionVisitor(String fileName, List<OOMethod> globalFunctions) {
		super(fileName);
		this.globalFunctions = globalFunctions;
		shouldVisitNames = true;
	}
	
	public int visit(IASTName name) {
		if (!isCorrectContainingFile(name)) {
			return PROCESS_SKIP;
		}
		
		IBinding binding = name.resolveBinding();
		if (binding instanceof IFunction && name.getRoleOfName(true) == IASTNameOwner.r_definition) {
			IFunction cdtFunction = (IFunction) binding;
			
			OOMethod ooFunction = factory.createOOMethod();
			ooFunction.setName(cdtFunction.getName());
			ooFunction.setStatic(cdtFunction.isStatic());
			ooFunction.setVisibility(OOVisibility.PUBLIC);

			IType returnType = cdtFunction.getType().getReturnType();
			if (returnType instanceof IBasicType && ((IBasicType) returnType).getKind() == Kind.eVoid) {
				ooFunction.setReturnType(null);
			} else {
				ooFunction.setReturnType(TypeConverter.convertCDTTypeToOOgenType(returnType));
			}

			for (IParameter p : cdtFunction.getParameters()) {
				OOVariable param = factory.createOOVariable();
				param.setName(p.getName());
				param.setType(TypeConverter.convertCDTTypeToOOgenType(p.getType()));
				ooFunction.getParameters().add(param);
			}
			globalFunctions.add(ooFunction);
			
			return PROCESS_CONTINUE;
		}

		return PROCESS_SKIP;
	}
}
