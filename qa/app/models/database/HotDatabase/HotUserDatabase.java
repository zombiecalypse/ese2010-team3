package models.database.HotDatabase;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.User;
import models.SearchEngine.UserSearch;
import models.database.IUserDatabase;
import models.helpers.Mapper;

public class HotUserDatabase implements IUserDatabase {
	/** Tracks all users by their lowercase(!) usernames. */
	private static HashMap<String, User> users = new HashMap();

	public boolean isAvailable(String username) {
		return this.get(username) == null;
	}

	public User register(String username, String password, String email) {
		User user = new User(username, password, email);
		users.put(username.toLowerCase(), user);
		return user;
	}

	public User get(String name) {
		return users.get(name.toLowerCase());
	}

	public void remove(String name) {
		users.remove(name.toLowerCase());
	}

	public Collection<User> all() {
		return users.values();
	}

	public int count() {
		return users.size();
	}

	public void clear() {
		users.clear();
	}

	public void add(User user) {
		users.put(user.getName().toLowerCase(), user);
	}

	public Collection<User> allModerators() {
		Set<User> moderators = new HashSet();
		for (User user : users.values()) {
			if (user.isModerator()) {
				moderators.add(user);
			}
		}
		return moderators;
	}

	public List<User> searchFor(String term) {
		return Mapper.sort(all(), new UserSearch(term));
	}
}
