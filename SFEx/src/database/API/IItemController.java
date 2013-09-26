package database.API;

import java.util.ArrayList;

import shared.API.IItem;
import shared.API.IItemReadonly;

public interface IItemController {

	public Iterable<IItem> getItems();

	public IItemReadonly registerItem(String title, String description,
			String location, ArrayList<String> tags, int price, IUser user)
			throws NullTagsException, NoSuchUserException;

	// Scan through and remove any tag IDs that don't have any associated items
	public void cleanTagRelations();

	public Iterable<IItem> getItemsWithTag(String tag)
			throws NoSuchTagException;

}
