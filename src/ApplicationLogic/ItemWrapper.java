/**
 * 
 */
package ApplicationLogic;

import java.sql.SQLException;

import DatabaseFrontend.Item;
import DatabaseFrontend.Keyword;
import DatabaseFrontend.Permission;


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
		if (!control.accountControl.isLoggedInUserAllowed(getSeller(), Permission.EditOwnItems, Permission.EditOtherItems))
			return false;
		try {
			return item.setBuyNowPriceInCents(buyNowPriceInCents);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setDescription(String description) {
		if (!control.accountControl.isLoggedInUserAllowed(getSeller(), Permission.EditOwnItems, Permission.EditOtherItems))
			return false;
		try {
			return item.setDescription(description);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean delKeyword(String keyword) {
		if (!control.accountControl.isLoggedInUserAllowed(getSeller(), Permission.EditOwnItems, Permission.EditOtherItems))
			return false;
		Keyword k = control.keywordControl.unwrap(keyword);
		try {
			if (!item.delKeyword(k))
				return false;
			k.decPopularity(); //Todo: should be atomic
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addKeyword(String keyword) {
		if (!control.accountControl.isLoggedInUserAllowed(getSeller(), Permission.EditOwnItems, Permission.EditOtherItems))
			return false;
		Keyword k = control.keywordControl.unwrap(keyword);
		try {
			if (!item.addKeyword(k))
				return false;
			k.incPopularity(); //Todo: should be atomic
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public boolean setSoldToUser(UserWrapper user) {
		if (!control.accountControl.isLoggedInUserAllowed(getSeller(), Permission.MakeBid, Permission.EditOtherUsers))
			return false;
		try {
			item.setCurrentPriceInCents(item.getBuyNowPriceInCents());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		try {
			return item.setSoldToUser(user.getUser());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public long getCurrentPrice() {
		return item.getCurrentPriceInCents();
	}
}