package hu.bme.aut.moodernize.c2j.m2m;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOVariable;

public class ParameterListAndReturnTypeAnalysisTest extends AbstractTransformationTest {
    @Test
    public void noStructType_shouldNotTransformToAClass() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("void someFunction(int a){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMethod> globalFunctions = getDefaultClass(model).getMethods();
	Assert.assertEquals(1, globalFunctions.size());
	OOMethod function = globalFunctions.get(0);
	Assert.assertEquals("someFunction", function.getName());
    }

    @Test
    public void singleStructTypeParameter_shouldTransformToParameterTypesClass() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("void someFunction(struct S1 s1){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	checkIfFunctionBelongsExactlyToClass(model, "someFunction", "S1");

	List<OOClass> classes = model.getPackages().get(0).getClasses();
	OOMethod transformedMethod = TransformUtil.getMethodFromClasses(classes, "someFunction");
	Assert.assertNotNull(transformedMethod);
	Assert.assertNull(transformedMethod.getReturnType());
	Assert.assertEquals(0, transformedMethod.getParameters().size());
    }

    @Test
    public void singleStructTypeParameterMultipleOccurences_shouldRemoveOnlyTheFirstParameter() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("void someFunction(struct S1 s11, struct S1 s12){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	checkIfFunctionBelongsExactlyToClass(model, "someFunction", "S1");

	List<OOClass> classes = model.getPackages().get(0).getClasses();
	OOMethod transformedMethod = TransformUtil.getMethodFromClasses(classes, "someFunction");
	Assert.assertNotNull(transformedMethod);
	Assert.assertNull(transformedMethod.getReturnType());
	Assert.assertEquals(1, transformedMethod.getParameters().size());
	Assert.assertEquals("S1", transformedMethod.getParameters().get(0).getType().getClassType().getName());
	Assert.assertEquals("s12", transformedMethod.getParameters().get(0).getName());
    }

    @Test
    public void singleStructTypeAndPrimitiveParameters_shouldTransformToParameterTypesClass() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("void someFunction(struct S1 s1, int a, int b){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	checkIfFunctionBelongsExactlyToClass(model, "someFunction", "S1");

	List<OOClass> classes = model.getPackages().get(0).getClasses();
	OOMethod transformedMethod = TransformUtil.getMethodFromClasses(classes, "someFunction");
	Assert.assertNotNull(transformedMethod);
	Assert.assertNull(transformedMethod.getReturnType());
	Assert.assertEquals(2, transformedMethod.getParameters().size());
	for (OOVariable parameter : transformedMethod.getParameters()) {
	    Assert.assertNull(parameter.getType().getClassType());
	}
    }

    @Test
    public void multipleStructTypeParameters_shouldNotTransformToAClass() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("struct S2 {char c = 'X';}\n");
	sourceCode.append("void someFunction(struct S1 s1, struct S2 s2){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	checkIfFunctionBelongsExactlyToClass(model, "someFunction", "ModernizedCProgram");

	List<OOClass> classes = model.getPackages().get(0).getClasses();
	OOMethod transformedMethod = TransformUtil.getMethodFromClasses(classes, "someFunction");
	Assert.assertNotNull(transformedMethod);
	Assert.assertNull(transformedMethod.getReturnType());
	Assert.assertEquals(2, transformedMethod.getParameters().size());
	for (OOVariable parameter : transformedMethod.getParameters()) {
	    String name = parameter.getName();
	    if (name.equals("s1")) {
		Assert.assertEquals("S1", parameter.getType().getClassType().getName());
	    } else if (name.equals("s2")) {
		Assert.assertEquals("S2", parameter.getType().getClassType().getName());
	    } else {
		Assert.fail("Unexpected parameter name encountered: " + name);
	    }
	}
    }

    @Test
    public void multipleStructTypeAndPrimitiveParameters_shouldNotTransformToAClass() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("struct S2 {char c = 'X';}\n");
	sourceCode.append("void someFunction(struct S1 s1, struct S2 s2, int a, int b, char c){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	checkIfFunctionBelongsExactlyToClass(model, "someFunction", "ModernizedCProgram");

	List<OOClass> classes = model.getPackages().get(0).getClasses();
	OOMethod transformedMethod = TransformUtil.getMethodFromClasses(classes, "someFunction");
	Assert.assertNotNull(transformedMethod);
	Assert.assertNull(transformedMethod.getReturnType());
	Assert.assertEquals(5, transformedMethod.getParameters().size());
	for (OOVariable parameter : transformedMethod.getParameters()) {
	    String name = parameter.getName();
	    if (name.equals("s1")) {
		Assert.assertEquals("S1", parameter.getType().getClassType().getName());
	    } else if (name.equals("s2")) {
		Assert.assertEquals("S2", parameter.getType().getClassType().getName());
	    } else {
		Assert.assertNull(parameter.getType().getClassType());
		;
	    }
	}
    }

    @Test
    public void structReturnTypeNoStructParameter_shouldTransformToReturnTypesClass() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("struct S1 someFunction(int a, int b){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	checkIfFunctionBelongsExactlyToClass(model, "someFunction", "S1");

	List<OOClass> classes = model.getPackages().get(0).getClasses();
	OOMethod transformedMethod = TransformUtil.getMethodFromClasses(classes, "someFunction");
	Assert.assertNotNull(transformedMethod);
	Assert.assertEquals(2, transformedMethod.getParameters().size());
	for (OOVariable parameter : transformedMethod.getParameters()) {
	    Assert.assertNull(parameter.getType().getClassType());
	}

	Assert.assertEquals("S1", transformedMethod.getReturnType().getClassType().getName());
    }

