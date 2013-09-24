


public interface IUserController {
	public IUser getUser(String userName);
	public Iterable<IUser> getUsers();
	public IUser getUser(int userID);
	public IUser registerUser(String username, String passwordHash, String email);
}
