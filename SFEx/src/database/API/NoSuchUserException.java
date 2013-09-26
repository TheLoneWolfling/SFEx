package database.API;

public class NoSuchUserException extends Exception {

	public NoSuchUserException(String username) {
		super("No such username: " + username);
	}

	private static final long serialVersionUID = 1L;

}
