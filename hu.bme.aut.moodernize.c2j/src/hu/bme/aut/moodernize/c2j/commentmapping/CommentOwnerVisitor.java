package hu.bme.aut.moodernize.c2j.commentmapping;

import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTCompoundStatement;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTName;
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

    public CommentOwnerVisitor(String fileName, IASTComment comment) {
	super(fileName);

	this.commentLineNumber = comment.getFileLocation().getStartingLineNumber();
	this.comment = comment;

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
	    int distance = Math.abs(name.getFileLocation().getStartingLineNumber() - commentLineNumber);
	    int currentDistance = Math.abs(currentClosestLineNumber - commentLineNumber);

	    if (distance < currentDistance) {
		currentClosestLineNumber = name.getFileLocation().getStartingLineNumber();
		commentOwnerResult.commentOwner = binding;
	    }

	    return PROCESS_CONTINUE;
	}

	return PROCESS_SKIP;
    }

    public int visit(IASTStatement statement) {
	if (!isCorrectContainingFile(statement)) {
	    return PROCESS_SKIP;
	}

	if (statement instanceof IASTCompoundStatement) {
	    return PROCESS_CONTINUE;
	}
	
	int distance = Math.abs(statement.getFileLocation().getStartingLineNumber() - commentLineNumber);
	int currentDistance = Math.abs(currentClosestLineNumber - commentLineNumber);
	if (distance < currentDistance
		|| (distance == currentDistance && commentOwnerResult.commentOwner instanceof IFunction)) {
	    currentClosestLineNumber = statement.getFileLocation().getStartingLineNumber();
	    commentOwnerResult.commentOwner = statement;
	}

	return PROCESS_CONTINUE;
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

	return PROCESS_CONTINUE;
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

    private boolean isStruct(IBinding binding) {
	return binding instanceof ICompositeType && ((ICompositeType) binding).getKey() == ICompositeType.k_struct;
    }
}
