package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

import hu.bme.aut.apitransform.apiTransform.Model;
import hu.bme.aut.moodernize.c2j.callchain.CallChainAnalyzer;
import hu.bme.aut.moodernize.c2j.callchain.CallGraph;
import hu.bme.aut.moodernize.c2j.callchain.FunctionCallMapHolder;
import hu.bme.aut.moodernize.c2j.callchain.Vertex;
import hu.bme.aut.moodernize.c2j.commentmapping.CommentOwnerResult;
import hu.bme.aut.moodernize.c2j.commentmapping.CommentOwnerVisitor;
import hu.bme.aut.moodernize.c2j.dataholders.CommentMappingDataHolder;
import hu.bme.aut.moodernize.c2j.dataholders.FunctionCallExpressionDataHolder;
import hu.bme.aut.moodernize.c2j.dataholders.RemovedParameterDataHolder;
import hu.bme.aut.moodernize.c2j.dataholders.TransformationDataHolder;
import hu.bme.aut.moodernize.c2j.pointerconversion.PointerConversionDataHolder;
import hu.bme.aut.moodernize.c2j.projectcreation.CallChainClassCreator;
import hu.bme.aut.moodernize.c2j.projectcreation.MainClassCreator;
import hu.bme.aut.moodernize.c2j.projectcreation.ProjectCreator;
import hu.bme.aut.moodernize.c2j.projectcreation.SupplementingMethodCreator;
import hu.bme.aut.moodernize.c2j.util.TransformUtil;
import hu.bme.aut.moodernize.c2j.visitor.AbstractBaseVisitor;
import hu.bme.aut.moodernize.c2j.visitor.EnumVisitor;
import hu.bme.aut.moodernize.c2j.visitor.FunctionDeclarationVisitor;
import hu.bme.aut.moodernize.c2j.visitor.FunctionDefinitionVisitor;
import hu.bme.aut.moodernize.c2j.visitor.GlobalVariableVisitor;
import hu.bme.aut.moodernize.c2j.visitor.ProblemVisitor;
import hu.bme.aut.moodernize.c2j.visitor.StructVisitor;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOEnumeration;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OogenFactory;

public class CToJavaTransformer {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    private OOModel model = factory.createOOModel();
    private List<OOClass> createdClasses = TransformationDataHolder.createdClasses;
    private List<OOMethod> globalFunctions = new ArrayList<OOMethod>();
    private List<OOEnumeration> enums = new ArrayList<OOEnumeration>();
    public static Model apiModel = null;
    private FunctionCallMapHolder functionCalls = new FunctionCallMapHolder();

    public OOModel transform(Set<IASTTranslationUnit> asts, Model apiTransformModel) {
	apiModel = apiTransformModel;
	// checkForErrors(asts);

	clearDataHolders();
	createCommentMappings(asts);

	collectProjectStructure(asts);
	assignFunctionsToClassesBySignature();

	createMainClass();
	createSupplementingMethods();
	collectFunctionDefinitions(asts);
	createClassesFromCallChains();
	createProjectHierarchy();

	return model;
    }

    private void checkForErrors(Set<IASTTranslationUnit> asts) {
	for (IASTTranslationUnit ast : asts) {
	    if (ast != null) {
		ast.accept(new ProblemVisitor(ast.getContainingFilename()));
	    }
	}
    }

    private void clearDataHolders() {
	TransformationDataHolder.clear();
	RemovedParameterDataHolder.clearEntries();
	FunctionCallExpressionDataHolder.clearFunctionCalls("");
	CommentMappingDataHolder.clearMappings();
	PointerConversionDataHolder.clear();
    }

    private void createCommentMappings(Set<IASTTranslationUnit> asts) {
	List<CommentOwnerResult> commentOwners = new ArrayList<CommentOwnerResult>();
	for (IASTTranslationUnit ast : asts) {
	    if (ast != null) {
		for (IASTComment comment : ast.getComments()) {
		    if (comment.getContainingFilename().equals(ast.getContainingFilename())
			    && comment.getFileLocation() != null) {
			CommentOwnerVisitor visitor = new CommentOwnerVisitor(ast.getContainingFilename(), comment);
			ast.accept(visitor);
			commentOwners.add(visitor.getCommentOwnerResult());
		    }
		}
	    }
	}
	CommentMappingDataHolder.addAllMappings(commentOwners);
    }

