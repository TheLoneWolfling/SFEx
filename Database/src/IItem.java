
public interface IItem {
	public int getID();
	public String getTitle();
	public void setTitle(String title);
	public String getDescription();
	public void setDescription(String description);
	public String getLocation();
	public void setLocation(String location);
	public String[] getTags();
	public void setTags();
	public int getPriceInCents();
	public boolean beenBought();
	public void setBought();
	public int getSellerID();
}
