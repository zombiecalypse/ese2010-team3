package models.database;

import java.util.Collection;
import java.util.List;

import models.User;

public interface IUserDatabase {

	/**
	 * Get the <code>User</code> with the given name.
	 * 
	 * @param name
	 *            unique user name
	 * @return a <code>User</code> or null if the given name doesn't exist.
	 */
	public User get(String name);

	/**
	 * Creates a <code>User</code> with the given credentials. Asserts that
	 * <code>needsSignUp(username)</code> before executing this and
	 * <code>register(username,password) == get(name)</code>
	 * 
	 * @param username
	 *            unique identifier
	 * @param password
	 * @return The user with this credentials
	 */
	public User register(String username, String password);

	/**
	 * Validate if the <code>User</code> is already in our database.
	 * 
	 * @param username
	 * @return True iff there is no <code>User</code> of that name
	 */
	public boolean needSignUp(String username);

	/**
	 * Deletes the user from the database without clean up.
	 * 
	 * @param name
	 */

	public void remove(String name);

	/**
	 * A collection of all registered Users in the system.
	 * 
	 * @return
	 */

	public Collection<User> all();

	/**
	 * The number of all registered Users.
	 * 
	 * @return int n >= 0
	 */

	public int count();

	/**
	 * Deletes every user from the DB.
	 */

	public void clear();

	/**
	 * Add a User directly.
	 * 
	 * @param user
	 */

	public void add(User user);

	/**
	 * Get moderating crowd.
	 */
	public Collection<User> allModerators();

	/**
	 * Searches for the given <code>term</code>. This includes at least a search
	 * for the User's name.
	 * 
	 * @param term
	 * @return a list of <code>User</code>s, by decreasing fit for the given
	 *         term.
	 */
	public List<User> searchFor(String term);
}
