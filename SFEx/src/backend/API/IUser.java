package backend.API;

import shared.API.IItemReadonly;

public interface IUser {
	public boolean getUsername();

	public IItemReadonly[] getItemsSold();

	public IItemReadonly trySellItem(String title, String desc,
			String location, String[] tags);
}
