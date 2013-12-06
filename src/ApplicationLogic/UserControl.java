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
			User u = User.getUserFromEmail(email);
			if (u == null) 
				return null;
			return wrap(u);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	UserWrapper wrap(User user) {
		if (user == null)
			return null;
		return new UserWrapper(user, this.p);
	}

	public UserWrapper getUserFromId(long id) {
		try {
			User u = User.getUserFromID(id);
			if (u == null) 
				return null;
			return wrap(u);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public UserWrapper getUserFromName(String name) {
		try {
			User u = User.getUserFromUserName(name);
			if (u == null) 
				return null;
			return wrap(u);
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
