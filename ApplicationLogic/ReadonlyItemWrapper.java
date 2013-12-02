/**
 * 
 */
package ApplicationLogic;

import DatabaseFrontend.Item;
import java.sql.SQLException;
import java.util.Set;


public class ReadonlyItemWrapper {

	protected final Item item;
	protected final Control control;


	protected ReadonlyItemWrapper(Item i, Control control) {
		this.item = i;
		this.control = control;
	}

	public long getBuyNowPriceInCents() {
		return item.getBuyNowPriceInCents();
	}

	public Set<BidWrapper> getBids() {
		try {
			return control.bidControl.wrap(item.getBids());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Set<String> getLocation() {
		try {
			return control.locationControl.wrap(item.getLocation());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Set<String> getCategory() {
		try {
			return control.categoryControl.wrap(item.getCategory());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Set<String> getKeywords() {
		try {
			return control.keywordControl.wrap(item.getKeywords());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getDescription() {
		return item.getDescription();
	}

	public UserWrapper getSeller() {
		try {
			return control.userControl.wrap(item.getSeller());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public UserWrapper getSoldToUser() {
		try {
			return control.userControl.wrap(item.getSoldToUser());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}