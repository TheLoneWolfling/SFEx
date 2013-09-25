package databaseTester;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseBackendInterface.IUser;
import fakeDatabase.fakeUser;

public class FakeUserTester {

	public static final String FAKE_EMAIL = "test@te.st";
	public static final String FAKE_PASSHASH = "12345";
	public static final String FAKE_USER = "Fake User";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFakeUser() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetUsername() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetPasswordHash() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetEmail() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetItems() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRefresh() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testPopulate() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testRegister() {
		fail("Not yet implemented"); // TODO
	}

	public static IUser makeUser() {
		// TODO Auto-generated method stub
		return new fakeUser(FAKE_USER, FAKE_PASSHASH, FAKE_EMAIL);
	}

}
