package hu.bme.aut.moodernize.ui.popup.actions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.index.IIndex;
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
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.progress.IProgressService;

import hu.bme.aut.moodernize.c2j.TransformC2J;

public class TransformCToJava implements IObjectActionDelegate {
	private static final Pattern PKG_CLASS_PARSE_PATTERN = Pattern.compile("(.*)\\.(.*)");

	private IProject project;
	private ICProject cProject;
	
	private IProgressService pgService;

	public TransformCToJava() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		pgService = targetPart.getSite().getWorkbenchWindow().getWorkbench().getProgressService();
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
					subMonitor.beginTask("Modernizing project", 100);
					
					SubMonitor transformationMonitor = subMonitor.split(50);
					transformationMonitor.beginTask("Transforming program code", 100);
					
					IIndex index = CCorePlugin.getIndexManager().getIndex(cProject);

					Set<IASTTranslationUnit> asts = new HashSet<>();
					ISourceRoot sourceRoots[] = cProject.getSourceRoots();
					for (ISourceRoot folder : sourceRoots) {
						if (folder.getElementName() == null || folder.getElementName().isEmpty())
							continue;
						for (ITranslationUnit tu : folder.getTranslationUnits()) {
							IASTTranslationUnit ast = tu.getAST();
							asts.add(ast);
						}
					}
					transformationMonitor.worked(10);
					Map<String, String> transformedCode = TransformC2J.transform(index, asts);
					transformationMonitor.worked(90);
					transformationMonitor.done();

					SubMonitor generationMonitor = subMonitor.split(50);
					generationMonitor.beginTask("Generating source code", 100);
					String newProjName = cProject.getResource().getName() + ".javaproj";
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
					IProject newProject = root.getProject(newProjName);
					if (!newProject.exists())
						newProject.create(generationMonitor.split(5));
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
					IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
					LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
					for (LibraryLocation element : locations) {
						entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
					}
					javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), generationMonitor.split(5));

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

						pkg.createCompilationUnit(fileName, e.getValue(), true, generationMonitor.split(increment / 2));
					}
				} catch (CModelException e) {
					e.printStackTrace();
				} catch (CoreException e1) {
					e1.printStackTrace();
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

}
