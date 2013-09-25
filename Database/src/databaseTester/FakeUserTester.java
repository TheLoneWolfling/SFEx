package databaseTester;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import databaseBackendInterface.IItem;
import databaseBackendInterface.IItemController;
import databaseBackendInterface.IUser;
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
		FakeItemControllerTester.registerDefaultItem(c, u);
		int num = 0;
		IItem item = null;
		for (IItem d : u.getItems()) {
			num++;
			item = d;
		}
		assertEquals(1, num);
		FakeItemTester.assertItemDefault(item, u);
	}

	@Test
	public void testRefresh() {
		u.refresh();
	}

	@Test
	public void testPopulate() {
		u.populate();
	}

	static void assertUserDefault(IUser u) {
		assertEquals(FAKE_USER, u.getUsername());
		assertEquals(FAKE_PASSHASH, u.getPasswordHash());
		assertEquals(FAKE_EMAIL, u.getEmail());
	}

	public static IUser makeUser() {
		return new fakeUser(FAKE_USER, FAKE_PASSHASH, FAKE_EMAIL);
	}

}
