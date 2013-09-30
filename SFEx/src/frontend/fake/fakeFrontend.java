package frontend.fake;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import shared.API.IItemReadonly;

import backend.API.IBackend;

public class fakeFrontend {

	public static void main(String[] args) {
		IBackend backend = null;
		
		//XXX: BAD IDEA!!!!
		backend.getIRegistration().tryRegister("test", "test!@#");
		
		String email;
		String password;
		while (true) {
			email = readLine("Email: ");
			password = readPassword("Password: ");
			if (backend.getILogin().tryLogin(email, password))
				break;
			else
				System.out.println("Login failed");
		}		
		
		while (true) {
			System.out.println("(1): Browse your items");
			System.out.println("(2): Browse items");
			System.out.println("(3): Sell an item");
			System.out.println("(4): Die");
			String res = readLine("What do you wish to do?");
			int resIndex;
			try {
				resIndex = Integer.valueOf(res);
			} catch (NumberFormatException n) {
				resIndex = -1;
			}
			switch(resIndex) {
				case 1: // My items
					browseSelling(backend);
					break;
				case 2: // Browse
					browseItems(backend);
					break;
				case 3: // Sell
					sellItem(backend);
					break;
				case 4: // Die
					System.out.println("Bye");
					return;
				case -1:
				default:
					System.out.println("Error: invalid index");
			}
			
			
		}
	}
	
	private static void sellItem(IBackend backend) {
		// TODO Auto-generated method stub
		
	}

	private static void browseItems(IBackend backend) {
		IItemReadonly[] items = backend.getIUser().getItemsSold();
		for (int i = 0; i < items.length; i++) {
			System.out.println("(" + (i+1) + ") " + items[i]);
		}
		while (true) {
			String res = readLine("Enter an index to look at item, or 0 to return: ");
			int resIndex;
			try {
				resIndex = Integer.valueOf(res);
			} catch (NumberFormatException n) {
				resIndex = -1;
			}
			if (resIndex == 0)
				return;
			else if (resIndex <= items.length && resIndex >= 1)
				dispItem(items[resIndex - 1]);
		}
	}

	private static void dispItem(IItemReadonly item) {
		System.out.println(item);
	}

	private static void browseSelling(IBackend backend) {
		// TODO Auto-generated method stub
		
	}

	private static String readLine(String prompt) {
		String line = null;
        Console c = System.console();
        if (c != null) {
             line = String.valueOf(c.readLine(prompt));
        } else {
            System.out.print(prompt);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                 line = bufferedReader.readLine();
            } catch (IOException e) { 
                //Ignore    
            }
        }
        return line;
	}

	private static String readPassword(String prompt) {
        String line = null;
        Console c = System.console();
        if (c != null) {
             line = String.valueOf(c.readPassword(prompt));
        } else {
            System.out.print(prompt);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                 line = bufferedReader.readLine();
            } catch (IOException e) { 
                //Ignore    
            }
        }
        return line;
    }

}
