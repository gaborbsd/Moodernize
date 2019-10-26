package hu.bme.aut.moodernize.c2j.commentmapping;

import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.IEnumeration;
import org.eclipse.cdt.core.dom.ast.IField;
import org.eclipse.cdt.core.dom.ast.IFunction;

import hu.bme.aut.moodernize.c2j.visitor.AbstractBaseVisitor;
import hu.bme.aut.oogen.OOComment;

public class CommentOwnerVisitor extends AbstractBaseVisitor {
    private int commentLineNumber;
    private int currentClosestLineNumber = Integer.MAX_VALUE;
    private CommentOwnerResult commentOwnerResult = new CommentOwnerResult();
    private IASTComment comment;
    private IASTFileLocation commentLocation;
    int epsilon = 10;

    public CommentOwnerVisitor(String fileName, IASTComment comment) {
	super(fileName);

	this.commentLineNumber = comment.getFileLocation().getStartingLineNumber();
	this.comment = comment;
	this.commentLocation = comment.getFileLocation();

	commentOwnerResult.commentOwner = null;
	commentOwnerResult.position = null;

	this.shouldVisitNames = true;
	this.shouldVisitStatements = true;
	if (comment.isBlockComment()) {
	    this.shouldVisitExpressions = true;
	}
    }

    public int visit(IASTName name) {
	if (!isCorrectContainingFile(name)) {
	    return PROCESS_SKIP;
	}

	IBinding binding = name.resolveBinding();
	if (binding instanceof IFunction || binding instanceof IEnumeration || binding instanceof IField
		|| (isStruct(binding))) {
	    if (name == null || name.getFileLocation() == null) {
		return PROCESS_SKIP;
	    }
	    int distance = Math.abs(name.getFileLocation().getStartingLineNumber() - commentLineNumber);
	    int currentDistance = Math.abs(currentClosestLineNumber - commentLineNumber);

	    if (distance < currentDistance) {
		currentClosestLineNumber = name.getFileLocation().getStartingLineNumber();
		commentOwnerResult.commentOwner = binding;
	    }

	    return calculateReturnValue(name);
	}

	return PROCESS_SKIP;
    }

    public int visit(IASTStatement statement) {
	if (!isCorrectContainingFile(statement)) {
	    return PROCESS_SKIP;
	}

	if (statement instanceof IASTCompoundStatement) {
	    return calculateReturnValue(statement);
	}

	int distance = Math.abs(statement.getFileLocation().getStartingLineNumber() - commentLineNumber);
	int currentDistance = Math.abs(currentClosestLineNumber - commentLineNumber);
	if (distance < currentDistance
		|| (distance == currentDistance && commentOwnerResult.commentOwner instanceof IFunction)) {
	    currentClosestLineNumber = statement.getFileLocation().getStartingLineNumber();
	    commentOwnerResult.commentOwner = statement;
	}

	return calculateReturnValue(statement);
    }

    public int visit(IASTExpression expression) {
	if (!isCorrectContainingFile(expression)) {
	    return PROCESS_SKIP;
	}

	int distance = Math.abs(expression.getFileLocation().getStartingLineNumber() - commentLineNumber);
	int currentDistance = Math.abs(currentClosestLineNumber - commentLineNumber);
	if (distance <= currentDistance) {
	    currentClosestLineNumber = expression.getFileLocation().getStartingLineNumber();
	    commentOwnerResult.commentOwner = expression;
	}

	return calculateReturnValue(expression);
    }

    private boolean isStruct(IBinding binding) {
	return binding instanceof ICompositeType && ((ICompositeType) binding).getKey() == ICompositeType.k_struct;
    }

    private boolean commentIsBeforeNode(IASTNode node) {
	IASTFileLocation nodeLocation = node.getFileLocation();
	return commentLocation.getStartingLineNumber() < nodeLocation.getStartingLineNumber();
    }

    private int calculateLineDifference(IASTNode node) {
	IASTFileLocation nodeLocation = node.getFileLocation();
	int difference;
	if (commentIsBeforeNode(node)) {
	    difference = nodeLocation.getStartingLineNumber() - commentLocation.getEndingLineNumber();
	} else {
	    difference = commentLocation.getStartingLineNumber() - nodeLocation.getEndingLineNumber();
	}

	return difference;
    }

    private int calculateReturnValue(IASTNode node) {
	int lineDifference = calculateLineDifference(node);
	if (lineDifference <= epsilon) {
	    return PROCESS_CONTINUE;
	} else {
	    return PROCESS_SKIP;
	}
    }

    public CommentOwnerResult getCommentOwnerResult() {
	if (commentLineNumber <= currentClosestLineNumber) {
	    commentOwnerResult.position = CommentPosition.BEFORE;
	} else {
	    commentOwnerResult.position = CommentPosition.AFTER;
	}

	OOComment ooComment = factory.createOOComment();
	ooComment.setIsBlockComment(this.comment.isBlockComment());
	ooComment.setText(String.copyValueOf(this.comment.getComment()));
	commentOwnerResult.comment = ooComment;

	return commentOwnerResult;
    }
}
