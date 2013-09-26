package shared.API;

import database.API.IUser;

public interface IItemReadonly {
	public String getTitle();
	public String getDescription();
	public String getLocation();
	public String[] getTags();
	public boolean hasTag(String tag);
	public int getPriceInCents();
	public boolean beenBought();
	public IUser getSeller();
}
