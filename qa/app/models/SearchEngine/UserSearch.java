package models.SearchEngine;

import static models.helpers.StringDistance.getLevenshteinDistance;
import models.User;
import models.helpers.Filter;

public class UserSearch implements Filter<User, Double> {

	private String searchString;

	public UserSearch(String searchString) {
		super();
		this.searchString = searchString;
	}

	public Double rateUsername(String name) {
		Double distance = ((Integer) getLevenshteinDistance(name.toLowerCase(),
				this.searchString.toLowerCase())).doubleValue();
		return (distance * distance)
				/ (name.length() * this.searchString.length());
	}

	public Double visit(User user) {
		Double rating = rateUsername(user.getName());
		if (rating > 0.25)
			return null;
		else
			return rating;
	}

}
