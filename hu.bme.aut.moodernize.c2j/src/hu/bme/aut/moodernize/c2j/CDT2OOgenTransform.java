package hu.bme.aut.moodernize.c2j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNameOwner;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IBasicType;
import org.eclipse.cdt.core.dom.ast.IBasicType.Kind;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.IField;
import org.eclipse.cdt.core.dom.ast.IFunction;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.IVariable;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVisibility;
import hu.bme.aut.oogen.OogenFactory;

public class CDT2OOgenTransform extends ASTVisitor {
	private static OogenFactory factory = OogenFactory.eINSTANCE;
	private String fileName;
	private OOModel model;
	private static List<OOClass> structs = new ArrayList<OOClass>();
	private static Map<String, ArrayList<String>> functionCallHierarchy = new HashMap<String, ArrayList<String>>();

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
		if (!isCorrectContainingFile(name)) {
			return PROCESS_SKIP;
		}

		IBinding binding = name.resolveBinding();
		// Function definition was found
		if (binding instanceof IFunction && name.getRoleOfName(true) == IASTNameOwner.r_definition) {
			IFunction function = (IFunction) binding;
			OOMethod func = factory.createOOMethod();
			func.setName(function.getName());
			func.setStatic(function.isStatic());
			func.setVisibility(OOVisibility.PUBLIC);

			IType returnType = function.getType().getReturnType();
			if (returnType instanceof IBasicType && ((IBasicType) returnType).getKind() == Kind.eVoid) {
				func.setReturnType(null);
			} else {
				func.setReturnType(TransformUtil.convertCDTTypeToOOgenType(returnType));
			}

			for (IParameter p : function.getParameters()) {
				OOVariable param = factory.createOOVariable();
				param.setName(p.getName());
				param.setType(TransformUtil.convertCDTTypeToOOgenType(p.getType()));
				func.getParameters().add(param);
			}

			model.getGlobalFunctions().add(func);

			return PROCESS_CONTINUE;
		}
		// A variable was found
		else if (binding instanceof IVariable && name.getRoleOfName(true) != IASTNameOwner.r_reference) {
			IVariable variable = (IVariable) binding;
			if (variable.getOwner() == null) {
				OOVariable var = factory.createOOVariable();
				var.setName(variable.getName());
				var.setType(TransformUtil.convertCDTTypeToOOgenType(variable.getType()));
				// TODO: init exp

				model.getGlobalVariables().add(var);
			}

			return PROCESS_CONTINUE;
		}

		// A struct was found
		else if (binding instanceof ICompositeType) {
			ICompositeType composite = (ICompositeType) binding;
			IField[] members = composite.getFields();

			OOClass cl = factory.createOOClass();
			cl.setName(composite.getName());

			for (IField var : members) {
				OOMember m = factory.createOOMember();
				m.setName(var.getName());
				m.setType(TransformUtil.convertCDTTypeToOOgenType(var.getType()));
				m.setVisibility(OOVisibility.PRIVATE);
				cl.getMembers().add(m);
			}
			if (!structs.contains(cl)) {
				structs.add(cl);
			}
			return PROCESS_CONTINUE;
		}

		return PROCESS_SKIP;
	}

	public static List<OOClass> getStructs() {
		return structs;
	}

	public static Map<String, ArrayList<String>> getFunctionCallHierarchy() {
		return functionCallHierarchy;
	}

	public OOModel getModel() {
		return model;
	}

	private boolean isCorrectContainingFile(IASTNode node) {
		if (node.getContainingFilename() != this.fileName) {
			return false;
		}
		return true;
	}
}
