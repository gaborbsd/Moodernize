package hu.bme.aut.moodernize.c2j.commentmapping;

import org.eclipse.cdt.core.dom.ast.IASTComment;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.IEnumeration;
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
    }

    public int visit(IASTName name) {
	if (!isCorrectContainingFile(name)) {
	    return PROCESS_SKIP;
	}

	IBinding binding = name.resolveBinding();
	if (binding instanceof IFunction || binding instanceof IEnumeration || (binding instanceof ICompositeType
		&& ((ICompositeType) binding).getKey() == ICompositeType.k_struct)) {
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

    public CommentOwnerResult getCommentOwnerResult() {
	if (commentLineNumber < currentClosestLineNumber) {
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
