package DatabaseFrontend;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ApplicationLogic.AccountControl;

public class UserTest {
	User u;
	Item i;
	Bid b;

	@Before
	public void setUp() throws Exception {
		
		User v = User.getUserFromEmail("emailTESTING12345");
		if (v != null)
			v.deleteUser();
		u = User.getUserFromEmail("emailTESTING1234");
		if (u == null)
			u = User.makeUser("emailTESTING1234", "userNameTESTING1234",
					"contactInfoTESTING1234", "passwordHashTESTING1234",
					EnumSet.allOf(Permission.class), "saltTESTING1234");
		try {
			i = Item.getItemsByUserSelling(u).iterator().next();
		} catch (NoSuchElementException e) {
		}
		if (i == null)
			i = Item.makeItem(1, "NameTESTING1234", 0, u,
					"descriptionTESTING1234");
		if (b == null)
			b = Bid.makeBid(u, i, 20);
		try {
			b = Bid.getBidsByUser(u).iterator().next();
		} catch (NoSuchElementException e) {
		}
	}

	@After
	public void tearDown() throws Exception {
		if (i != null)
			i.deleteItem();
		if (u != null)
			u.deleteUser();
		User v = User.getUserFromEmail("emailTESTING12345");
		if (v != null)
			v.deleteUser();
	}

	@Test
	public void testGetUserFromEmail() throws SQLException {
		assertEquals(u, User.getUserFromEmail(u.getEmail()));
	}

	@Test
	public void testGetUserFromID() throws SQLException {
		assertEquals(u, User.getUserFromID(u.getID()));
	}

	@Test
	public void testGetUserFromUserName() throws SQLException {
		assertEquals(u, User.getUserFromUserName(u.getUserName()));
	}

	@Test
	public void testMakeUser() throws SQLException {
		User v = User.makeUser("emailTESTING12345", "userNameTESTING12345",
				"contactInfoTESTING12345", "passwordHashTESTING12345",
				EnumSet.allOf(Permission.class), "saltTESTING12345");
		assertNotNull(v);
	}

	@Test
	public void testDeletePermission() throws SQLException {
		for (Permission p : Permission.values()) {
			u.deletePermission(p);
			assertFalse(u.getPermissions().contains(p));
		}
	}

	@Test
	public void testDeleteUser() throws SQLException {
		assertTrue(u.deleteUser());
		assertNull(User.getUserFromEmail(u.getEmail()));
		assertNull(User.getUserFromID(u.getID()));
		assertNull(User.getUserFromUserName(u.getUserName()));
	}

	@Test
	public void testGetBidsMade() throws SQLException {
		Set<Bid> bids = u.getBidsMade();
		assertNotNull(bids);
		assertTrue(bids.contains(b));
		assertTrue(bids.size() == 1);
	}

	@Test
	public void testGetContactInfo() {
		assertEquals("contactInfoTESTING1234", u.getContactInfo());
	}

	@Test
	public void testGetEmail() {
		assertEquals("emailTESTING1234", u.getEmail());
	}

	@Test
	public void testGetID() {
		// No way to test?
	}

	@Test
	public void testGetItemsSelling() throws SQLException {
		Set<Item> items = u.getItemsSelling();
		assertNotNull(items);
		assertTrue(items.contains(i));
		assertTrue(items.size() == 1);
	}

	@Test
	public void testGetPasswordHash() {
		assertEquals("passwordHashTESTING1234", u.getPasswordHash());
	}

	@Test
	public void testGetPermissions() {
		assertEquals(EnumSet.allOf(Permission.class), u.getPermissions());
	}

	@Test
	public void testGetSalt() {
		assertEquals("saltTESTING1234", u.getSalt());
	}

	@Test
	public void testGetUserName() {
		assertEquals("userNameTESTING1234", u.getUserName());
	}

	@Test
	public void testSetContactInfo() throws SQLException {
		assertTrue(u.setContactInfo("changedContactInfoTESTING1234"));
		assertEquals("changedContactInfoTESTING1234", u.getContactInfo());
	}

	@Test
	public void testSetEmail() throws SQLException {
		assertTrue(u.setEmail("emailTESTING12345"));
		assertEquals("emailTESTING12345", u.getEmail());
	}

	@Test
	public void testSetPasswordHash() throws SQLException {
		assertTrue(u.setPasswordHash("changedHashTESTING1234"));
		assertEquals("changedHashTESTING1234", u.getPasswordHash());
	}

	@Test
	public void testSetPermission() throws SQLException {
		for (Permission p : Permission.values()) {
			u.deletePermission(p);
			u.setPermission(p);
			assertTrue(u.getPermissions().contains(p));
		}
	}

	@Test
	public void testSetUserName() throws SQLException {
		assertTrue(u.setUserName("changedUserNameTESTING1234"));
		assertEquals("changedUserNameTESTING1234", u.getUserName());
	}

}
