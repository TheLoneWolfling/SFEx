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
		registerDefaultItem(c, u);
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
		
		FakeItemTester.assertItemDefault(item, u);
	}

	@Test
	public void testRegisterItem() {
		registerDefaultItem(c, u);
		for (IItem i : c.getItems()) {
			FakeItemTester.assertItemDefault(i, u);
		}
	}

	@Test
	public void testCleanTagRelations() {
		c.cleanTagRelations();
	}

	@Test
	public void testGetItemsWithTag() throws NoSuchTagException {
		for (String t : FakeItemTester.tags) {
			int num = 0;
			IItem item = null;
			for (IItem i : c.getItemsWithTag(t)) {
				num++;
				item = i;
			}
			assertEquals(t, 1, num);
			FakeItemTester.assertItemDefault(item, u);

			
		}
	}

	static void registerDefaultItem(IItemController c, IUser u) {
		try {
			c.registerItem(FakeItemTester.FAKE_ITEM_NAME, 
					FakeItemTester.FAKE_ITEM_DESCRIPTION,
					FakeItemTester.FAKE_ITEM_LOCATION,
					new ArrayList<String>(Arrays.asList(FakeItemTester.tags)),
					FakeItemTester.FAKE_ITEM_COST,
					u);
		} catch (NullTagsException e) {
			fail("Null tags?");
		} catch (NoSuchUserException e) {
			fail ("No such user?");
		}
	}

	public static IItemController makeItemController() {
		return new fakeItemController();
	}

}
