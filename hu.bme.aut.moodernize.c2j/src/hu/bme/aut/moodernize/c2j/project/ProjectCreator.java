package hu.bme.aut.moodernize.c2j.project;

import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOPackage;
import hu.bme.aut.oogen.OogenFactory;

public class ProjectCreator {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    public void createProjectHierarchy(OOModel model, List<OOClass> createdClasses) {
	OOPackage mainPackage = createPackageHierarchy(model);
	createAllClasses(mainPackage, createdClasses);
    }

    private OOPackage createPackageHierarchy(OOModel model) {
	OOPackage mainPackage = factory.createOOPackage();
	mainPackage.setName("prog");
	model.getPackages().add(mainPackage);

	return mainPackage;
    }

    private void createAllClasses(OOPackage mainPackage, List<OOClass> createdClasses) {
	for (OOClass newClass : createdClasses) {
	    OOClass classCopy = EcoreUtil.copy(newClass);
	    addClassToPackage(classCopy, mainPackage);
	}
    }

    private void addClassToPackage(OOClass newClass, OOPackage pack) {
	newClass.setPackage(pack);
	pack.getClasses().add(newClass);
    }
}
