package DatabaseFrontend;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CategoryTest {
	User u;
	Item i;
	Category c;
	Category d;

	@Before
	public void setUp() throws Exception {
		u = User.getUserFromEmail("emailTESTING1234");
		if (u != null)
			u.deleteUser();
		u = User.makeUser("emailTESTING1234", "userNameTESTING1234",
					"contactInfoTESTING1234", "passwordHashTESTING1234",
					EnumSet.allOf(Permission.class), "saltTESTING1234");
		try {
			i = Item.getItemsByUserSelling(u).iterator().next();
		} catch (NoSuchElementException e) {
		}
		if (i != null)
			i.deleteItem();
		i = Item.makeItem(1, "NameTESTING1234", 0, u,
					"descriptionTESTING1234");
		c = Category.getCategoryByName("CategoryTESTING1234");
		if (c != null)
			c.deleteCategory();
		c = Category.makeCategory("CategoryTESTING1234", null, "descriptionTESTING1234");
		i.addCategory(c);
		d = Category.getCategoryByName("CategoryTESTING12345");
		if (d != null)
			d.deleteCategory();
		d = Category.makeCategory("CategoryTESTING12345", c, "descriptionTESTING12345");
		d.setParent(c);
		Category e = Category.getCategoryByName("CategoryTESTING123456");
		if (e != null)
			e.deleteCategory();
		
	}

	@After
	public void tearDown() throws Exception {
		if (i != null)
			i.deleteItem();
		if (u != null)
			u.deleteUser();
		if (c != null)
			c.deleteCategory();
		if (d != null)
			d.deleteCategory();
	}

	@Test
	public void testGetCategoryByID() throws SQLException {
		assertEquals(c, Category.getCategoryByID(c.getID()));
	}

	@Test
	public void testGetCategoryByName() throws SQLException {
		assertEquals(c, Category.getCategoryByName(c.getName()));
	}

	@Test
	public void testGetCategoryForItem() throws SQLException {
		Set<Category> s = Category.getCategoryForItem(i);
		assertTrue(s.contains(c));
		assertEquals(1, s.size());
	}

	@Test
	public void testGetTopLevelCategories() throws SQLException {
		Set<Category> ret = Category.getTopLevelCategories();
		assertTrue(ret.contains(c));
		assertFalse(ret.contains(d));
	}

	@Test
	public void testMakeCategory() throws SQLException {
		Category f = Category.makeCategory("CategoryTESTING123456", d, "descriptionTESTING123456");
		assertNotNull(f);
	}

	@Test
	public void testDeleteCategory() throws SQLException {
		d.deleteCategory();
		assertNull(Category.getCategoryByID(d.getID()));
	}

	@Test
	public void testGetDescription() {
		assertEquals("descriptionTESTING1234", c.getDescription());
	}

	@Test
	public void testGetID() {
		//Cannot test?
	}

	@Test
	public void testGetItems() throws SQLException {
		Set<Item> items = c.getItems();
		assertNotNull(items);
		assertTrue(items.contains(i));
		assertTrue(items.size() == 1);
	}

	@Test
	public void testGetName() {
		assertEquals("CategoryTESTING1234", c.getName());
	}

	@Test
	public void testGetParent() throws SQLException {
		assertEquals(c, d.getParent());
	}

	@Test
	public void testSetDescription() throws SQLException {
		assertTrue(c.setDescription("newDescriptionTESTING1234"));
		assertEquals("newDescriptionTESTING1234", c.getDescription());
	}

	@Test
	public void testSetName() throws SQLException {
		assertTrue(c.setName("CategoryTESTING123456"));
		assertEquals("CategoryTESTING123456", c.getName());
	}

	@Test
	public void testSetParent() throws SQLException {
		assertFalse(c.setParent(d));
		assertNull(c.getParent());
		assertTrue(Category.getTopLevelCategories().contains(c));
		assertTrue(d.setParent(null));
		assertNull(c.getParent());
		assertTrue(Category.getTopLevelCategories().contains(d));
		assertTrue(c.setParent(d));
		assertFalse(Category.getTopLevelCategories().contains(c));
		assertTrue(d.getChildCategories().contains(c));
		assertEquals(d, c.getParent());
		assertNull(d.getParent());
	}

	@Test
	public void testGetChildCategories() throws SQLException {
		assertTrue(c.getChildCategories().contains(d));
		assertTrue(d.getChildCategories().isEmpty());
	}

}
