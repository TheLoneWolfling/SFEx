/**
 * 
 */
package ApplicationLogic;

import java.sql.SQLException;

import DatabaseFrontend.Item;
import DatabaseFrontend.Keyword;


public class ItemWrapper extends ReadonlyItemWrapper {
	public ItemWrapper(Item i, Control control) {
		super(i, control);
	}
	public boolean delete() {
		try {
			return item.deleteItem();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setBuyNowPriceInCents(long buyNowPriceInCents) {
		try {
			return item.setBuyNowPriceInCents(buyNowPriceInCents);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setDescription(String description) {
		try {
			return item.setDescription(description);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean delKeyword(String keyword) {
		try {
			Keyword k = control.keywordControl.unwrap(keyword);
			return item.delKeyword(k);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean addKeyword(String keyword) {
		try {
			Keyword k = control.keywordControl.unwrap(keyword);
			return item.addKeyword(k);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}