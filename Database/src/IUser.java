
public interface IUser {
	public String getUsername();
	public String getPasswordHash();
	public String getEmail();
	public int[] getItems();
	public void refresh();
	public void populate();
}
