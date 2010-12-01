package controllers;

import java.util.List;

import models.Answer;
import models.Question;
import models.User;
import models.database.Database;
import models.helpers.Tools;
import play.mvc.Controller;

public class Search extends Controller {

	public static void searchAll(String term) {
		List<Question> questions = Database.get().questions().searchFor(term);
		List<User> users = Database.get().users().searchFor(term);
		List<Answer> answers = Database.get().questions().searchForAnswer(term);
		boolean moreQuestions = false, moreUsers = false, moreAnswers = false;
		if (questions.size() > getQuestionSearchPreviewSize()) {
			questions = questions.subList(0, getQuestionSearchPreviewSize());
			moreQuestions = true;
		}
		if (users.size() > getUserSearchPreviewSize()) {
			users = users.subList(0, getUserSearchPreviewSize());
			moreUsers = true;
		}
		if (answers.size() > getAnswerSearchPreviewSize()) {
			answers = answers.subList(0, getAnswerSearchPreviewSize());
			moreAnswers = true;
		}
		render(term, questions, answers, users, moreQuestions, moreAnswers,
				moreUsers);
	}

	private static int getAnswerSearchPreviewSize() {
		return 3;
	}

	private static int getQuestionSearchPreviewSize() {
		return 3;
	}

	private static int getUserSearchPreviewSize() {
		return 3;
	}

	public static void searchQuestion(String term, int index) {
		List<Question> questions = Database.get().questions().searchFor(term);

		int maxIndex = Tools.determineMaximumIndex(questions,
				Session.get().getEntriesPerPage());

		questions = Tools.paginate(questions,
				Session.get().getEntriesPerPage(), index);
		render(questions, term, index, maxIndex);
	}

	public static void searchAnswer(String term, int index) {
		List<Answer> answers = Database.get().questions().searchForAnswer(term);
		int maxIndex = Tools.determineMaximumIndex(answers,
				Session.get().getEntriesPerPage());

		answers = Tools.paginate(answers,
				Session.get().getEntriesPerPage(), index);
		render(answers, term, index, maxIndex);
	}

	public static void searchUser(String term, int index) {
		List<User> users = Database.get().users().searchFor(term);
		int maxIndex = Tools.determineMaximumIndex(users,
				Session.get().getEntriesPerPage());

		users = Tools.paginate(users,
				Session.get().getEntriesPerPage(), index);
		render(users, term, index, maxIndex);
	}
}
