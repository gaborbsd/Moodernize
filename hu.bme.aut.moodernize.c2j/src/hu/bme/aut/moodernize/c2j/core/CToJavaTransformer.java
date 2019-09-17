package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

import hu.bme.aut.moodernize.c2j.commentmapping.CommentOwnerResult;
import hu.bme.aut.moodernize.c2j.commentmapping.CommentOwnerVisitor;
import hu.bme.aut.moodernize.c2j.dataholders.FunctionCallExpressionDataHolder;
import hu.bme.aut.moodernize.c2j.dataholders.RemovedParameterDataHolder;
import hu.bme.aut.moodernize.c2j.dataholders.TransformationDataHolder;
import hu.bme.aut.moodernize.c2j.projectcreation.MainClassCreator;
import hu.bme.aut.moodernize.c2j.projectcreation.ProjectCreator;
import hu.bme.aut.moodernize.c2j.projectcreation.SupplementingMethodCreator;
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

public class CToJavaTransformer implements ICToJavaTransformer {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    private OOModel model = factory.createOOModel();
    private List<OOClass> createdClasses = TransformationDataHolder.getCreatedClasses();
    private List<OOMethod> globalFunctions = new ArrayList<OOMethod>();
    private List<OOEnumeration> enums = new ArrayList<OOEnumeration>();

    @Override
    public OOModel transform(Set<IASTTranslationUnit> asts) {
	checkForErrors(asts);
	clearDataStructures();
	createCommentMappings(asts);
	traverseAsts(asts);
	assignFunctionsToClassesBySignature();
	createMainClass();
	createSupplementingMethods();
	collectFunctionDefinitions(asts);
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

    private void clearDataStructures() {
	TransformationDataHolder.clear();
	RemovedParameterDataHolder.clearEntries();
	FunctionCallExpressionDataHolder.clearFunctionCalls("");
    }

    private void createCommentMappings(Set<IASTTranslationUnit> asts) {
	for (IASTTranslationUnit ast : asts) {
	    if (ast != null) {
		List<CommentOwnerResult> commentOwners = new ArrayList<CommentOwnerResult>();
		for (IASTComment comment : ast.getComments()) {
		    CommentOwnerVisitor visitor = new CommentOwnerVisitor(ast.getContainingFilename(),
			    comment.getFileLocation().getStartingLineNumber());
		    ast.accept(visitor);
		    commentOwners.add(visitor.getCommentOwnerResult());
		}
		System.out.println("Dummy");
	    }
	}
    }

    private void traverseAsts(Set<IASTTranslationUnit> asts) {
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
		ast.accept(new FunctionDefinitionVisitor(ast.getContainingFilename(), getAllFunctions()));
	    }
	}
    }

    private void createSupplementingMethods() {
	new SupplementingMethodCreator().createSupplementingMethods(createdClasses);
    }

    private void createMainClass() {
	createdClasses.add(new MainClassCreator().createMainClass(model.getGlobalVariables(), globalFunctions));
	model.getGlobalVariables().clear();
    }

    private void collectComments(Set<IASTTranslationUnit> asts) {

    }

    private void createProjectHierarchy() {
	globalFunctions.forEach(f -> model.getGlobalFunctions().add(f));
	ProjectCreator projectCreator = new ProjectCreator();
	projectCreator.createProjectHierarchy(model, createdClasses, enums);
    }

    private List<OOMethod> getAllFunctions() {
	List<OOMethod> functions = new ArrayList<OOMethod>();
	for (OOMethod function : globalFunctions) {
	    functions.add(function);
	}
	for (OOClass cl : createdClasses) {
	    for (OOMethod method : cl.getMethods()) {
		functions.add(method);
	    }
	}

	return functions;
    }
}