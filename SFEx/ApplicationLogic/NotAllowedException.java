package ApplicationLogic;

import DatabaseFrontend.Permission;

public class NotAllowedException extends Exception {

	public NotAllowedException(Permission p) {
		super(p.toString());
	}

}
