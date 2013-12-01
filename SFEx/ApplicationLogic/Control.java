/**
 * 
 */
package ApplicationLogic;

public class Control {
	public final AccountControl accountControl;
	public final ItemControl itemControl;
	public final PurchaseControl purchaseControl;
	public final LocationControl locationControl;
	public final KeywordControl keywordControl;
	public final CategoryControl categoryControl;

	public Control() {
		accountControl = new AccountControl();
		itemControl = new ItemControl();
		purchaseControl = new PurchaseControl();
		locationControl = new LocationControl();
		keywordControl = new KeywordControl();
		categoryControl = new CategoryControl();
	}
}