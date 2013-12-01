/**
 * 
 */
package ApplicationLogic;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;

import DatabaseFrontend.Permission;

public class AccountControl {
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	//private DeleteAccount deleteAccount;
	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	//private DeleteOtherAccount deleteOtherAccount;
	//private DeleteAccount deleteAccount;
	//private DeleteOtherAccount deleteOtherAccount;
	private UserWrapper userWrapper;
	private static SecureRandom r = new SecureRandom();

	public void tryRegisterUser(String email, String contactInfo,
			String password, Permission... permissions) {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @param email
	 * @param password
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Boolean tryLogin(String email, String password) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object logOut() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @return
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Boolean isLoggedIn() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static UserWrapper getLoggedInUser() {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void getOtherUser() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	public static String hash(String password, String salt) {
		if (password == null || password.length() == 0)
			throw new IllegalArgumentException(
					"Empty passwords are not supported.");
		SecretKey key = null;
		try {
			SecretKeyFactory f = SecretKeyFactory
					.getInstance("PBKDF2WithHmacSHA1");
			key = f.generateSecret(new PBEKeySpec(password.toCharArray(), salt
					.getBytes("UTF-8"), 10 * 1024, 512));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			throw new RuntimeException();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return DatatypeConverter.printBase64Binary(key.getEncoded());
	}
	
	public static String makeSalt() {
		final byte[] saltBytes = new byte[512 / 8];
		r.nextBytes(saltBytes);
		return DatatypeConverter.printBase64Binary(saltBytes);
	}
}