package hu.bme.aut.moodernize.c2j.util;

import java.util.List;

import hu.bme.aut.moodernize.c2j.commentmapping.CommentOwnerResult;
import hu.bme.aut.moodernize.c2j.commentmapping.CommentPosition;
import hu.bme.aut.oogen.OOCommentOwner;

public class CommentProcessor {
    public static void processOwnedComments(OOCommentOwner owner, List<CommentOwnerResult> ownedComments) {
	for (CommentOwnerResult mapping : ownedComments) {
	    if (mapping.position == CommentPosition.BEFORE) {
		owner.getBeforeComments().add(mapping.comment);
	    } else if (mapping.position == CommentPosition.AFTER) {
		owner.getAfterComments().add(mapping.comment);
	    }
	}
    }
}
