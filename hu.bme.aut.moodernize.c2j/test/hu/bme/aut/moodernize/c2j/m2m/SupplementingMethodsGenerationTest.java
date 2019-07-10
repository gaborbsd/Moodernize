package hu.bme.aut.moodernize.c2j.m2m;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.moodernize.c2j.AbstractTransformationTest;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOModel;

public class SupplementingMethodsGenerationTest extends AbstractTransformationTest {
    
    @Test
    public void classMembers_shouldGenerateGettersAndSetters() {
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
}
