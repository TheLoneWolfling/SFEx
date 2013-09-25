package databaseTester;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import databaseBackendInterface.IItem;
import databaseBackendInterface.IItemController;
import databaseBackendInterface.IUser;
import databaseBackendInterface.NoSuchTagException;
import databaseBackendInterface.NoSuchUserException;
import databaseBackendInterface.NullTagsException;

import fakeDatabase.fakeItem;
import fakeDatabase.fakeItemController;

public class FakeItemControllerTester {

	private IItemController c;
	private IUser u;

	@Before
	public void setUp() throws Exception {
		c = makeItemController();
		u = FakeUserTester.makeUser();
		c.registerItem(FakeItemTester.FAKE_ITEM_NAME, 
						FakeItemTester.FAKE_ITEM_DESCRIPTION,
						FakeItemTester.FAKE_ITEM_LOCATION,
						new ArrayList<String>(Arrays.asList(FakeItemTester.tags)),
						FakeItemTester.FAKE_ITEM_COST,
						u);
	}

	@Test
	public void testGetItems() {
		int num = 0;
		IItem item = null;
		for (IItem i : c.getItems()) {
			num++;
			item = i;
		}
		assertEquals(1, num);
		
		assertEquals(FakeItemTester.FAKE_ITEM_NAME, item.getTitle());
		assertEquals(FakeItemTester.FAKE_ITEM_DESCRIPTION, item.getDescription());
		assertEquals(FakeItemTester.FAKE_ITEM_LOCATION, item.getLocation());
		assertArrayEquals(FakeItemTester.tags, item.getTags());
		assertEquals(FakeItemTester.FAKE_ITEM_COST, item.getPriceInCents());
		assertEquals(u, item.getSeller());
	}

	@Test
	public void testRegisterItem() {
		try {
			c.registerItem(FakeItemTester.FAKE_ITEM_NAME, 
					FakeItemTester.FAKE_ITEM_DESCRIPTION,
					FakeItemTester.FAKE_ITEM_LOCATION,
					new ArrayList<String>(Arrays.asList(FakeItemTester.tags)),
					FakeItemTester.FAKE_ITEM_COST,
					u);
		} catch (NullTagsException e) {
			fail ("Null tags?");
		} catch (NoSuchUserException e) {
			fail ("No such user?");
		}
		for (IItem i : c.getItems()) {
			assertEquals(FakeItemTester.FAKE_ITEM_NAME, i.getTitle());
			assertEquals(FakeItemTester.FAKE_ITEM_DESCRIPTION, i.getDescription());
			assertEquals(FakeItemTester.FAKE_ITEM_LOCATION, i.getLocation());
			assertArrayEquals(FakeItemTester.tags, i.getTags());
			assertEquals(FakeItemTester.FAKE_ITEM_COST, i.getPriceInCents());
			assertEquals(u, i.getSeller());
		}
	}

	@Test
	public void testCleanTagRelations() {
		c.cleanTagRelations();
	}

	@Test
	public void testGetItemsWithTag() {
		for (String t : FakeItemTester.tags) {
			int num = 0;
			IItem item = null;
			try {
				for (IItem i : c.getItemsWithTag(t)) {
					num++;
					item = i;
				}
				assertEquals(t, 1, num);
				
				assertEquals(FakeItemTester.FAKE_ITEM_NAME, item.getTitle());
				assertEquals(FakeItemTester.FAKE_ITEM_DESCRIPTION, item.getDescription());
				assertEquals(FakeItemTester.FAKE_ITEM_LOCATION, item.getLocation());
				assertArrayEquals(FakeItemTester.tags, item.getTags());
				assertEquals(FakeItemTester.FAKE_ITEM_COST, item.getPriceInCents());
				assertEquals(u, item.getSeller());
			} catch (NoSuchTagException e) {
				fail("No such tag: " + e);
			}
			
		}
	}

	public static IItemController makeItemController() {
		return new fakeItemController();
	}

}
