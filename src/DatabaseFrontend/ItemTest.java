package DatabaseFrontend;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ItemTest {
	private CommonTesting c;

	@Before
	public void setUp() throws Exception {
		c = new CommonTesting();
	}

	@After
	public void tearDown() throws Exception {
		c.del();
	}

	@Test
	public void testGetItemFromID() throws SQLException {
		assertEquals(c.i, Item.getItemFromID(c.i.getID()));
	}

	@Test
	public void testGetItemsBy() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetItemsByCategory() throws SQLException {
		Set<Item> items = Item.getItemsByCategory(c.c);
		assertNotNull(items);
		assertTrue(items.contains(c.i));
	}

	@Test
	public void testGetItemsByName() throws SQLException {
		Set<Item> items = Item.getItemsByName("NameTESTING1234");
		assertNotNull(items);
		assertTrue(items.contains(c.i));
	}

	@Test
	public void testGetItemsByKeyword() throws SQLException {
		Set<Item> items = Item.getItemsByKeyword(c.k);
		assertNotNull(items);
		assertTrue(items.contains(c.i));
	}

	@Test
	public void testGetItemsByLocation() throws SQLException {
		Set<Item> items = Item.getItemsByLocation(c.l);
		assertNotNull(items);
		assertTrue(items.contains(c.i));
	}

	@Test
	public void testGetItemsByPrice() throws SQLException {
		Set<Item> items = Item.getItemsByPrice(0, 1);
		assertNotNull(items);
		assertTrue(items.contains(c.i));
	}

	@Test
	public void testGetItemsByUserSelling() throws SQLException {
		Set<Item> items = Item.getItemsByUserSelling(c.u);
		assertNotNull(items);
		assertTrue(items.contains(c.i));
	}

	@Test
	public void testMakeItem() throws SQLException {
		Item j = Item.makeItem(30, "NameTESTING123456", 2, c.u, "DescriptionTESTING123456");
		assertNotNull(j);
	}

	@Test
	public void testDeleteItem() throws SQLException {
		c.i.deleteItem();
		assertNull(Item.getItemFromID(c.i.getID()));
	}

	@Test
	public void testGetBids() throws SQLException {
		assertTrue(c.i.getBids().contains(c.b));
	}

	@Test
	public void testGetBuyNowPriceInCents() {
		assertEquals(1, c.i.getBuyNowPriceInCents());
	}

	@Test
	public void testGetCategory() throws SQLException {
		assertTrue(c.i.getCategory().contains(c.c));
	}

	@Test
	public void testGetName() {
		assertEquals("NameTESTING1234", c.i.getName());
	}

	@Test
	public void testGetDescription() {
		assertEquals("descriptionTESTING1234", c.i.getDescription());
	}

	@Test
	public void testGetID() {
		//Cannot test?
	}

	@Test
	public void testGetKeywords() throws SQLException {
		assertTrue(c.i.getKeywords().contains(c.k));
	}

	@Test
	public void testGetLocation() throws SQLException {
		assertTrue(c.i.getLocation().contains(c.l));
	}

	@Test
	public void testGetSeller() throws SQLException {
		assertEquals(c.u, c.i.getSeller());
	}

	@Test
	public void testGetSoldToUser() throws SQLException {
		assertNull(c.i.getSoldToUser());
	}

	@Test
	public void testSetBuyNowPriceInCents() throws SQLException {
		assertTrue(c.i.setBuyNowPriceInCents(50));
		assertEquals(50, c.i.getBuyNowPriceInCents());
	}

	@Test
	public void testSetName() throws SQLException {
		assertTrue(c.i.setName("NameTESTING123456"));
		assertEquals("NameTESTING123456", c.i.getName());
	}

	@Test
	public void testSetDescription() throws SQLException {
		assertTrue(c.i.setDescription("DescriptionTESTING123456"));
		assertEquals("DescriptionTESTING123456", c.i.getDescription());
	}

	@Test
	public void testSetSoldToUser() throws SQLException {
		assertTrue(c.i.setSoldToUser(c.u));
		assertEquals(c.u, c.i.getSoldToUser());
	}

	@Test
	public void testSetCurrentPriceInCents() throws SQLException {
		assertTrue(c.i.setCurrentPriceInCents(40));
		assertEquals(40, c.i.getCurrentPriceInCents());
	}

	@Test
	public void testAddKeyword() throws SQLException {
		c.i.delKeyword(c.k);
		assertTrue(c.i.addKeyword(c.k));
		assertTrue(c.i.getKeywords().contains(c.k));
	}

	@Test
	public void testAddCategory() throws SQLException {
		c.i.delCategory(c.c);
		assertTrue(c.i.addCategory(c.c));
		assertTrue(c.i.getCategory().contains(c.c));
	}

	@Test
	public void testAddLocation() throws SQLException {
		c.i.delLocation(c.l);
		assertTrue(c.i.addLocation(c.l));
		assertTrue(c.i.getLocation().contains(c.l));
	}

	@Test
	public void testDelKeyword() throws SQLException {
		assertTrue(c.i.delKeyword(c.k));
		assertFalse(c.i.getKeywords().contains(c.k));
	}

	@Test
	public void testDelLocation() throws SQLException {
		assertTrue(c.i.delLocation(c.l));
		assertFalse(c.i.getLocation().contains(c.l));
	}

	@Test
	public void testDelCategory() throws SQLException {
		assertTrue(c.i.delCategory(c.c));
		assertFalse(c.i.getCategory().contains(c.c));
	}

}
