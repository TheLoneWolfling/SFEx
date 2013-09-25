package fakeDatabase;

import java.util.ArrayList;
import java.util.List;

import databaseBackendInterface.IItem;
import databaseBackendInterface.IItemController;
import databaseBackendInterface.IUser;
import databaseBackendInterface.NoSuchTagException;

public class fakeItemController implements IItemController {
	
	private List<IItem> items = new ArrayList<IItem>();

	@Override
	public Iterable<IItem> getItems() {
		return items;
	}

	@Override
	public IItem registerItem(String title, String description, String location, ArrayList<String> tags, int price, IUser user) {
		IItem item = new fakeItem(title, description, location, tags, price, user);
		((fakeUser) user).register(item);
		return item;
	}

	@Override
	public void cleanTagRelations() {
		return;
	}

	@Override
	public Iterable<IItem> getItemsWithTag(String tag)
			throws NoSuchTagException {
		List<IItem> temp = new ArrayList<IItem>();
		for (IItem i : items)
			if (i.hasTag(tag))
				temp.add(i);
		return temp;
	}

}
