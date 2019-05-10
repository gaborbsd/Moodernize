package hu.bme.aut.moodernize.c2j.core;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;

public class FunctionToClassAssignerTest extends AbstractTransformationTest {
	@Test
	public void noStructType_shouldNotTransformToAClass() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("struct S1 {int a, b;}\n");
		sourceCode.append("void someFunction(int a){}");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		List<OOMethod> globalFunctions = model.getPackages().get(0).getClasses().get(0).getMethods();
		Assert.assertEquals(1, globalFunctions.size());
		Assert.assertEquals("someFunction", globalFunctions.get(0).getName());
	}
	
	@Test
	public void singleStructTypeParameter_shouldTransformToParameterTypesClass() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("struct S1 {int a, b;}\n");
		sourceCode.append("void someFunction(struct S1 s1){}");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		checkIfGivenFunctionBelongsOnlyToGivenClass(model, "someFunction", "S1");
	}
	
	@Test
	public void singleStructTypeAndPrimitiveParameters_shouldTransformToParameterTypesClass() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("struct S1 {int a, b;}\n");
		sourceCode.append("void someFunction(struct S1 s1, int a, int b){}");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		checkIfGivenFunctionBelongsOnlyToGivenClass(model, "someFunction", "S1");
	}
	
	@Test
	public void multipleStructTypeParameters_shouldNotTransformToAClass() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("struct S1 {int a, b;}\n");
		sourceCode.append("struct S2 {char c = 'X';}\n");
		sourceCode.append("void someFunction(struct S1 s1, struct S2 s2){}");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		checkIfGivenFunctionBelongsOnlyToGivenClass(model, "someFunction", "ModernizedCProgram");
	}
	
	@Test
	public void multipleStructTypeAndPrimitiveParameters_shouldNotTransformToAClass() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("struct S1 {int a, b;}\n");
		sourceCode.append("struct S2 {char c = 'X';}\n");
		sourceCode.append("void someFunction(struct S1 s1, struct S2 s2, int a, int b, char c){}");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		checkIfGivenFunctionBelongsOnlyToGivenClass(model, "someFunction", "ModernizedCProgram");
	}
	
	@Test
	public void structReturnTypeNoStructParameter_shouldTransformToReturnTypesClass() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("struct S1 {int a, b;}\n");
		sourceCode.append("struct S1 someFunction(int a, int b){}");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		checkIfGivenFunctionBelongsOnlyToGivenClass(model, "someFunction", "S1");
	}
	
	@Test
	public void structReturnTypeSingleIdenticalStructParameter_shouldTransformToReturnTypesClass() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("struct S1 {int a, b;}\n");
		sourceCode.append("struct S1 someFunction(struct S1 s1){}");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		checkIfGivenFunctionBelongsOnlyToGivenClass(model, "someFunction", "S1");
	}
	
	@Test
	public void structReturnTypeSingleDifferentStructParameter_shouldTransformToReturnTypesClass() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("struct S1 {int a, b;}\n");
		sourceCode.append("struct S2 {char c}\n");
		sourceCode.append("struct S1 someFunction(struct S2 s2){}");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		checkIfGivenFunctionBelongsOnlyToGivenClass(model, "someFunction", "S1");
	}
	
	@Test
	public void structReturnTypeMultipleStructParametersContainingReturnType_shouldTransformToReturnTypesClass() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("struct S1 {int a, b;}\n");
		sourceCode.append("struct S2 {char c;}\n");
		sourceCode.append("struct S1 someFunction(struct S1 s1, struct S2 s2){}");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		checkIfGivenFunctionBelongsOnlyToGivenClass(model, "someFunction", "S1");
	}
	
	@Test
	public void structReturnTypeMultipleStructParametersNotContainingReturnType_shouldTransformToReturnTypesClass() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("struct S1 {int a, b;}\n");
		sourceCode.append("struct S2 {char c;}\n");
		sourceCode.append("struct S3 {int x;}\n");
		sourceCode.append("struct S1 someFunction(struct S2 s2, struct S3 s3){}");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		checkIfGivenFunctionBelongsOnlyToGivenClass(model, "someFunction", "S1");
	}
	
	private void checkIfGivenFunctionBelongsOnlyToGivenClass(OOModel model, String functionName, String className) {
		for (OOClass cl : model.getPackages().get(0).getClasses()) {
			List<OOMethod> methods = cl.getMethods();
			if (cl.getName().equals(className)) {
				Assert.assertEquals(1, methods.size());
				Assert.assertEquals(functionName, methods.get(0).getName());
			} else {
				Assert.assertEquals(0, methods.size());
			}
		}
	}
}
