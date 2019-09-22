package hu.bme.aut.moodernize.ui.popup.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.model.CModelException;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.ISourceRoot;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.progress.IProgressService;

import hu.bme.aut.moodernize.c2j.core.CToJavaTransformer;
import hu.bme.aut.moodernize.c2j.core.ICToJavaTransformer;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOEnumeration;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OOPackage;
import hu.bme.aut.oogen.java.OOCodeGeneratorTemplatesJava;

public class TransformCToJava implements IObjectActionDelegate {
    private static final Pattern PKG_CLASS_PARSE_PATTERN = Pattern.compile("(.*)\\.(.*)");

    private IProject project;
    private ICProject cProject;

    private IProgressService pgService;
    private Shell shell;
    private Display display;

    public TransformCToJava() {
	super();
    }

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	IWorkbench workbench = targetPart.getSite().getWorkbenchWindow().getWorkbench();
	pgService = workbench.getProgressService();
	shell = workbench.getModalDialogShellProvider().getShell();
	display = workbench.getDisplay();
    }

    /**
     * @see IActionDelegate#run(IAction)
     */
    public void run(IAction action) {
	if (project == null || cProject == null)
	    return;

	try {
	    pgService.busyCursorWhile((pm) -> {
		SubMonitor subMonitor = SubMonitor.convert(pm, 100);
		try {
		    subMonitor.beginTask("Parsing project", 100);
		    Set<IASTTranslationUnit> asts = parseCProject();
		    try {
			Map<String, String> transformedCode = transformToOOgenModel(subMonitor, asts);
			generateJavaProject(subMonitor, transformedCode);
		    } catch (OperationCanceledException e) {
			showErrorWindow(e.getMessage());
		    }
		} finally {
		    subMonitor.done();
		}
	    });
	} catch (InvocationTargetException | InterruptedException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @see IActionDelegate#selectionChanged(IAction, ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
	if (selection instanceof IStructuredSelection) {
	    IStructuredSelection iss = (IStructuredSelection) selection;
	    Object first = iss.getFirstElement();
	    if (first instanceof IProject) {
		IProject proj = (IProject) first;
		if (!CoreModel.hasCNature(proj)) {
		    project = null;
		    cProject = null;
		} else {
		    project = proj;
		    cProject = CoreModel.getDefault().getCModel().getCProject(proj.getName());
		}
	    }
	}
    }

    private Map<String, String> transformToOOgenModel(SubMonitor subMonitor, Set<IASTTranslationUnit> asts) {
	SubMonitor transformationMonitor = subMonitor.split(50);
	transformationMonitor.beginTask("Transforming program code", 100);
	transformationMonitor.worked(10);

	ICToJavaTransformer transformer = new CToJavaTransformer();
	OOModel ooModel = transformer.transform(asts);

	Map<String, String> classes = new HashMap<>();
	OOCodeGeneratorTemplatesJava template = OOCodeGeneratorTemplatesJava.getInstance();
	for (OOPackage pkg : ooModel.getPackages()) {
	    for (OOClass cl : pkg.getClasses()) {
		classes.put(pkg.getName() + "." + cl.getName(), template.generate(cl));
	    }
	    for (OOEnumeration e : pkg.getEnums()) {
		classes.put(pkg.getName() + "." + e.getName(), template.generate(e));
	    }
	}

	transformationMonitor.worked(90);
	transformationMonitor.done();
	return classes;
    }

    private Set<IASTTranslationUnit> parseCProject() {
	Set<IASTTranslationUnit> asts = new HashSet<>();
	try {
	    for (ISourceRoot folder : cProject.getSourceRoots()) {
		if (folder.getElementName() == null || folder.getElementName().isEmpty())
		    continue;
		for (ITranslationUnit tu : folder.getTranslationUnits()) {
		    asts.add(tu.getAST());
		}
	    }
	} catch (CModelException cme) {
	    cme.printStackTrace();
	} catch (CoreException ce) {
	    ce.printStackTrace();
	}

	return asts;
    }

    private void generateJavaProject(SubMonitor subMonitor, Map<String, String> transformedCode) {
	SubMonitor generationMonitor = subMonitor.split(50);
	generationMonitor.beginTask("Generating source code", 100);
	String newProjName = cProject.getResource().getName() + ".javaproj";
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	IProject newProject = root.getProject(newProjName);

	try {
	    if (!newProject.exists()) {
		newProject.create(generationMonitor.split(5));
	    }
	    newProject.open(generationMonitor.split(5));
	    IProjectDescription newProjectDesc = newProject.getDescription();
	    newProjectDesc.setNatureIds(new String[] { JavaCore.NATURE_ID });
	    newProject.setDescription(newProjectDesc, generationMonitor.split(5));
	    IJavaProject javaProject = JavaCore.create(newProject);
	    IFolder binFolder = newProject.getFolder("bin");
	    if (!binFolder.exists()) {
		binFolder.create(true, true, generationMonitor.split(5));
		javaProject.setOutputLocation(binFolder.getFullPath(), generationMonitor.split(5));
	    }
	    List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
	    for (LibraryLocation element : JavaRuntime.getLibraryLocations(JavaRuntime.getDefaultVMInstall())) {
		entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
	    }
	    javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]),
		    generationMonitor.split(5));

	    IFolder srcFolder = newProject.getFolder("src");
	    if (!srcFolder.exists()) {
		srcFolder.create(true, true, generationMonitor.split(5));
	    }
	    IPackageFragmentRoot pkgRoot = javaProject.getPackageFragmentRoot(srcFolder);
	    IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
	    IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
	    System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
	    newEntries[oldEntries.length] = JavaCore.newSourceEntry(pkgRoot.getPath());
	    javaProject.setRawClasspath(newEntries, generationMonitor.split(5));

	    int increment = 60 / transformedCode.size();
	    for (Entry<String, String> e : transformedCode.entrySet()) {
		Matcher matcher = PKG_CLASS_PARSE_PATTERN.matcher(e.getKey());
		if (!matcher.matches())
		    continue;
		String pkgName = matcher.group(1);
		String fileName = matcher.group(2) + ".java";

		IPackageFragment pkg = javaProject.getPackageFragmentRoot(srcFolder).createPackageFragment(pkgName,
			false, generationMonitor.split(increment / 2));
		try {
		    pkg.createCompilationUnit(fileName, e.getValue(), true, generationMonitor.split(increment / 2));
		} catch (JavaModelException me) {
		    me.printStackTrace();
		}
	    }
	} catch (CoreException e) {
	    e.printStackTrace();
	}
    }
    
    private void showErrorWindow(String message) {
	display.asyncExec(new Runnable() {
	    @Override
	    public void run() {
		MessageDialog.openError(shell, "Error", message);
	    }
	});
    }
}
