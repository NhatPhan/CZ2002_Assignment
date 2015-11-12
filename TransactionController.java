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
import java.text.ParseException;


public class TransactionController {
	private ArrayList<Transaction> transactionList = new ArrayList();
	private PriceSettings priceSettings;
	public static final String SEPARATOR_IN = ",";
	public static final String SEPARATOR_IN_TRANS = ";";
	public static final String SEPARATOR_OUT_TICKET = "/";
	public static final String SEPARATOR_IN_TICKET = "?";
	public static final String SEPARATOR_OUT_CLASS = "&";
	
	public TransactionController(String filename) {
		PriceController priceCtrl = new PriceController("pricesettings.txt");
		this.priceSettings = priceCtrl.getPriceSettings();
		// read String from text file
		try {
			ArrayList transactionArray = (ArrayList)read(filename);
			for (int i = 0 ; i < transactionArray.size() ; i++) {
				try {
					String st = (String)transactionArray.get(i);
					List<TicketInfo> ticketList = new ArrayList<TicketInfo>();
					// get individual 'fields' of the string separated by SEPARATOR
					StringTokenizer star = new StringTokenizer(st , SEPARATOR_IN_TRANS);	// pass in the string to the string tokenizer using delimiter "|"
					String  id = star.nextToken().trim();
					String  date = star.nextToken().trim();
					SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm:ss yyyy",Locale.ROOT);
					String  priceStr = star.nextToken().trim();	
					double price = Double.parseDouble(priceStr);
					String ticketInfoListStr = star.nextToken().trim();
					//Tickets data
					StringTokenizer star1 = new StringTokenizer(ticketInfoListStr , SEPARATOR_OUT_TICKET);
					while(star1.hasMoreTokens()) {
						String firstStr = star1.nextToken();
						StringTokenizer star2 = new StringTokenizer(firstStr , SEPARATOR_IN_TICKET);
						String  name = star2.nextToken().trim();	
						String  mobile = star2.nextToken().trim();	
						String  email = star2.nextToken().trim();
						Customer newcustomer = new Customer(name,mobile,email);
						String  movieTitle = star2.nextToken().trim();	
						String  seatId = star2.nextToken().trim();	
						String  classPref = star2.nextToken().trim();	
						String  cineplexId = star2.nextToken().trim();	
						String  cinemaId = star2.nextToken().trim();
						String showtimeStrArr = star2.nextToken().trim();
						String showtimeStr = showtimeStrArr.substring(4,20) + showtimeStrArr.substring(24,28);
						Calendar cal2 = Calendar.getInstance();
						cal2.setTime(sdf.parse(showtimeStr));
						Showtime newshowtime = new Showtime(cineplexId, cinemaId, cal2);
						TicketInfo newTicketInfo = new TicketInfo(newcustomer, movieTitle, seatId, classPref, newshowtime);
						ticketList.add(newTicketInfo);
					}
					Transaction newTransaction = new Transaction(id, date, price, ticketList);
					transactionList.add(newTransaction);
				}catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
	}
	
	public void add(Transaction transaction) throws IOException {
		transactionList.add(transaction);
		sync();
	}
	
	public void remove(Transaction transaction) throws IOException {
		transactionList.remove(transaction);
		sync();
	}
	
	public void sync() throws IOException {
		List alw = new ArrayList();
		for(Transaction trans : transactionList) {
			StringBuilder st =  new StringBuilder();
			st.append(trans.getId());
			st.append(SEPARATOR_IN_TRANS);
			st.append(trans.getDate());
			st.append(SEPARATOR_IN_TRANS);
			st.append(trans.getPrice());
			st.append(SEPARATOR_IN_TRANS);
			for(TicketInfo ticketInfo : trans.getTickets()) {
				st.append(ticketInfo.getCustomer().getName());
				st.append(SEPARATOR_IN_TICKET);
				st.append(ticketInfo.getCustomer().getMobile());
				st.append(SEPARATOR_IN_TICKET);
				st.append(ticketInfo.getCustomer().getEmail());
				st.append(SEPARATOR_IN_TICKET);
				st.append(ticketInfo.getMovieTitle());
				st.append(SEPARATOR_IN_TICKET);
				st.append(ticketInfo.getSeatId());
				st.append(SEPARATOR_IN_TICKET);
				st.append(ticketInfo.getClassPref());
				st.append(SEPARATOR_IN_TICKET);
				st.append(ticketInfo.getShowtime().getCineplexId());
				st.append(SEPARATOR_IN_TICKET);
				st.append(ticketInfo.getShowtime().getCinemaId());
				st.append(SEPARATOR_IN_TICKET);
				st.append(ticketInfo.getShowtime().getCalendar().getTime().toString());
				st.append(SEPARATOR_IN_TICKET);
			}
			alw.add(st.toString());
		}
		write("transaction.txt",alw);
	}
	
	public void getTransHistory(String transId) {
		for(int i = 0; i < transactionList.size(); i++) {
			if(transactionList.get(i).getId().equals(transId)) {
				System.out.println("-----------------------------------------------------------");
				System.out.println("Transaction Information: ");
				System.out.printf("Date of Purchases: %s", transactionList.get(i).getDate());
				System.out.printf("\nNumber of tickets buy: %d", transactionList.get(i).getTickets().size());
				System.out.printf("\nMovie Title: %s", transactionList.get(i).getTickets().get(0).getMovieTitle());
				System.out.printf("\nShow Time: %s", transactionList.get(i).getTickets().get(0).getShowtime().getCalendar().getTime());
				for(int j = 0; j < transactionList.get(i).getTickets().size(); j++) {	
					System.out.printf("\nTicket %d:", j);
					System.out.printf("\nCustomer Name: %s", transactionList.get(i).getTickets().get(j).getCustomer().getName());
					System.out.printf("\nCustomer Mobile: %s", transactionList.get(i).getTickets().get(j).getCustomer().getMobile());
					System.out.printf("\nCustomer Email: %s", transactionList.get(i).getTickets().get(j).getCustomer().getEmail());
					System.out.printf("\nSeat Id: %s", transactionList.get(i).getTickets().get(j).getSeatId());
					System.out.printf("\nClass Pref: %s", transactionList.get(i).getTickets().get(j).getClassPref());
				}
				System.out.printf("\nTotal Price: %.2f\n", transactionList.get(i).getPrice());
				System.out.println("\n-----------------------------------------------------------");
			}
		}
	}
	

	// Read the contents of the given file
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
		
	// Write fixed content to the given file
	public static void write(String fileName, List data) throws IOException  {
		PrintWriter out = new PrintWriter(new FileWriter(fileName));
		try {
			for (int i = 0; i < data.size() ; i++) {
				out.println((String)data.get(i));
			}
	    }
	    finally {
	      out.close();
	    }
	}
	
	// Write fixed content to the given file
	public static void writeAppend(String fileName, List data) throws IOException  {
		PrintWriter out = new PrintWriter(new FileWriter(fileName));
		try {
			for (int i = 0; i < data.size() ; i++) {
				out.append((String)data.get(i)  + '\n');
			}
	    }
	    finally {
	      out.close();
	    }
	}
	
	//add data to file
	/*
	public static void main(String[] aArgs) throws IOException {
		int i = 0;
		List alw = new ArrayList() ;// to store Professors data
		Scanner scan = new Scanner(System.in);
		StringBuilder st =  new StringBuilder();
		System.out.print("Enter the following details : ");	
		st.append(i);
		st.append(SEPARATOR_OUT_TRANS);
		//Calendar
		//Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MM/yyyy HH:mm", Locale.FRANCE);
		System.out.print("\n* Days Of Week (Mon to Sun) : ");
		String dayOfWeek = scan.next();
		System.out.print("\n* Day (00 to 31): ");
		String day = scan.next();
		System.out.print("\n* Month (01 to 12) : ");
		String month = scan.next();
		System.out.print("\n* Year : ");
		String year = scan.next();
		System.out.print("\n* Hour : ");
		String hour = scan.next();
		System.out.print("\n* Minute : ");
		String minute = scan.next();
		String cal = dayOfWeek + " " + day + "/" + month + "/" + year + " " + hour + ":" + minute;
		//cal.setTime(sdf.parse(cal_str));
		//transactionList[i].setDate(cal);
		st.append(cal);
		st.append(SEPARATOR_OUT_TRANS);
		
		System.out.print("\nEnter the price for this transaction : ");
		double price = scan.nextDouble();
		st.append(price);
		st.append(SEPARATOR_OUT_TRANS);
		
		boolean cont = true;
		while(cont) {
			//Preferences
			System.out.print("\nEnter the following preference's details : ");
			System.out.print("\n* class: ");
			String classPref = scan.next();
			st.append(classPref);
			st.append(SEPARATOR_OUT_TICKET);
			System.out.print("\n* age: ");
			String age = scan.next();
			st.append(age);
			st.append(SEPARATOR_OUT_TICKET);
			System.out.print("\n* dayPref: ");
			String dayPref = scan.next();
			st.append(dayPref);
			st.append(SEPARATOR_OUT_TICKET);
			System.out.print("\n* Seat: \n");
			System.out.print("\n* row: ");
			String row = scan.next();
			st.append(row);
			st.append(SEPARATOR_IN);
			System.out.print("\n* number: ");
			int number = scan.nextInt();
			st.append(number);
			st.append(SEPARATOR_IN);
			System.out.print("\n* isOccupied: ");
			boolean isOccupied = scan.nextBoolean();
			st.append(isOccupied);
			st.append(SEPARATOR_OUT_TICKET);

			//Customer
			System.out.print("\nEnter the following customer's details : ");
			System.out.print("\n* Name: ");
			String name = scan.next();
			st.append(name);
			st.append(SEPARATOR_IN);
			System.out.print("*\n Mobile: ");
			String mobile = scan.next();
			st.append(mobile);
			st.append(SEPARATOR_IN);
			System.out.print("\n* Email: ");
			String email = scan.next();
			st.append(email);
			st.append(SEPARATOR_OUT_TICKET);
			
			//Movie
			System.out.print("\nEnter the following movie's details : ");
			System.out.print("\n* Title: ");
			String title = scan.next();
			st.append(title);
			st.append(SEPARATOR_IN);
			System.out.print("*\n Status: ");
			String status = scan.next();
			st.append(status);
			st.append(SEPARATOR_IN);
			System.out.print("\n* Synopsis: ");
			String synopsis = scan.next();
			st.append(synopsis);
			st.append(SEPARATOR_IN);
			System.out.print("\n* Director: ");
			String director = scan.next();
			st.append(director);
			st.append(SEPARATOR_IN);
			System.out.print("\n* Type: ");
			String type = scan.next();
			st.append(type);
			st.append(SEPARATOR_IN);
			System.out.print("\n* OverallRating: ");
			double overallRating = scan.nextDouble();
			st.append(overallRating);
			st.append(SEPARATOR_OUT_TICKET_CLASS);
			System.out.print("\n* Cast: \n");
			boolean moreCast = true;
			String cast;
			while(moreCast) {
				cast = scan.next();
				st.append(cast);
				System.out.print("\n* Add Cast(y or n): ");
				moreCast = scan.next().equals("y");
				if(moreCast) {st.append(SEPARATOR_IN); }
			}
			st.append(SEPARATOR_OUT_TICKET_CLASS);
			System.out.print("\n* showTime: ");
			String movieShowTime;
			boolean moreMovieShowTime = true;
			while(moreMovieShowTime) {
				System.out.print("\n* Days Of Week (Mon to Sun) : ");
				dayOfWeek = scan.next();
				System.out.print("\n* Day (00 to 31): ");
				day = scan.next();
				System.out.print("\n* Month (01 to 12) : ");
				month = scan.next();
				System.out.print("\n* Year : ");
				year = scan.next();
				System.out.print("\n* Hour : ");
				hour = scan.next();
				System.out.print("\n* Minute : ");
				minute = scan.next();
				movieShowTime = dayOfWeek + day + "/" + month + "/" + year + hour + ":" + minute;
				st.append(movieShowTime);
				moreMovieShowTime = scan.next().equals("y");
				if(moreMovieShowTime) {st.append(SEPARATOR_IN); }
			}
			st.append(SEPARATOR_OUT_TICKET_CLASS);
			System.out.print("\n* Review: ");
			String review;
			boolean moreReview = true;
			while(moreReview) {
				scan.nextLine();
				review = scan.nextLine();
				st.append(review);
				System.out.print("\n* Add Movie Review (y or n): ");
				moreReview = scan.next().equals("y");
				if(moreReview) {st.append(SEPARATOR_IN); }
			}
			System.out.print("\n* Add More Data (y or n): ");
			cont = scan.next().equals("y");
			if(cont) {st.append(SEPARATOR_OUT_TICKET);}
		}
		alw.add(st.toString());
		write("transaction.txt",alw);	
	}	
	*/
}











