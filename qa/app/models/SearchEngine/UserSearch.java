package models.SearchEngine;

import static models.helpers.StringDistance.getLevenshteinDistance;
import models.User;
import models.helpers.IFilter;

public class UserSearch implements IFilter<User, Double> {

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
		if (rating > getMinimalMatchQuality())
			return null;
		else
			return rating;
	}

	private Double getMinimalMatchQuality() {
		return 0.25;
	}

}
