package hu.bme.aut.moodernize.c2j.project;

import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOEnumeration;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOPackage;
import hu.bme.aut.oogen.OogenFactory;

public class ProjectCreator {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public void createProjectHierarchy(OOModel model, List<OOClass> createdClasses, List<OOEnumeration> enums) {
	OOPackage mainPackage = createPackageHierarchy(model);
	createEnums(mainPackage, enums);
	createAllClasses(mainPackage, createdClasses);
    }

    private OOPackage createPackageHierarchy(OOModel model) {
	OOPackage mainPackage = factory.createOOPackage();
	mainPackage.setName("application");
	model.getPackages().add(mainPackage);

	return mainPackage;
    }
    
    private void createEnums(OOPackage mainPackage, List<OOEnumeration> enums) {
	for (OOEnumeration enumeration : enums) {
	    addClassToPackage(EcoreUtil.copy(enumeration), mainPackage);
	}
    }

    private void createAllClasses(OOPackage mainPackage, List<OOClass> createdClasses) {
	for (OOClass newClass : createdClasses) {
	    addClassToPackage(EcoreUtil.copy(newClass), mainPackage);
	}
    }

    private void addClassToPackage(OOClass newClass, OOPackage pack) {
	newClass.setPackage(pack);
	pack.getClasses().add(newClass);
    }
    
    private void addClassToPackage(OOEnumeration newEnum, OOPackage pack) {
	newEnum.setPackage(pack);
	pack.getEnums().add(newEnum);
    }
}
