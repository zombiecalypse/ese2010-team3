package models.SearchEngine;

import models.Answer;

public class AnswerSearch extends EntrySearch<Answer> {

	public AnswerSearch(String query) {
		super(query);
	}

	@Override
	public Double visit(Answer answer) {
		clearMustHave();
		double rating = rateText(answer, this.mustHave);
		return this.mustHave.isEmpty() ? rating : null;
	}
}
