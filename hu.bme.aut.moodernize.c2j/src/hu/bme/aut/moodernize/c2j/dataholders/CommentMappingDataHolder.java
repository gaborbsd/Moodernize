package hu.bme.aut.moodernize.c2j.dataholders;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.ICompositeType;
import org.eclipse.cdt.core.dom.ast.IEnumeration;
import org.eclipse.cdt.core.dom.ast.IField;
import org.eclipse.cdt.core.dom.ast.IFunction;

import hu.bme.aut.moodernize.c2j.commentmapping.CommentOwnerResult;

public class CommentMappingDataHolder {
    private static List<CommentOwnerResult> commentOwners = new ArrayList<CommentOwnerResult>();

    public static void clearMappings() {
	commentOwners.clear();
    }

    public static void addAllMappings(List<CommentOwnerResult> mappings) {
	clearMappings();
	for (CommentOwnerResult mapping : mappings) {
	    commentOwners.add(mapping);
	}
    }

    public static List<CommentOwnerResult> findAllOwnedComments(ICompositeType potentialOwner) {
	return collectAllOwnedComments(ICompositeType.class, potentialOwner);
    }
    
    public static List<CommentOwnerResult> findAllOwnedComments(IFunction potentialOwner) {
	return collectAllOwnedComments(IFunction.class, potentialOwner);
    }
    
    public static List<CommentOwnerResult> findAllOwnedComments(IEnumeration potentialOwner) {
	return collectAllOwnedComments(IEnumeration.class, potentialOwner);
    }
    
    public static List<CommentOwnerResult> findAllOwnedComments(IASTStatement potentialOwner) {
	return collectAllOwnedComments(IASTStatement.class, potentialOwner);
    }
    
    public static List<CommentOwnerResult> findAllOwnedComments(IField potentialOwner) {
	return collectAllOwnedComments(IField.class, potentialOwner);
    }
    
    public static List<CommentOwnerResult> findAllOwnedComments(IASTExpression potentialOwner) {
	return collectAllOwnedComments(IASTExpression.class, potentialOwner);
    }
    
    private static <T> List<CommentOwnerResult> collectAllOwnedComments(Class<T> type, T potentialOwner) {
	List<CommentOwnerResult> ownedComments = new ArrayList<CommentOwnerResult>();

	for (CommentOwnerResult mapping : commentOwners) {
	    if (type.isInstance(mapping.commentOwner)) {
		@SuppressWarnings("unchecked")
		T downcastOwner = (T) mapping.commentOwner;
		if (downcastOwner == potentialOwner) {
		    ownedComments.add(mapping);
		}
	    }
	}

	return ownedComments;
    }
}
