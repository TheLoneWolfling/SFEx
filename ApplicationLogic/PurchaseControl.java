/**
 * 
 */
package ApplicationLogic;

public class PurchaseControl {
	
	public final Control p;

	public PurchaseControl(Control p) {
		this.p = p;
	}

	public boolean tryBuyNow(ItemWrapper item) {
		return true;
	}

	public BidWrapper makeBid(ItemWrapper item, long amountInCents) {
		return null;
	}
}