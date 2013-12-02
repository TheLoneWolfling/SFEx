package ApplicationLogic;

import java.sql.SQLException;

import DatabaseFrontend.User;

public class UserControl {
	
	public final Control p;
	
	public UserControl(Control c) {
		this.p = c;
	}

	public UserWrapper getUserFromEmail(String email) {
		try {
			return wrap(User.getUserFromEmail(email));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	UserWrapper wrap(User user) {
		return new UserWrapper(user, this.p);
	}

	public UserWrapper getUserFromId(long id) {
		try {
			return wrap(User.getUserFromID(id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public UserWrapper getUserFromName(String name) {
		try {
			return wrap(User.getUserFromUserName(name));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public UserWrapper getUserFromEmailOrName(String name) {
		UserWrapper toRet = getUserFromEmail(name);
		if (toRet == null)
			toRet = getUserFromName(name);
		return toRet;
	}

}
