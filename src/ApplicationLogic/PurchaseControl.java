/**
 * 
 */
package ApplicationLogic;

import java.sql.SQLException;

public class PurchaseControl {
	
	public final Control p;

	public PurchaseControl(Control p) {
		this.p = p;
	}

	public boolean tryBuyNow(ItemWrapper item) {
		if (!p.accountControl.isLoggedIn())
			return false;
		return item.setSoldToUser(p.accountControl.getLoggedInUser());
	}

	public BidWrapper makeBid(ItemWrapper item, long priceInCents) {
		return p.bidControl.makeBid(p.accountControl.getLoggedInUser(), item, priceInCents);
	}
}