package databaseTester;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import databaseBackendInterface.IItem;
import databaseBackendInterface.IItemController;
import databaseBackendInterface.IUser;
import databaseBackendInterface.NoSuchUserException;
import databaseBackendInterface.NullTagsException;
import fakeDatabase.fakeUser;

public class FakeUserTester {

	public static final String FAKE_EMAIL = "test@te.st";
	public static final String FAKE_PASSHASH = "12345";
	public static final String FAKE_USER = "Fake User";
	private IUser u;

	@Before
	public void setUp() throws Exception {
		u = makeUser();
	}

	@Test
	public void testGetUsername() {
		assertEquals(FAKE_USER, u.getUsername());
	}

	@Test
	public void testGetPasswordHash() {
		assertEquals(FAKE_PASSHASH, u.getPasswordHash());
	}

	@Test
	public void testGetEmail() {
		assertEquals(FAKE_EMAIL, u.getEmail());
	}

	@Test
	public void testGetItems() {
		IItemController c = FakeItemControllerTester.makeItemController();
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
		int num = 0;
		IItem item = null;
		for (IItem d : u.getItems()) {
			num++;
			item = d;
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
	public void testRefresh() {
		u.refresh();
	}

	@Test
	public void testPopulate() {
		u.populate();
	}

	public static IUser makeUser() {
		return new fakeUser(FAKE_USER, FAKE_PASSHASH, FAKE_EMAIL);
	}

}
