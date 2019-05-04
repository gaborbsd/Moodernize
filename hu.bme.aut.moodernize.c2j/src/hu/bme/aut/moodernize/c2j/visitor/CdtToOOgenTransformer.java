package hu.bme.aut.moodernize.c2j.visitor;

public class CdtToOOgenTransformer {
	/*private String fileName;
	private List<OOClass> structs = new ArrayList<OOClass>();
	private static Callgraph callGraph = new Callgraph();

	public CdtToOOgenTransformer(String fileName) {
		this(fileName, factory.createOOModel());
	}

	public CdtToOOgenTransformer(String fileName, OOModel model) {
		this.fileName = fileName;

		shouldVisitNames = true;
		shouldVisitImplicitNames = true;
		shouldVisitDeclarations = true;
		shouldVisitExpressions = true;
	}

	public int visit(IASTNode node) {
		return PROCESS_SKIP;
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
			// TODO: What to do with incorrect class names? Replace all references or
			// ignore?
			if (!TransformUtil.isCorrectClassName(composite.getName())) {
				return PROCESS_SKIP;
			}
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
				if (statement instanceof IASTFunctionCallExpression) {
					IASTFunctionCallExpression call = (IASTFunctionCallExpression) ((IASTExpressionStatement) statement)
							.getExpression();
					;
					IASTExpression functionNameExpression = call.getFunctionNameExpression();
					if (functionNameExpression != null && functionNameExpression instanceof IASTIdExpression) {
						IASTIdExpression idExpression = (IASTIdExpression) functionNameExpression;
						String calledName = idExpression.getName().resolveBinding().getName();
						addEdgeToCallgraph(callerName, calledName);
					}
				}
			}
		}
		return PROCESS_CONTINUE;
	}

	public List<OOClass> getStructs() {
		return structs;
	}

	public static Callgraph getCallgraph() {
		return callGraph;
	}

	private static void addEdgeToCallgraph(String callerName, String calledName) {
		callGraph.add(new Calledge(callerName, calledName));
	}

	public static void resetCallgraph() {
		callGraph.clear();
	}*/
}
