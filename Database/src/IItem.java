
public interface IItem {
	public String getTitle();
	public void setTitle(String title);
	public String getDescription();
	public void setDescription(String description);
	public String getLocation();
	public void setLocation(String location);
	public String[] getTags();
	// Return true if tag was already added
	public boolean addTag(String tag);
	public boolean removeTag(String tag);
	public int getPriceInCents();
	public boolean beenBought();
	public void setBought();
	public int getSellerID();
}
