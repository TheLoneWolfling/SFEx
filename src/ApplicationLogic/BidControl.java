package ApplicationLogic;

import java.util.HashSet;
import java.util.Set;

import DatabaseFrontend.Bid;
import DatabaseFrontend.Item;

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

}
