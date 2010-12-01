package tests.mocks;

import models.User;
import controllers.Session;

public class SessionMock extends Session {
	private User user;

	public User currentUser() {
		return this.user;
	}

	public void loginAs(User _user) {
		this.user = _user;
	}

}
