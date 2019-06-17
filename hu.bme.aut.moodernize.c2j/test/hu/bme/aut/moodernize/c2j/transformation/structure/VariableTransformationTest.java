package hu.bme.aut.moodernize.c2j.transformation.structure;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOModel;

public class VariableTransformationTest extends AbstractTransformationTest{
	@Test
	public void globalVariable_shouldTransformToGlobalVariable() throws CoreException {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("int globalInt;");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		List<OOMember> globalVariables = model.getPackages().get(0).getClasses().get(0).getMembers();
		Assert.assertEquals(1, globalVariables.size());
		OOMember globalVariable = globalVariables.get(0);
		Assert.assertEquals("globalInt", globalVariable.getName());
	}
	
	@Test
	public void localVariable_shouldNotTransformToGlobalVariable() throws CoreException {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("int main(void) { int localInt; return 0;}");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		List<OOMember> globalVariables = model.getPackages().get(0).getClasses().get(0).getMembers();
		Assert.assertEquals(0, globalVariables.size());
	}
}
