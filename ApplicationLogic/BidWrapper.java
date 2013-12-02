/**
 * 
 */
package ApplicationLogic;

import static ApplicationLogic.ItemWrapper.*;
import static ApplicationLogic.UserWrapper.*;

import java.sql.SQLException;
import java.util.Set;

import DatabaseFrontend.Bid;

public class BidWrapper {
	
	private final Bid bid;
	private final Control control;

	public long getPriceInCents() {
		return bid.getPriceInCents();
	}

	public UserWrapper getUser() {
		try {
			return control.userControl.wrap(bid.getUser());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ReadonlyItemWrapper getItem() {
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
}