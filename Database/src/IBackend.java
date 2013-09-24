


public interface IBackend {
	public int getUserID(String userName);
	public Iterable<IUser> getUsers();
	public IUser getUser(int userID);
	public IUser registerUser(String username, String passwordHash, String email);
	public Iterable<IItem> getItems();
	public IItem registerItem(int userID);
}
