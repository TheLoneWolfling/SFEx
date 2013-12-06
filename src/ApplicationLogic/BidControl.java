package ApplicationLogic;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import DatabaseFrontend.Bid;
import DatabaseFrontend.Item;
import DatabaseFrontend.Permission;

public class BidControl {
	
	public final Control p;

	public BidControl(Control p) {
		this.p = p;
	}

	public Set<BidWrapper> wrap(Set<Bid> bids) {
		Set<BidWrapper> bw = new HashSet<BidWrapper>();
		for (Bid b : bids)
			bw.add(wrap(b));
		return bw;
	}

	private BidWrapper wrap(Bid b) {
		return new BidWrapper(b, p);
	}

	public BidWrapper makeBid(UserWrapper user, ItemWrapper item,
			long priceInCents) {
		if (priceInCents <= item.getCurrentPrice())
			return null;
		if(priceInCents >= item.getBuyNowPriceInCents())
			return null;
		if (!p.accountControl.isLoggedInUserAllowed(user, Permission.MakeBid, Permission.EditOtherUsers))
			return null;
		try {
			return wrap(Bid.makeBid(user.getUser(), item.item, priceInCents));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

}
