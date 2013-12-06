/**
 * 
 */
package ApplicationLogic;

import static ApplicationLogic.ItemWrapper.*;
import static ApplicationLogic.UserWrapper.*;

import java.sql.SQLException;
import java.util.Set;

import DatabaseFrontend.Bid;
import DatabaseFrontend.Permission;

public class BidWrapper {
	
	private final Bid bid;
	private final Control control;

	public long getPriceInCents() {
		return bid.getPriceInCents();
	}

	public UserWrapper getUser() {
		if (!control.accountControl.isLoggedInUserAllowed(Permission.ViewBids))
			return null;
		try {
			return control.userControl.wrap(bid.getUser());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ReadonlyItemWrapper getItem() {
		if (!control.accountControl.isLoggedInUserAllowed(Permission.ViewBids))
			return null;
		try {
			return control.itemControl.wrapReadOnly(bid.getItem());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public BidWrapper(Bid b, Control control) {
		this.bid = b;
		this.control = control;
	}

	public boolean delete() {
		try {
			return bid.deleteBid();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}