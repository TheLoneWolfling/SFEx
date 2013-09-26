package database.API;

public interface IUserController {
	public IUser getUser(String username) throws NoSuchUserException;

	public Iterable<IUser> getUsers();

	public IUser registerUser(String username, String passwordHash, String email);
}
