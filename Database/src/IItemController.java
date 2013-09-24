
public interface IItemController {

	public Iterable<IItem> getItems();

	public IItem registerItem(IUser user);
	
	// Scan through and remove any tag IDs that don't have any associated items
	public void cleanTagRelations();
	

	public Iterable<IItem> getItemsWithTag(String tag) throws NoSuchTagException;

}
