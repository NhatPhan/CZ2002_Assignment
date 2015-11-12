import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.Console;

public class AccountController {
	public static final String SEPARATOR = "|";
	private Account[] account = new Account[4];  //4 admins
	
	public AccountController(String filename) {
		// read String from text file
		try {
			ArrayList accountArray = (ArrayList)read(filename);
			for (int i = 0 ; i < accountArray.size() ; i++) {
				String st = (String)accountArray.get(i);
				
				// get individual 'fields' of the string separated by SEPARATOR
				StringTokenizer star = new StringTokenizer(st , SEPARATOR);	// pass in the string to the string tokenizer using delimiter "|"
	
				String  username = star.nextToken().trim();	// first token
				String  password = star.nextToken().trim();	// second token
				String  name = star.nextToken().trim();	// third token
	
				// Append the instance to the Account[] account
				account[i] = new Account(username, password, name);
			}
		}catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
	}
	
	public boolean authenticate() {
		String username, password, name;
		Scanner scan = new Scanner(System.in);
		
		//read username
		System.out.print("\nEnter your username : ");
		username = scan.next();
		
		//read password
		System.out.print("\nEnter your username : ");
		password = scan.next();

		AccountController acc_ctrl = new AccountController("account.txt");
		
		if(acc_ctrl.authenticate(username, password)) {
			System.out.println("***** Successful Authentication *****");
			return true;
		}
		else {
			System.out.println("***** Unsuscessful Authentication *****");
			return false;
		}
	}
	
	public boolean authenticate(String username, String password) {
		//System.out.printf("Username: %s pass: %s ", username, password);
		for (int i = 0 ; i < account.length ; i++) {
			if(account[i].getUsername().equals(username) && account[i].getPassword().equals(password)){
				return true;
			}
		}
		return false;
	}
	
	/** Read the contents of the given file. */
	public static List read(String fileName) throws IOException {
		List data = new ArrayList() ;
	    Scanner scanner = new Scanner(new FileInputStream(fileName));
	    try {
	      while (scanner.hasNextLine()){
	        data.add(scanner.nextLine());
	      }
	    }
	    finally{
	      scanner.close();
	    }
	    return data;
	}
	
	public Account[] getAccounts() {return account; }
	
	/*Testing
	public static void main(String[] aArgs)  {
		String username, password, name;
		Scanner scan = new Scanner(System.in);
		
		//read username
		System.out.print("\nEnter your username : ");
		username = scan.next();
		
		//read password
		System.out.print("\nEnter your username : ");
		password = scan.next();

		AccountController acc_ctrl = new AccountController("account.txt");
		
		if(acc_ctrl.authenticate(username, password))
			System.out.println("Successful");
		else
			System.out.println("Unsuscessful");
	} 
	*/

}
