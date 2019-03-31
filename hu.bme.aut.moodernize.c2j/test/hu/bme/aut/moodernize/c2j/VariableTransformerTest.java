package hu.bme.aut.moodernize.c2j;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOModel;

public class VariableTransformerTest extends AbstractTransformationTest{
	@Test
	public void globalVariable_shouldTransformToGlobalVariable() throws CoreException {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("int globalInt = 2;");
		
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
	
	@Test
	public void intType_shouldTransformToInt() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("int a; short b; unsigned int c; unsigned short d;");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		List<OOMember> globalVariables = model.getPackages().get(0).getClasses().get(0).getMembers();
		for (OOMember m : globalVariables) {
			Assert.assertTrue(m.getType().getBaseType() == OOBaseType.INT);
		}
	}
	
	@Test
	public void longType_shouldTransformToLong() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("long a; unsigned long b;");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		List<OOMember> globalVariables = model.getPackages().get(0).getClasses().get(0).getMembers();
		for (OOMember m : globalVariables) {
			Assert.assertTrue(m.getType().getBaseType() == OOBaseType.LONG);
		}
	}
	
	@Test
	public void byteType_shouldTransformToByte() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("char a; unsigned char b; signed char c;");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		List<OOMember> globalVariables = model.getPackages().get(0).getClasses().get(0).getMembers();
		for (OOMember m : globalVariables) {
			Assert.assertTrue(m.getType().getBaseType() == OOBaseType.BYTE);
		}
	}
	
	@Test
	public void doubleType_shouldTransformToDouble() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("float a; double b; long double c;");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		List<OOMember> globalVariables = model.getPackages().get(0).getClasses().get(0).getMembers();
		for (OOMember m : globalVariables) {
			Assert.assertTrue(m.getType().getBaseType() == OOBaseType.DOUBLE);
		}
	}
	
	@Test
	public void voidPointer_shouldTransformToObject() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("void *a;");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		List<OOMember> globalVariables = model.getPackages().get(0).getClasses().get(0).getMembers();
		for (OOMember m : globalVariables) {
			Assert.assertTrue(m.getType().getBaseType() == OOBaseType.OBJECT);
		}
	}
	
	@Test
	public void array_shouldTransformToArray() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("float a[200]; int b[] = {0}; double *t[10];");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		List<OOMember> globalVariables = model.getPackages().get(0).getClasses().get(0).getMembers();
		for (OOMember m : globalVariables) {
			Assert.assertTrue(m.getType().isArray());
		}
	}
}
