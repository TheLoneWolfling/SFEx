package databaseBackendInterface;

public interface IUser {
	public String getUsername();
	public String getPasswordHash();
	public String getEmail();
	public Iterable<IItem> getItems();
	public void refresh();
	public void populate();
}
