/**
 * 
 */
package ApplicationLogic;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;

import DatabaseFrontend.Item;

public class SearchControl {

	public SearchControl(Control control) {
		this.control = control;
	}

	private final Control control;

	public Set<ReadonlyItemWrapper> search(Set<String> names, Set<String> locations, Set<String> keywords,
			Set<String> categories, long minimumPrice, long maximumPrice,
			Set<String> fullText) {
		try {
			return control.itemControl.wrapItemsReadOnly(Item.getItemsBy(names, control.locationControl.unwrap(locations), 
					control.keywordControl.unwrap(keywords),
					control.categoryControl.unwrap(categories),minimumPrice, maximumPrice, fullText));
		} catch (SQLException e) {
			e.printStackTrace();
			return Collections.emptySet();
		}
	}
}