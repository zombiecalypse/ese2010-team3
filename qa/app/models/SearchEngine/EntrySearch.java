package models.SearchEngine;

import static models.helpers.SetOperations.difference;
import static models.helpers.SetOperations.intersection;

import java.util.HashSet;
import java.util.Set;

import models.Entry;
import models.helpers.Filter;
import models.helpers.Tools;

public abstract class EntrySearch<E extends Entry> implements Filter<E, Double> {
	protected final Set<String> queryFulltext;
	protected final Set<String> mustHave;

	public EntrySearch(String query) {
		this.queryFulltext = getWords(query);
		this.mustHave = new HashSet();
	}

	protected void clearMustHave() {
		this.mustHave.clear();
		this.mustHave.addAll(this.queryFulltext);
	}

	protected double rateText(Entry entry, Set<String> mustHave) {
		Set<String> words = getWords(entry.content());
		if (this.queryFulltext.isEmpty() || words.isEmpty())
			return 0;
		mustHave.removeAll(words);
		return 1.0 * intersection(words, this.queryFulltext).size()
				/ words.size();
	}

	protected Set<String> getWords(String string) {

		// this isn't ideally placed...
		string = Tools.htmlToText(string);

		Set<String> words = new HashSet<String>();
		if (string == null)
			return words;
		for (String word : string.split("\\W+")) {
			words.add(word.toLowerCase());
		}
		words.remove(""); // remove splitting artifact
		return difference(words, StopWords.get());
	}

}
