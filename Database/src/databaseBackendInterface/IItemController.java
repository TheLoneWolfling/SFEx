package databaseBackendInterface;

import java.util.ArrayList;

public interface IItemController {

	public Iterable<IItem> getItems();

	public IItem registerItem(String title, String description, String location, ArrayList<String> tags, int price, IUser user) throws NullTagsException, NoSuchUserException;
	
	// Scan through and remove any tag IDs that don't have any associated items
	public void cleanTagRelations();
	

	public Iterable<IItem> getItemsWithTag(String tag) throws NoSuchTagException;

}