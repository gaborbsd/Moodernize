package hu.bme.aut.moodernize.ui.popup.actions;

import java.io.File;

import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import hu.bme.aut.apitransform.ApiTransformStandaloneSetup;
import hu.bme.aut.apitransform.apiTransform.ApiTransformPackage;
import hu.bme.aut.apitransform.apiTransform.Model;

public class ApiTransformModelInterpreter {
    private static final String API_TRANSFORM_FILE_NAME = "transformations.apitransform";
    private ICProject cProject;
    
    public ApiTransformModelInterpreter(ICProject cProject) {
	this.cProject = cProject;
    }
    
    public Model getApiTransformationModel() {
	Model model = loadApiTransformationModel();
	return model;
    }
    
    private Model loadApiTransformationModel() {
	Injector injector = new ApiTransformStandaloneSetup().createInjector();
	ApiTransformPackage.eINSTANCE.eClass();
	XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
	resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
	
	java.net.URI uri = cProject.getLocationURI();
	String fullURI = uri.getScheme() + ":" + uri.getPath() + File.separator + API_TRANSFORM_FILE_NAME;
	try {
		Resource resource = resourceSet.getResource(URI.createURI(fullURI), true);
		return (Model) resource.getContents().get(0);
	} catch (Exception ex) {
	    return null;
	} 
    }
}