    @Test
    public void structReturnTypeSingleIdenticalStructParameter_shouldTransformToReturnTypesClass() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("struct S1 someFunction(struct S1 s1){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	checkIfFunctionBelongsExactlyToClass(model, "someFunction", "S1");

	List<OOClass> classes = model.getPackages().get(0).getClasses();
	OOMethod transformedMethod = TransformUtil.getMethodFromClasses(classes, "someFunction");
	Assert.assertNotNull(transformedMethod);
	Assert.assertEquals(0, transformedMethod.getParameters().size());
	Assert.assertEquals("S1", transformedMethod.getReturnType().getClassType().getName());
    }

    @Test
    public void structReturnTypeSingleDifferentStructParameter_shouldTransformToReturnTypesClass() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("struct S2 {char c}\n");
	sourceCode.append("struct S1 someFunction(struct S2 s2){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	checkIfFunctionBelongsExactlyToClass(model, "someFunction", "S1");

	List<OOClass> classes = model.getPackages().get(0).getClasses();
	OOMethod transformedMethod = TransformUtil.getMethodFromClasses(classes, "someFunction");
	Assert.assertNotNull(transformedMethod);
	Assert.assertEquals("S1", transformedMethod.getReturnType().getClassType().getName());
	Assert.assertEquals(1, transformedMethod.getParameters().size());
	Assert.assertEquals("S2", transformedMethod.getParameters().get(0).getType().getClassType().getName());
    }

    @Test
    public void structReturnTypeMultipleStructParametersContainingReturnType_shouldTransformToReturnTypesClass() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("struct S2 {char c;}\n");
	sourceCode.append("struct S1 someFunction(struct S1 s1, struct S2 s2){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	checkIfFunctionBelongsExactlyToClass(model, "someFunction", "S1");

	List<OOClass> classes = model.getPackages().get(0).getClasses();
	OOMethod transformedMethod = TransformUtil.getMethodFromClasses(classes, "someFunction");
	Assert.assertNotNull(transformedMethod);
	Assert.assertEquals("S1", transformedMethod.getReturnType().getClassType().getName());
	Assert.assertEquals(1, transformedMethod.getParameters().size());
	Assert.assertEquals("S2", transformedMethod.getParameters().get(0).getType().getClassType().getName());
    }

    @Test
    public void structReturnTypeMultipleStructParametersContainingReturnTypeWithMultipleOccurences_shouldTransformToReturnTypesClass() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("struct S2 {char c;}\n");
	sourceCode.append("struct S1 someFunction(struct S1 s11, struct S2 s2, struct S1 s12, struct S1 s13){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	checkIfFunctionBelongsExactlyToClass(model, "someFunction", "S1");

	List<OOClass> classes = model.getPackages().get(0).getClasses();
	OOMethod transformedMethod = TransformUtil.getMethodFromClasses(classes, "someFunction");
	Assert.assertNotNull(transformedMethod);
	Assert.assertEquals("S1", transformedMethod.getReturnType().getClassType().getName());
	Assert.assertEquals(3, transformedMethod.getParameters().size());
	Assert.assertEquals("S2", transformedMethod.getParameters().get(0).getType().getClassType().getName());
	Assert.assertEquals("S1", transformedMethod.getParameters().get(1).getType().getClassType().getName());
	Assert.assertEquals("s12", transformedMethod.getParameters().get(1).getName());
	Assert.assertEquals("S1", transformedMethod.getParameters().get(2).getType().getClassType().getName());
	Assert.assertEquals("s13", transformedMethod.getParameters().get(2).getName());
    }

    @Test
    public void structReturnTypeMultipleStructParametersNotContainingReturnType_shouldTransformToReturnTypesClass() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("struct S1 {int a, b;}\n");
	sourceCode.append("struct S2 {char c;}\n");
	sourceCode.append("struct S3 {int x;}\n");
	sourceCode.append("struct S1 someFunction(struct S2 s2, struct S3 s3){}");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	checkIfFunctionBelongsExactlyToClass(model, "someFunction", "S1");

	List<OOClass> classes = model.getPackages().get(0).getClasses();
	OOMethod transformedMethod = TransformUtil.getMethodFromClasses(classes, "someFunction");
	Assert.assertNotNull(transformedMethod);
	Assert.assertEquals("S1", transformedMethod.getReturnType().getClassType().getName());
	Assert.assertEquals(2, transformedMethod.getParameters().size());
	Assert.assertEquals("S2", transformedMethod.getParameters().get(0).getType().getClassType().getName());
	Assert.assertEquals("S3", transformedMethod.getParameters().get(1).getType().getClassType().getName());
    }
    
    private void checkIfFunctionBelongsExactlyToClass(OOModel model, String functionName, String className) {
	for (OOClass cl : model.getPackages().get(0).getClasses()) {
	    if (cl.getName().equals(className)) {
		Assert.assertTrue(TransformUtil.functionBelongsToClass(functionName, cl));
	    } else {
		Assert.assertFalse(TransformUtil.functionBelongsToClass(functionName, cl));
	    }
	}
    }
}
