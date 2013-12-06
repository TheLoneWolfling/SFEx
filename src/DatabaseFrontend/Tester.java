package DatabaseFrontend;

import java.sql.SQLException;
import java.util.Collections;
import java.util.EnumSet;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import ApplicationLogic.AccountControl;

public class Tester {
	
	
	

	public static void main(String[] args) throws SQLException {
		final String salt = AccountControl.makeSalt();
		final EnumSet<Permission> allpermissions = EnumSet
				.allOf(Permission.class);		
		
		User.makeUser("email", "userName", "contactInfo", AccountControl.hash("abcd", salt), allpermissions, salt);
		if ("".isEmpty())
			return;
	}
}
