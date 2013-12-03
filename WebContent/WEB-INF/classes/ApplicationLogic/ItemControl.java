
package ApplicationLogic;

import static ApplicationLogic.ItemWrapper.*;

import java.util.HashSet;
import java.util.Set;

import DatabaseFrontend.Item;

public class ItemControl {
	
	public final Control p;
	
	public ItemControl(Control p) {
		this.p = p;
	}

	public ItemWrapper newItem(long buyNowPriceInCents, String description) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	public Set<ReadonlyItemWrapper> wrapItemsReadOnly(Set<Item> items) {
		Set<ReadonlyItemWrapper> w = new HashSet();
		for (Item i : items)
			w.add(wrapReadOnly(i));
		return w;
	}

	ReadonlyItemWrapper wrapReadOnly(Item i) {
		return new ReadonlyItemWrapper(i, this.p);
	}
}