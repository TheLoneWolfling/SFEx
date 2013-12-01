package DatabaseFrontend;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;

public class ResultSet {

	private final Set<Category> categories;

	private final Set<Keyword> keywords;

	private final Set<Location> locations;

	private final long maxPrice;

	private final long minPrice;

	private final Set<String> text;

	public ResultSet(final Set<Location> locations,
			final Set<Keyword> keywords, final Set<Category> categories,
			final long minPrice, final long maxPrice, final Set<String> text) {
		this.locations = Collections.unmodifiableSet(locations);
		this.keywords = Collections.unmodifiableSet(keywords);
		this.categories = Collections.unmodifiableSet(categories);
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.text = Collections.unmodifiableSet(text);
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public Set<Item> getItems() throws SQLException {
		return Item.getItemsBy(locations, keywords, categories, minPrice,
				maxPrice, text);
	}

	public Set<Keyword> getKeywords() {
		return keywords;
	}

	public Set<Location> getLocations() {
		return locations;
	}

	public long getMaxPrice() {
		return maxPrice;
	}

	public long getMinPrice() {
		return minPrice;
	}

	public Set<String> getText() {
		return text;
	}

}