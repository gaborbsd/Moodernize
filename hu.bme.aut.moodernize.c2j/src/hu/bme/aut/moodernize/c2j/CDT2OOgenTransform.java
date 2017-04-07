package hu.bme.aut.moodernize.c2j;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNameOwner;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IFunction;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.IValue;
import org.eclipse.cdt.core.dom.ast.IVariable;

import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVisibility;
import hu.bme.aut.oogen.OogenFactory;

public class CDT2OOgenTransform extends ASTVisitor {
	private static OogenFactory factory = OogenFactory.eINSTANCE;
	private String fileName;
	private OOModel model;

	public CDT2OOgenTransform(String fn) {
		this(fn, factory.createOOModel());
	}

	public CDT2OOgenTransform(String fileName, OOModel model) {
		this.fileName = fileName;
		this.model = model;
		shouldVisitNames = true;
		shouldVisitImplicitNames = true;
	}



	public int visit(IASTName name) {

		if (name.getContainingFilename() != fileName) {
			return PROCESS_SKIP;
		}

		IBinding binding = name.resolveBinding();
		if (binding instanceof IFunction && name.getRoleOfName(true) == IASTNameOwner.r_definition) {
			IFunction function = (IFunction) binding;
			boolean isStatic = function.isStatic();
			IType returnType = function.getType().getReturnType();
			
			OOMethod func = factory.createOOMethod();
			func.setName(function.getName());
			func.setStatic(function.isStatic());
			func.setVisibility(OOVisibility.PUBLIC);
			// TODO: type

			for (IParameter p : function.getParameters()) {
				OOVariable param = factory.createOOVariable();
				param.setName(p.getName());
				// TODO: type
				OOType paramType = factory.createOOType();
				paramType.setBaseType(OOBaseType.INT);
				param.setType(paramType);
				func.getParameters().add(param);
			}

			model.getGlobalFunctions().add(func);
			
			return PROCESS_CONTINUE;
		} else if (binding instanceof IVariable && name.getRoleOfName(true) != IASTNameOwner.r_reference) {
			IVariable variable = (IVariable) binding;
			
			if (variable.getOwner() == null) {
				String variableName = variable.getName();
				IType variableType = variable.getType();
				IValue variableValue = variable.getInitialValue();
				
				OOVariable var = factory.createOOVariable();
				var.setName(variable.getName());
				// TODO: type
				OOType paramType = factory.createOOType();
				paramType.setBaseType(OOBaseType.INT);
				var.setType(paramType);
				// TODO: init exp

				model.getGlobalVariables().add(var);
			}
			return PROCESS_CONTINUE;
		}

		else {
			return PROCESS_SKIP;
		}
	}

	public OOModel getModel() {
		return model;
	}
}
