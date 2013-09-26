package database.fake;

import java.util.LinkedHashMap;

import database.API.IUser;
import database.API.IUserController;
import database.API.NoSuchUserException;

public class fakeUserController implements IUserController {

	private LinkedHashMap<String, IUser> users = new LinkedHashMap<>();

	@Override
	public IUser getUser(String username) throws NoSuchUserException {
		IUser u = users.get(username);
		if (u == null)
			throw new NoSuchUserException(username);
		return u;
	}

	@Override
	public Iterable<IUser> getUsers() {
		return users.values();
	}

	@Override
	public IUser registerUser(String username, String passwordHash, String email) {
		IUser u = new fakeUser(username, passwordHash, email);
		users.put(username, u);
		return u;
	}

}
