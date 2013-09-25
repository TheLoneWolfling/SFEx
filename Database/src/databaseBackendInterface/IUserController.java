package databaseBackendInterface;



public interface IUserController {
	public IUser getUser(String userName) throws NoSuchUserException;
	public Iterable<IUser> getUsers();
	public IUser registerUser(String username, String passwordHash, String email);
}
