package shared.API;

public interface IItemWriteonly {

	public void setTitle(String title);
	public void setDescription(String description);
	public void setLocation(String location);
	// Return true if tag was already added
	public boolean addTag(String tag);
	// Returns true if tag existed beforehand
	public boolean removeTag(String tag);
	public void setPriceInCents(int price);
	public void setBought();
}
