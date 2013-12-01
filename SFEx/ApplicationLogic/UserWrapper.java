/**
 * 
 */
package ApplicationLogic;

import static ApplicationLogic.Control.*;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import DatabaseFrontend.Bid;
import DatabaseFrontend.Item;
import DatabaseFrontend.User;
import DatabaseFrontend.Permission;

public class UserWrapper {
	private final User user;

	public String getEmail() throws NotAllowedException {
		UserWrapper currentUser = AccountControl.getLoggedInUser();
		if (this.user != currentUser.user
				&& !AccountControl.getLoggedInUser().isAllowed(
						Permission.ViewEmail))
			throw new NotAllowedException(Permission.ViewEmail);
		return user.getEmail();
	}

	public String getContactInfo() throws NotAllowedException {
		UserWrapper currentUser = AccountControl.getLoggedInUser();
		if (this.user != currentUser.user
				&& !AccountControl.getLoggedInUser().isAllowed(
						Permission.ViewContactInfo))
			throw new NotAllowedException(Permission.ViewContactInfo);
		return user.getEmail();
	}

	public void setEmail(String email) throws NotAllowedException {
		UserWrapper currentUser = AccountControl.getLoggedInUser();
		if (this.user == currentUser.user) {
			if (!currentUser.isAllowed(Permission.EditOwnUser))
				throw new NotAllowedException(Permission.EditOwnUser);
		} else {
			if (!currentUser.isAllowed(Permission.EditOtherUsers))
				throw new NotAllowedException(Permission.EditOtherUsers);
		}
		user.setEmail(email);
	}

	public Set<ItemWrapper> getItemsSelling() {
		UserWrapper currentUser = AccountControl.getLoggedInUser();
		if (this.user == currentUser.user) {
			if (!currentUser.isAllowed(Permission.EditOwnUser))
				throw new NotAllowedException(Permission.EditOwnUser);
		} else {
			if (!currentUser.isAllowed(Permission.EditOtherUsers))
				throw new NotAllowedException(Permission.EditOtherUsers);
		}
		Set<ItemWrapper> s = new HashSet<ItemWrapper>();

		for (Item i : user.getItemsSelling())
			s.add(new ItemWrapper(i));
		return s;
	}

	public Set<BidWrapper> getBidsMade() {
		UserWrapper currentUser = AccountControl.getLoggedInUser();
		if (this.user != currentUser.user
				&& !currentUser.isAllowed(Permission.ViewBids))
			throw new NotAllowedException(Permission.ViewBids);
		Set<BidWrapper> s = new HashSet<BidWrapper>();
		for (Bid b : user.getBidsMade())
			;
		s.add(new BidWrapper(b));
		return s;
	}

	public void setContactInfo(String contactInfo) {
		UserWrapper currentUser = AccountControl.getLoggedInUser();
		if (this.user == currentUser.user) {
			if (!currentUser.isAllowed(Permission.EditOwnUser))
				throw new NotAllowedException(Permission.EditOwnUser);
		} else {
			if (!currentUser.isAllowed(Permission.EditOtherUsers))
				throw new NotAllowedException(Permission.EditOtherUsers);
		}
		user.setContactInfo(contactInfo);
	}

	public void setPassword(String password) {
		UserWrapper currentUser = AccountControl.getLoggedInUser();
		if (this.user == currentUser.user) {
			if (!currentUser.isAllowed(Permission.EditOwnUser))
				throw new NotAllowedException(Permission.EditOwnUser);
		} else {
			if (!currentUser.isAllowed(Permission.EditOtherUsers))
				throw new NotAllowedException(Permission.EditOtherUsers);
		}
		user.setPassword(AccountControl.hash(password, user.getSalt()));
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param email
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void deleteUser(String email) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param userEmail
	 * @param permission
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPermission(String userEmail, Permission permission) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	public void deleteUser() {
		user.deleteUser();
	}

	public boolean setPermission(Permission permission) throws NotAllowedException, SQLException {
		UserWrapper currentUser = AccountControl.getLoggedInUser();
		if (!currentUser.isAllowed(Permission.EditUserPermissions))
			throw new NotAllowedException(Permission.EditUserPermissions);
		return user.setPermission(permission);
	}

	public boolean isAllowed(Permission permission) {
		return user.getPermissions().contains(permission);
	}

	public boolean deletePermission(Permission permission, String userEmail) throws NotAllowedException, SQLException {
		UserWrapper currentUser = AccountControl.getLoggedInUser();
		if (!currentUser.isAllowed(Permission.EditUserPermissions))
			throw new NotAllowedException(Permission.EditUserPermissions);
		return user.deletePermission(permission);
	}

	public UserWrapper(User user) {
		this.user = user;
	}

	public UserWrapper(long id) throws SQLException {
		this.user = User.getUserFromID(id);
	}
}