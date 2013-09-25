package databaseTester;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import databaseBackendInterface.IUser;
import databaseBackendInterface.IUserController;
import databaseBackendInterface.NoSuchUserException;

import fakeDatabase.fakeUserController;

public class FakeUserControllerTester {

	private IUserController c;

	@Before
	public void setUp() throws Exception {
		c = makeUserController();
		c.registerUser(FakeUserTester.FAKE_USER, 
						FakeUserTester.FAKE_PASSHASH, 
						FakeUserTester.FAKE_EMAIL);
	}

	public static IUserController makeUserController() {
		return new fakeUserController();
	}

	@Test
	public void testGetUser() {
		IUser u = null;
		try {
			u = c.getUser(FakeUserTester.FAKE_USER);
		} catch (NoSuchUserException e) {
			fail("No such user found: " + e);
		}
		try {
			u = c.getUser(FakeUserTester.FAKE_USER + "ABC");
			fail("Shouldn't have gotten a user: " + u);
		} catch (NoSuchUserException e) {
			//fallthrough
		}
		assertEquals(FakeUserTester.FAKE_USER, u.getUsername());
		assertEquals(FakeUserTester.FAKE_PASSHASH, u.getPasswordHash());
		assertEquals(FakeUserTester.FAKE_EMAIL, u.getEmail());
	}

	@Test
	public void testGetUsers() {
		int num = 0;
		IUser user = null;
		for (IUser u : c.getUsers()) {
			num++;
			user = u;
		}
		assertEquals(1, num);
		assertEquals(FakeUserTester.FAKE_USER, user.getUsername());
		assertEquals(FakeUserTester.FAKE_PASSHASH, user.getPasswordHash());
		assertEquals(FakeUserTester.FAKE_EMAIL, user.getEmail());
	}

	@Test
	public void testRegisterUser() {
		c.registerUser(FakeUserTester.FAKE_USER + "ABC", 
				FakeUserTester.FAKE_PASSHASH + "ABC", 
				FakeUserTester.FAKE_EMAIL + "ABC");
		IUser u = null;
		try {
			u = c.getUser(FakeUserTester.FAKE_USER + "ABC");
		} catch (NoSuchUserException e) {
			fail("No such user found: " + e);
		}
		assertEquals(FakeUserTester.FAKE_USER + "ABC", u.getUsername());
		assertEquals(FakeUserTester.FAKE_PASSHASH + "ABC", u.getPasswordHash());
		assertEquals(FakeUserTester.FAKE_EMAIL + "ABC", u.getEmail());
	}

}
