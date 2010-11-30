package models.SearchEngine;

import models.Comment;

public class CommentSearch extends EntrySearch<Comment> {

	public CommentSearch(String query) {
		super(query);
	}

	public Double visit(Comment comment) {
		clearMustHave();
		double rating = rateText(comment, this.mustHave);
		return this.mustHave.isEmpty() ? rating : null;
	}

}