    private void collectProjectStructure(Set<IASTTranslationUnit> asts) {
	for (IASTTranslationUnit ast : asts) {
	    if (ast != null) {
		acceptVisitors(ast, ast.getContainingFilename());
	    }
	}
    }

    private void acceptVisitors(IASTTranslationUnit ast, String containingFilename) {
	List<AbstractBaseVisitor> visitors = new ArrayList<AbstractBaseVisitor>();
	visitors.add(new GlobalVariableVisitor(containingFilename, model.getGlobalVariables()));
	visitors.add(new StructVisitor(containingFilename, createdClasses));
	visitors.add(new EnumVisitor(containingFilename, enums));
	visitors.add(new FunctionDeclarationVisitor(containingFilename, globalFunctions));

	for (AbstractBaseVisitor visitor : visitors) {
	    ast.accept(visitor);
	}
    }

    private void assignFunctionsToClassesBySignature() {
	ParameterListAndReturnTypeAnalyser assigner = new ParameterListAndReturnTypeAnalyser(createdClasses,
		globalFunctions);
	assigner.assignFunctionsToClasses();
    }

    private void collectFunctionDefinitions(Set<IASTTranslationUnit> asts) {
	for (IASTTranslationUnit ast : asts) {
	    if (ast != null) {
		FunctionDefinitionVisitor visitor = new FunctionDefinitionVisitor(ast.getContainingFilename(),
			getAllFunctions());
		ast.accept(visitor);
		visitor.getFunctionCallMap().entrySet().forEach(entry -> {
		    if (TransformUtil.listContainsMethod(
			    TransformUtil.getMainClassFromClasses(createdClasses).getMethods(), entry.getKey())) {
			functionCalls.addCallGraphEntry(entry.getKey(), entry.getValue());
		    } else {
			functionCalls.addOutsideFunctionEntry(entry.getKey(), entry.getValue());
		    }
		});
	    }
	}
	// PointerConversionDataHolder.writeResultsToFile();
    }

    private void createClassesFromCallChains() {
	CallChainAnalyzer analyzer = new CallChainAnalyzer();
	List<List<Vertex>> classes = analyzer.createClasses(
		new CallGraph(functionCalls.getCallGraphFunctionCalls(),
			TransformUtil.getMainClassFromClasses(createdClasses).getMethods()),
		functionCalls.getOutsideFunctionCalls());

	for (List<Vertex> createdClass : classes) {
	    System.out.println("---------------NEW CLASS------------");
	    for (Vertex vertex : createdClass) {
		System.out.println(vertex.getName());
	    }
	}

	createdClasses.addAll(new CallChainClassCreator().createCallChainClasses(classes,
		TransformUtil.getMainClassFromClasses(createdClasses).getMethods(), functionCalls));
    }

    private void createSupplementingMethods() {
	new SupplementingMethodCreator().createSupplementingMethods(createdClasses);
    }

    private void createMainClass() {
	createdClasses.add(new MainClassCreator().createMainClass(model.getGlobalVariables(), globalFunctions));
	model.getGlobalVariables().clear();
    }

    private void createProjectHierarchy() {
	globalFunctions.forEach(f -> model.getGlobalFunctions().add(f));
	ProjectCreator projectCreator = new ProjectCreator();
	projectCreator.createProjectHierarchy(model, createdClasses, enums);
    }

    private List<OOMethod> getAllFunctions() {
	List<OOMethod> functions = new ArrayList<OOMethod>();
	globalFunctions.forEach(function -> functions.add(function));
	createdClasses.forEach(cl -> cl.getMethods().forEach(method -> functions.add(method)));

	return functions;
    }
}