package hu.bme.aut.moodernize.c2j;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOModel;

public class StructTransformerTest extends AbstractTransformationTest {
	@Test
	public void struct_shouldTransformToClassWithCorrespondingMembers() {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("typedef struct S1 {int i, j = 0;}someStruct;");
		sourceCode.append("struct S2 {char *c;}\n");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		List<OOClass> classes = model.getPackages().get(0).getClasses();
		classes.removeIf(cl -> cl.getName().equals("ModernizedCProgram"));
		Assert.assertEquals(2, classes.size());
		for (int i = 0; i < classes.size(); i++) {
			OOClass cl = classes.get(i);
			List<OOMember> members = cl.getMembers();
			if (i == 0) {
				Assert.assertEquals("S1", cl.getName());
				Assert.assertEquals(2, members.size());
			} else {
				Assert.assertEquals("S2", cl.getName());
				Assert.assertEquals(1, members.size());
			}
		}
	}
	
	@Test
	public void structType_shouldTransformToReferenceType( ) {
		StringBuilder sourceCode = new StringBuilder();
		sourceCode.append("typedef struct S1 {int i, j = 0;}S1Ref;");
		sourceCode.append("struct S2 {char *c;};");
		sourceCode.append("S1Ref s1;");
		sourceCode.append("struct S2 s2;");
		
		OOModel model = getModelBySourceCode(sourceCode.toString());
		
		List<OOMember> globalVariables = model.getPackages().get(0).getClasses().get(0).getMembers();
		for (int i = 0; i < globalVariables.size(); i++) {
			OOMember m = globalVariables.get(i);
			String typeName = m.getType().getClassType().getName();
			if (i == 0) {
				Assert.assertTrue(typeName.equals("S1"));
			} else if (i == 1){
				Assert.assertTrue(typeName.equals("S2"));
			}
		}
	}
}
