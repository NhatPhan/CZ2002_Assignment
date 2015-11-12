import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.Console;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Random;
import java.text.ParseException;

public class Moblima {
	public static void main(String[] aArgs) throws IOException {
		boolean cont = false;
		while(true) {
			Scanner scan = new Scanner(System.in);
			System.out.println("\n========================= MOBLIMA ===========================");
			System.out.println("|1. Admin Module                                            |");
			System.out.println("|2. Movie-Goer Module                                       |");
			System.out.println("|3. Exit                                                    |");
			System.out.println("=============================================================");
			int module = scan.nextInt();
			switch(module) {
				case 1:
					AccountController accountCtrl = new AccountController("account.txt");
					if(accountCtrl.authenticate()) {Admin.main(aArgs);};
					break;
				case 2:
					MovieGoer.main(aArgs);
					break;
				case 3:
					cont = false;
					break;
			}
		}
	}
}







