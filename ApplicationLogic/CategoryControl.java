/**
 * 
 */
package ApplicationLogic;

import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import DatabaseFrontend.Category;
import DatabaseFrontend.Category;

public class CategoryControl {
	private Control c;

	public CategoryControl(Control c) {
		this.c = c;
	}

	public Category unwrap(String category) {
		try {
			return Category.getCategoryByName(category);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean renameCategory(String category, String newName) {
		Category c = unwrap(category);
		if (c == null)
			return false;
		try {
			return c.setName(newName);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean changeDescription(String category, String description) {
		Category c = unwrap(category);
		if (c == null)
			return false;
		try {
			return c.setDescription(description);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getDescription(String category) {
		Category c = unwrap(category);
		if (c == null)
			return "ERROR";
		return c.getDescription();
	}

	public Set<String> getValidSubCategories(String category) {
		Set<String> categories = new HashSet();
		Queue<Category> toCheck = new ArrayDeque<>();
		Category c = unwrap(category);
		do {
			if (c != null) {
				categories.add(c.getName());
				try {
					for (Category d : c.getChildCategories())
						toCheck.add(d);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			c = toCheck.poll();
		} while (!toCheck.isEmpty());
		return categories;
	}


	public Set<String> getTopLevelCategories() {
		Set<String> categories = new HashSet();
		try {
			for (Category c : Category.getTopLevelCategories())
				categories.add(c.getName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categories;
	}

	Set<String> wrap(Set<Category> categories) {
		Set<String> toRet = new HashSet<String>();
		for (Category c : categories)
			toRet.add(c.getName());
		return toRet;
	}

	public Set<Category> unwrap(Set<String> categories) {
		Set<Category> toRet = new HashSet<Category>();
		for (String c : categories)
			toRet.add(unwrap(c));
		return toRet;
	}
}