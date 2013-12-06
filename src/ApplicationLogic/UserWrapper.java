/**
 * 
 */
package ApplicationLogic;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import DatabaseFrontend.Bid;
import DatabaseFrontend.Item;
import DatabaseFrontend.User;
import DatabaseFrontend.Permission;

public class UserWrapper {
	private final User user;
	private AccountControl control;

	public String getEmail() {
		if (!control.isLoggedInUserAllowed(this, Permission.ViewEmail))
			return "";
		return user.getEmail();
	}

	public String getContactInfo() {		
		if (!control.isLoggedInUserAllowed(this, Permission.ViewContactInfo))
			return "";
		return user.getContactInfo();
	}

	public boolean setEmail(String email) {
		if (!control.isLoggedInUserAllowed(this, Permission.EditOwnUser, Permission.EditOtherUsers))
			return false;
		if (control.p.userControl.getUserFromEmail(email) != null)
			return false;
		try {
			return user.setEmail(email);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Set<ItemWrapper> getItemsSelling() {
		Set<ItemWrapper> s = new HashSet<ItemWrapper>();
		try {
			for (Item i : user.getItemsSelling())
				s.add(new ItemWrapper(i, control.p));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	public Set<BidWrapper> getBidsMade() {
		if (!control.isLoggedInUserAllowed(this, Permission.ViewBids))
			return Collections.emptySet();
		Set<BidWrapper> s = new HashSet<BidWrapper>();
		try {
			for (Bid b : user.getBidsMade())
				s.add(new BidWrapper(b, control.p));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	public boolean setContactInfo(String contactInfo) {
		if (!control.isLoggedInUserAllowed(this, Permission.EditOwnUser, Permission.EditOtherUsers))
			return false;
		try {
			return user.setContactInfo(contactInfo);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setPassword(String password) {
		if (!control.isLoggedInUserAllowed(this, Permission.EditOwnUser, Permission.EditOtherUsers))
			return false;
		if (password.length() < 8)
			return false;
		try {
			return user.setPasswordHash(AccountControl.hash(password, user.getSalt()));
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteUser() {
		if (!control.isLoggedInUserAllowed(this, Permission.EditOwnUser, Permission.EditOtherUsers))
			return false;
		try {
			if (this == control.p.accountControl.getLoggedInUser())
				control.p.accountControl.logOut();
			for (BidWrapper b : getBidsMade())
				b.delete();
			for (ItemWrapper i : getItemsSelling())
				i.delete();
			for (ItemWrapper i : getItemsSoldTo())
				i.item.setSoldToUser(null);
			return user.deleteUser();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private Set<ItemWrapper> getItemsSoldTo() {
		Set<Item> i = null;
		try {
			i = user.getItemsSoldTo();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (i == null)
			return Collections.emptySet();
		return control.p.itemControl.wrap(i);
	}

	public boolean setPermission(Permission permission) {
		if (!control.isLoggedInUserAllowed(this, Permission.EditUserPermissions, Permission.EditUserPermissions))
			return false;
		try {
			return user.setPermission(permission);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isAllowed(Permission permission) {
		return user.getPermissions().contains(permission);
	}

	public boolean deletePermission(Permission permission, String userEmail) {
		if (!control.isLoggedInUserAllowed(this, Permission.EditUserPermissions, Permission.EditUserPermissions))
			return false;
		try {
			return user.deletePermission(permission);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public UserWrapper(User user, Control control) {
		this.user = user;
		this.control = control.accountControl;
	}

	public boolean checkPass(String password) {
		String hash = user.getPasswordHash();
		return hash.equals(AccountControl.hash(password, user.getSalt()));
	}

	public User getUser() {
		return user;
	}
}