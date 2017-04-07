package hu.bme.aut.moodernize.c2j;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNameOwner;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.IFunction;
import org.eclipse.cdt.core.dom.ast.IParameter;
import org.eclipse.cdt.core.dom.ast.IType;
import org.eclipse.cdt.core.dom.ast.IValue;
import org.eclipse.cdt.core.dom.ast.IVariable;

public class ASTNameVisitor extends ASTVisitor {
	private StringBuffer sb;
	private String fileName;

	public ASTNameVisitor(String fn) {
		super();
		fileName = fn;
		sb = new StringBuffer();

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
			String functionName = function.getName();
			boolean isStatic = function.isStatic();
			IType returnType = function.getType().getReturnType();
			IParameter[] parameters = function.getParameters();

			sb.append("Function name: " + functionName + "\n");
			sb.append("Is static: " + isStatic + "\n");
			sb.append("Return type: " + returnType + "\n");
			sb.append("Number of parameters: " + parameters.length + "\n");
			for (int i = 0; i < parameters.length; i++) {
				IParameter param = parameters[i];
				IType paramType = param.getType();
				String paramName = param.getName();

				sb.append((i + 1) + ". Parameter type: " + paramType + "\n");
				sb.append((i + 1) + ". Parameter name: " + paramName + "\n");
			}

			sb.append("\n");

			return PROCESS_CONTINUE;
		} else if (binding instanceof IVariable && name.getRoleOfName(true) != IASTNameOwner.r_reference) {
			IVariable variable = (IVariable) binding;
			if (variable.getOwner() == null) {
				String variableName = variable.getName();
				IType variableType = variable.getType();
				IValue variableValue = variable.getInitialValue();

				sb.append("Variable name: " + variableName + "\n");
				sb.append("Variable type: " + variableType + "\n");

				if (variableValue != null) {
					sb.append("Variable value: " + variableValue.numberValue().doubleValue() + "\n");
				}

				else {
					sb.append("Variable value: undefined" + "\n");
				}

				sb.append("\n");
			}
			return PROCESS_CONTINUE;
		}

		else {
			return PROCESS_SKIP;
		}
	}

	public String getData() {
		return sb.toString();
	}
}
