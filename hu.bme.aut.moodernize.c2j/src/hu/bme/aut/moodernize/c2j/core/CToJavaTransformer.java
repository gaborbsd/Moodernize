package hu.bme.aut.moodernize.c2j.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;

import hu.bme.aut.moodernize.c2j.util.RemovedParameterRepository;
import hu.bme.aut.moodernize.c2j.visitor.AbstractBaseVisitor;
import hu.bme.aut.moodernize.c2j.visitor.FunctionDeclarationVisitor;
import hu.bme.aut.moodernize.c2j.visitor.FunctionDefinitionVisitor;
import hu.bme.aut.moodernize.c2j.visitor.GlobalVariableVisitor;
import hu.bme.aut.moodernize.c2j.visitor.StructVisitor;
import hu.bme.aut.oogen.OOClass;
import hu.bme.aut.oogen.OOMethod;
import hu.bme.aut.oogen.OOModel;
import hu.bme.aut.oogen.OogenFactory;

public class CToJavaTransformer implements ICToJavaTransformer {
    private static OogenFactory factory = OogenFactory.eINSTANCE;

    private OOModel model = factory.createOOModel();
    private List<OOClass> createdClasses = new ArrayList<OOClass>();

    @Override
    public OOModel transform(Set<IASTTranslationUnit> asts) {
	clearDataStructures();
	traverseAsts(asts);
	assignFunctionsToClassesBySignature();
	collectFunctionDefinitions(asts);
	createProjectHierarchy(model, createdClasses);

	return model;
    }
    
    private void clearDataStructures() {
	RemovedParameterRepository.clearEntries();
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
	visitors.add(new FunctionDeclarationVisitor(containingFilename, model.getGlobalFunctions()));

	for (AbstractBaseVisitor visitor : visitors) {
	    ast.accept(visitor);
	}
    }

    private void assignFunctionsToClassesBySignature() {
	ParameterListAndReturnTypeAnalyser assigner = new ParameterListAndReturnTypeAnalyser(createdClasses, model.getGlobalFunctions());
	assigner.assignFunctionsToClasses();
    }

    private void collectFunctionDefinitions(Set<IASTTranslationUnit> asts) {
	List<OOMethod> functions = new ArrayList<OOMethod>();
	for (OOMethod function : model.getGlobalFunctions()) {
	    functions.add(function);
	}
	for (OOClass cl : createdClasses) {
	    for (OOMethod method : cl.getMethods()) {
		functions.add(method);
	    }
	}
	for (IASTTranslationUnit ast : asts) {
	    if (ast != null) {
		ast.accept(new FunctionDefinitionVisitor(ast.getContainingFilename(), functions));
	    }
	}
    }

    private void createProjectHierarchy(OOModel model, List<OOClass> createdClasses) {
	ProjectCreator projectCreator = new ProjectCreator();
	projectCreator.createProjectHierarchy(model, createdClasses);
    }
}