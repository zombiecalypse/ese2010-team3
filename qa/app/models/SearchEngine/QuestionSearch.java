package models.SearchEngine;

import static models.helpers.SetOperations.intersection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import models.Answer;
import models.Question;
import models.Tag;

public class QuestionSearch extends EntrySearch<Question> {
	private final Set<Tag> queryTags;

	public QuestionSearch(String query, Set<Tag> tags) {
		super(query);
		this.queryTags = tags != null ? tags : Collections.EMPTY_SET;
	}

	public Double visit(Question question) {
		clearMustHave();
		double tagRating = rateTags(question, this.mustHave);
		double textRating = rateText(question, this.mustHave);
		double answerRating = rateAnswers(question, this.mustHave);
		double rating = tagRating + textRating + answerRating;
		// words that aren't tags must appear in a question's content (AND
		// search)
		if (this.mustHave.size() != 0)
			return null;
		return rating > 0 ? -rating : null;
	}

	private double rateTags(Question question, Set<String> mustHave) {
		Set<Tag> tags = new HashSet<Tag>(question.getTags());
		if (this.queryTags == null || this.queryTags.isEmpty()
				|| tags.isEmpty())
			return 0;
		for (Tag tag : tags) {
			mustHave.remove(tag.getName());
		}

		// rate highest questions that share most of the tags and don't have
		// hardly any additional tags
		return Math.pow(intersection(tags, this.queryTags).size(), 2)
				/ this.queryTags.size() / tags.size();
	}

	private double rateAnswers(Question question, Set<String> mustHave) {
		double rating = 0;
		for (Answer ans : question.answers()) {
			rating += rateText(ans, mustHave);
		}
		int answerCount = question.countAnswers();
		return rating / (answerCount == 0 ? 1 : answerCount);
	}
}
