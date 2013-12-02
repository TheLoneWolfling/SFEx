/**
 * 
 */
package ApplicationLogic;

import javax.servlet.http.HttpSession;

public class Control {
	public final AccountControl accountControl;
	public final ItemControl itemControl;
	public final PurchaseControl purchaseControl;
	public final LocationControl locationControl;
	public final KeywordControl keywordControl;
	public final CategoryControl categoryControl;
	public final SearchControl searchControl;
	public final BidControl bidControl;
	public final UserControl userControl;

	public Control(HttpSession session) {
		accountControl = new AccountControl(session, this);
		itemControl = new ItemControl(this);
		purchaseControl = new PurchaseControl(this);
		locationControl = new LocationControl(this);
		keywordControl = new KeywordControl(this);
		categoryControl = new CategoryControl(this);
		searchControl = new SearchControl(this);
		bidControl = new BidControl(this);
		userControl = new UserControl(this);
	}
}