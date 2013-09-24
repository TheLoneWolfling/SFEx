
public interface IItemController {

	public Iterable<IItem> getItems();

	public IItem registerItem(int userID);
	
	// Scan through and remove any tag IDs that don't have any associated items
	public void cleanTagRelations();

}
