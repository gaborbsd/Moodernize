package hu.bme.aut.moodernize.c2j;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMember;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOPackage;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OOVisibility;
import hu.bme.aut.oogen.OogenFactory;

public class RewriteOOGen {
	private static OogenFactory factory = OogenFactory.eINSTANCE;
	
	public static void rewrite(OOModel model) {
		OOPackage pkg = factory.createOOPackage();
		pkg.setName("prog");
		
		model.getPackages().add(pkg);
		
		OOClass cl = factory.createOOClass();
		cl.setName("ModernizedCProgram");
		cl.setPackage(pkg);
		
		pkg.getClasses().add(cl);
		
		for (OOMethod f : model.getGlobalFunctions()) {
			//TODO: deep copy OOMethod
			OOMethod m = factory.createOOMethod();
			m.setName(f.getName());
			m.setVisibility(f.getVisibility());
			m.setStatic(f.isStatic());
			m.setReturnType(f.getReturnType());
			cl.getMethods().add(m);
		}
		model.getGlobalFunctions().clear();
		
		for (OOVariable v : model.getGlobalVariables()) {
			OOMember m = factory.createOOMember();
			m.setName(v.getName());
			m.setType(v.getType());
			m.setVisibility(OOVisibility.PRIVATE);
			cl.getMembers().add(m);
		}
		model.getGlobalVariables().clear();
	}
}
