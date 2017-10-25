package hu.bme.aut.moodernize.c2j;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTExpressionStatement;
import org.eclipse.cdt.core.dom.ast.IASTFunctionCallExpression;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNameOwner;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
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
import util.Calledge;
import util.Callgraph;
import util.TransformUtil;
import util.TypeConverter;

public class CDT2OOgenTransform extends ASTVisitor {
	private static OogenFactory factory = OogenFactory.eINSTANCE;
	private String fileName;
	private OOModel model;
	private static List<OOClass> structs = new ArrayList<OOClass>();
	private static Callgraph callGraph = new Callgraph();

	public CDT2OOgenTransform(String fn) {
		this(fn, factory.createOOModel());
	}

	public CDT2OOgenTransform(String fileName, OOModel model) {
		this.fileName = fileName;
		this.model = model;
		shouldVisitNames = true;
		shouldVisitImplicitNames = true;
		shouldVisitDeclarations = true;
		shouldVisitExpressions = true;
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
				func.setReturnType(TypeConverter.convertCDTTypeToOOgenType(returnType));
			}

			for (IParameter p : function.getParameters()) {
				OOVariable param = factory.createOOVariable();
				param.setName(p.getName());
				param.setType(TypeConverter.convertCDTTypeToOOgenType(p.getType()));
				func.getParameters().add(param);
			}

			model.getGlobalFunctions().add(func);

			return PROCESS_CONTINUE;
		}
		// A global variable was found
		else if (binding instanceof IVariable && !(binding instanceof IParameter) && !(binding instanceof IField)) {
			IVariable variable = (IVariable) binding;
			if (variable.getOwner() == null) {
				OOVariable var = factory.createOOVariable();
				var.setName(variable.getName());
				var.setType(TypeConverter.convertCDTTypeToOOgenType(variable.getType()));
				
				if (!TransformUtil.listContainsOOVariable(model.getGlobalVariables(), var)) {
					model.getGlobalVariables().add(var);
				}
			}

			return PROCESS_CONTINUE;
		}

		// A struct was found
		else if (binding instanceof ICompositeType && ((ICompositeType) binding).getKey() == ICompositeType.k_struct) {
			ICompositeType composite = (ICompositeType) binding;
			IField[] members = composite.getFields();

			OOClass cl = factory.createOOClass();
			cl.setName(composite.getName());

			for (IField var : members) {
				OOMember m = factory.createOOMember();
				m.setName(var.getName());
				m.setType(TypeConverter.convertCDTTypeToOOgenType(var.getType()));
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

	public int visit(IASTDeclaration decl) {
		if (!isCorrectContainingFile(decl)) {
			return PROCESS_SKIP;
		}

		if (decl instanceof IASTFunctionDefinition) {
			IASTFunctionDefinition func = (IASTFunctionDefinition) decl;
			String callerName = func.getDeclarator().getName().resolveBinding().getName();

			IASTStatement[] statements = ((IASTCompoundStatement) func.getBody()).getStatements();
			for (IASTStatement statement : statements) {
				// TODO: Handle statements and expressions: all types, in their own funtions
				if (statement instanceof IASTExpressionStatement) {
					IASTExpression expression = ((IASTExpressionStatement) statement).getExpression();
					if (expression instanceof IASTFunctionCallExpression) {
						IASTFunctionCallExpression call = (IASTFunctionCallExpression) expression;
						IASTExpression exp = call.getFunctionNameExpression();
						if (exp != null && exp instanceof IASTIdExpression) {
							IASTIdExpression idExp = (IASTIdExpression) call.getFunctionNameExpression();
							String calledName = idExp.getName().resolveBinding().getName();
							addEdgeToCallgraph(callerName, calledName);
						}
					}
				}
			}
		}
		return PROCESS_CONTINUE;
	}

	public static List<OOClass> getStructs() {
		return structs;
	}

	public static Callgraph getCallgraph() {
		return callGraph;
	}

	public OOModel getModel() {
		return model;
	}
	
	private static void addEdgeToCallgraph(String callerName, String calledName) {
		callGraph.add(new Calledge(callerName, calledName));
	}
	
	public static void resetDataStructures() {
		structs.clear();
		callGraph.clear();
	}

	private boolean isCorrectContainingFile(IASTNode node) {
		if (node.getContainingFilename() != this.fileName) {
			return false;
		}
		return true;
	}
}
