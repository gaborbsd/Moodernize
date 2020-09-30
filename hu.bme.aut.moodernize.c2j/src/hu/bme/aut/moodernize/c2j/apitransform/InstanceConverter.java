package hu.bme.aut.moodernize.c2j.apitransform;

import java.util.ArrayList;
import java.util.List;

import hu.bme.aut.apitransform.apiTransform.Instance;
import hu.bme.aut.apitransform.apiTransform.Transformation;
import hu.bme.aut.oogen.OOBaseType;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OONewClass;
import hu.bme.aut.oogen.OOType;
import hu.bme.aut.oogen.OOVariable;
import hu.bme.aut.oogen.OogenFactory;

class InstanceConverter {
    private static OogenFactory factory = OogenFactory.eINSTANCE;
    
    public List<OOVariable> createInstances(Transformation transformation) {
	List<OOVariable> result = new ArrayList<OOVariable>();
	for (Instance instance : transformation.getInstances()) {
	    OOVariable instantiation = factory.createOOVariable();
	    instantiation.setName(instance.getName());
	    String fullClassName = getFullClassName(instance);
	    instantiation.setType(createTypeForInstance(fullClassName));
	    instantiation.setInitializerExpression(createConstructorCallForInstance(fullClassName, instance));
	    result.add(instantiation);
	}
	
	return result;
    }
    
    private OOType createTypeForInstance(String className) {
	OOType type = factory.createOOType();
	OOClass classType = factory.createOOClass();
	classType.setName(className);
	type.setBaseType(OOBaseType.OBJECT);
	type.setClassType(classType);
	
	return type;
    }
    
    private OONewClass createConstructorCallForInstance(String className, Instance instance) {
	OONewClass constructorCall = factory.createOONewClass();
	constructorCall.setClassName(className);
	constructorCall.getConstructorParameterExpressions().addAll(new ParameterListConverter().createParameters(instance.getParameters()));
	return constructorCall;
    }
    
    private String getFullClassName(Instance instance) {
	String result = "";
	for (String prefix : instance.getClassName().getPrefixes()) {
	    result += prefix + ".";
	}
	result += instance.getClassName().getName();
	return result;
    }
}
