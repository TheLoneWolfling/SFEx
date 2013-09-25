package databaseBackendInterface;

public interface IItem {
	public String getTitle();
	public void setTitle(String title);
	public String getDescription();
	public void setDescription(String description);
	public String getLocation();
	public void setLocation(String location);
	public String[] getTags();
	public boolean hasTag(String tag);
	// Return true if tag was already added
	public boolean addTag(String tag);
	public boolean removeTag(String tag) throws NoSuchTagException;
	public int getPriceInCents();
	public void setPriceInCents(int price);
	public boolean beenBought();
	public void setBought();
	public IUser getSeller();
}
