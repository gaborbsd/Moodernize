package hu.bme.aut.moodernize.c2j.transformation.structure;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOModel;

public class BaseTypeTransformationTest extends AbstractTransformationTest {
    @Test
    public void intType_shouldTransformToInt() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("int a; signed int b; unsigned int c; unsigned short d; unsigned short int e;");
	sourceCode.append("short f; short int g; signed short h; signed short int j;");
	sourceCode.append("signed k; unsigned l;");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMember> globalVariables = getDefaultClass(model).getMembers();
	for (OOMember m : globalVariables) {
	    Assert.assertTrue(m.getType().getBaseType() == OOBaseType.INT);
	}
    }

    @Test
    public void longType_shouldTransformToLong() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("long a; long int b; signed long c; signed long int d;");
	sourceCode.append("unsigned long e; long int f;");
	sourceCode.append("long long g; long long int h; signed long long j; signed long long int k;");
	sourceCode.append("unsigned long long l; unsigned long long int m;");
	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMember> globalVariables = getDefaultClass(model).getMembers();
	for (OOMember m : globalVariables) {
	    Assert.assertTrue(m.getType().getBaseType() == OOBaseType.LONG);
	}
    }

    @Test
    public void byteType_shouldTransformToByte() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("char a; unsigned char b; signed char c;");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMember> globalVariables = getDefaultClass(model).getMembers();
	for (OOMember m : globalVariables) {
	    Assert.assertTrue(m.getType().getBaseType() == OOBaseType.BYTE);
	}
    }

    @Test
    public void doubleType_shouldTransformToDouble() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("float a; double b; long double c;");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMember> globalVariables = getDefaultClass(model).getMembers();
	for (OOMember m : globalVariables) {
	    Assert.assertTrue(m.getType().getBaseType() == OOBaseType.DOUBLE);
	}
    }

    @Test
    public void voidPointer_shouldTransformToObject() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("void *a;");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMember> globalVariables = getDefaultClass(model).getMembers();
	for (OOMember m : globalVariables) {
	    Assert.assertTrue(m.getType().getBaseType() == OOBaseType.OBJECT);
	}
    }

    @Test
    public void array_shouldTransformToArray() {
	StringBuilder sourceCode = new StringBuilder();
	sourceCode.append("float a[200]; int b[] = {0}; double *t[10 * 3 + 1];");

	OOModel model = getModelBySourceCode(sourceCode.toString());

	List<OOMember> globalVariables = getDefaultClass(model).getMembers();
	for (OOMember m : globalVariables) {
	    Assert.assertEquals(1, (m.getType().getArrayDimensions()));
	}
    }
}
