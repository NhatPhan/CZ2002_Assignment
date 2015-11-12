import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileInputStream;import java.io.IOException;
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

public class CineplexController implements IPrinter{
	private ArrayList<Cineplex> cineplexList = new ArrayList();
	public static final String SEPARATOR_IN = ",";
	public static final String SEPARATOR_OUT_CINEMA = "|";
	public static final String SEPARATOR_IN_CINEPLEX = "#";
	
	public CineplexController (String filename) {
		// read String from text file
		try {
			ArrayList cineplexArray = (ArrayList)read(filename);
			for (int i = 0 ; i < cineplexArray.size() ; i++) {
				String st = (String)cineplexArray.get(i);
				List<Cinema> cinemaList = new ArrayList();
				
				// get individual cineplex of the string separated by SEPARATOR
				StringTokenizer star = new StringTokenizer(st , SEPARATOR_IN_CINEPLEX);
				
	
				String idCineplex = star.nextToken().trim();	
				String location = star.nextToken().trim();	
				String cinemas = star.nextToken().trim();	
				
				StringTokenizer star1 = new StringTokenizer(cinemas , SEPARATOR_OUT_CINEMA);
				while(star1.hasMoreTokens()) {
					List<Seat> seatList = new ArrayList();
					StringTokenizer star2 = new StringTokenizer(star1.nextToken().trim() , SEPARATOR_IN);
					String idCinema = star2.nextToken().trim();
					String showtime = star2.nextToken().trim();
					while(star2.hasMoreTokens()) {
						String[] newseat_array = star2.nextToken().trim().split(" ");
						Seat newseat = new Seat(newseat_array[0], Boolean.valueOf(newseat_array[1]));
						seatList.add(newseat);
					}
					Cinema newcinema = new Cinema(idCinema, showtime, seatList);
					cinemaList.add(newcinema);
				}
	
				Cineplex newCineplex = new Cineplex(idCineplex, location, cinemaList);
				cineplexList.add(newCineplex);
			}	
		}catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
	}
	
	public void addCineplex(Cineplex cineplex) {
		cineplexList.add(cineplex);
		updateNewCinplex();
	}
	
	public void printList(Showtime showtime) {
		for(Cineplex cineplex : cineplexList) {
			if(cineplex.getId().equals(showtime.getCineplexId())) {
				List<Cinema> cinemaList = cineplex.getCinemaList();
				for(Cinema cinema : cinemaList) {
					SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm:ss yyyy",Locale.ROOT);
					Calendar cal = Calendar.getInstance();
					try {
						cal.setTime(sdf.parse(cinema.getShowtime()));
						if(cal.equals(showtime.getCalendar())) {
							System.out.print("\n* Showtime : ");
							System.out.print(cal.getTime());
							System.out.print("\n* Location : ");
							System.out.print(cineplex.getLocation());
							System.out.printf("\n* Cineplex : %s ",cineplex.getId());
							System.out.printf("\n* Cinema : %s ",cinema.getId());
							System.out.print("\n* Seats (XX indicates seat not available) : \n");
							int rowLim = cinema.countRows();
							cinema.printSeats(rowLim);
						}
					}catch (ParseException e) {
						e.printStackTrace();
					}	
				}	
			}
		}
	}
	
	public void printList() {
		for(Cineplex cineplex : cineplexList) {
			System.out.print("id : ");
			System.out.print(cineplex.getId());
			System.out.print("Location : ");
			System.out.print(cineplex.getLocation());
			System.out.print("Location : ");
			System.out.print(cineplex.getLocation());
			System.out.print("\nCinemas's details : ");
			List<Cinema> cinemaList = cineplex.getCinemaList();
			for(Cinema cinema : cinemaList) {
				System.out.printf("\n%sst Cinema : ",cinema.getId());
				System.out.println();
				int rowLim = cinema.countRows();
				cinema.printSeats(rowLim);
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
		PrintWriter out = new PrintWriter(new FileWriter(fileName, true));
		try {
			for (int i = 0; i < data.size() ; i++) {
				out.append((String)data.get(i) + '\n');
			}
	    }
	    finally {
	      out.close();
	    }
	}
	
	public static void writeNew(String fileName, List data) throws IOException  {
		PrintWriter out = new PrintWriter(new FileWriter(fileName));
		try {
			for (int i = 0; i < data.size() ; i++) {
				out.write((String)data.get(i) + '\n');
			}
	    }
	    finally {
	      out.close();
	    }
	}
	
	public void updateCineplex(List<Ticket> ticketList) throws IOException {
		String showtimeStrArr = ticketList.get(0).getShowTime().getTime().toString();
		String showtimeStr = showtimeStrArr.substring(4,20) + showtimeStrArr.substring(24,28);
		for(Cineplex cineplex : cineplexList) {
			for(Cinema cinema : cineplex.getCinemaList()) {
				System.out.println(cinema.getShowtime());
				if(cinema.getShowtime().equals(showtimeStr)) {
					for(int j = 0; j < cinema.getSeatList().size(); j++) {
						for(int i = 0; i < ticketList.size(); i++) {
							String seatId = ticketList.get(i).getSeat().getId();
							if(cinema.getSeatList().get(j).getId().equals(seatId)) {
								cinema.getSeatList().get(j).bookSeat();
								sync("cineplex.txt", cinema.getShowtime());
							}
						}
					}
				}
			} 
		}
	}
	
	
	public void sync(String filename, String showtime) throws IOException {
		List alw = new ArrayList() ;	// to store Cineplex data
		for(Cineplex cineplex : cineplexList) {
			StringBuilder st =  new StringBuilder();
			st.append(cineplex.getId());
			st.append(SEPARATOR_IN_CINEPLEX);
			st.append(cineplex.getLocation());
			st.append(SEPARATOR_IN_CINEPLEX);
			for(Cinema cinema : cineplex.getCinemaList()) {
				st.append(cinema.getId());
				st.append(SEPARATOR_IN);
				if(cinema.getShowtime().equals(showtime))
					st.append(showtime);
				else 
					st.append(cinema.getShowtime());
				st.append(SEPARATOR_IN);
				int rowLim = cinema.countRows();
				int numLim = cinema.getSeatList().size() / rowLim;
				char row = 'A';
				for (int i = 0; i < numLim; i++) {
					for (int j = 0; j < rowLim; j++) {
						String seatID = Character.toString((char) (row + i)) + (j + 1);
						st.append(seatID);
						for(Seat seat : cinema.getSeatList()) {
							if(seat.getId().equals(seatID)) {
								if(seat.isOccupied())
									st.append(" true");
								else
									st.append(" false");
							}
						}
						st.append(SEPARATOR_IN);
					}
				}
				st.append(SEPARATOR_OUT_CINEMA);
			}
			alw.add(st.toString());
		}
		writeNew("cineplex.txt",alw);
	}
	
	public void updateNewCinplex() {
		List alw = new ArrayList() ;	// to store Cineplex data
		Scanner scan = new Scanner(System.in);
		
		for(int k = 0; k < cineplexList.size(); k++) {
			StringBuilder st =  new StringBuilder();
			st.append(cineplexList.get(k).getId());
			st.append(SEPARATOR_IN_CINEPLEX);
			st.append(cineplexList.get(k).getLocation());
			st.append(SEPARATOR_IN_CINEPLEX);
			for(int l = 0; l < cineplexList.get(k).getCinemaList().size(); l++) {
				st.append(cineplexList.get(k).getCinemaList().get(l).getId());
				st.append(",");
				st.append(cineplexList.get(k).getCinemaList().get(l).getShowtime());
				st.append(",");
				char row = 'A';
				for (int i = 0; i < 4; i++)
					for (int j = 0; j < 6; j++)
					{
						String seatID = Character.toString((char) (row + i)) + (j + 1);
						st.append(seatID);
						st.append(" false");
						st.append(SEPARATOR_IN);
					}
				st.append(SEPARATOR_OUT_CINEMA);
			}
			alw.add(st.toString());
		}
		try {
			writeNew("cineplex.txt",alw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] aArgs) throws IOException {
		List alw = new ArrayList() ;	// to store Cineplex data
		Scanner scan = new Scanner(System.in);

		int idCineplex = 0;	
		boolean moreCineplex = true;
		
		/*
		while(moreCineplex) {
			StringBuilder st =  new StringBuilder();
			idCineplex++;
			st.append(idCineplex);
			st.append(SEPARATOR_IN_CINEPLEX);
			System.out.print("\nEnter the following cineplex's details : ");
			if(idCineplex != 1)
				scan.nextLine();
			System.out.print("\n* Location: ");
			String location = scan.nextLine();
			st.append(location);
			st.append(SEPARATOR_IN_CINEPLEX);
			boolean moreCinema = true;
			int idCinema = 0;
			while(moreCinema) {
				idCinema++;
				System.out.printf("\nEnter the %dst cinema's details : ", idCinema);
				st.append(idCinema);
				st.append(SEPARATOR_IN);
				System.out.print("\n* How many rows in this cinema: ");
				int rowLim = scan.nextInt();
				System.out.print("\n* How many seat numbers per row: ");
				int numLim = scan.nextInt();
				char row = 'A';
				for (int i = 0; i < rowLim; i++)
					for (int j = 0; j < numLim; j++)
					{
						String seatID = Character.toString((char) (row + i)) + (j + 1);
						st.append(seatID);
						st.append(" false");
						st.append(SEPARATOR_IN);
					}
				System.out.print("\nAdd Cinema (y/n) : ");
				moreCinema = scan.next().equals("y");
				st.append(SEPARATOR_OUT_CINEMA);
			}
			alw.add(st.toString());
			moreCineplex = scan.next().equals("y");
		}
		write("cineplex.txt",alw);
		*/
		CineplexController cineplexCtrl = new CineplexController("cineplex.txt");
		cineplexCtrl.printList();
	}

	@Override
	public void print(int choice, Movie movie) {
		// TODO Auto-generated method stub
		
	}

	
}





















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

public class CineplexController implements IPrinter{
	private ArrayList<Cineplex> cineplexList = new ArrayList();
	public static final String SEPARATOR_IN = ",";
	public static final String SEPARATOR_OUT_CINEMA = "|";
	public static final String SEPARATOR_IN_CINEPLEX = "#";
	
	public CineplexController (String filename) {
		// read String from text file
		try {
			ArrayList cineplexArray = (ArrayList)read(filename);
			for (int i = 0 ; i < cineplexArray.size() ; i++) {
				String st = (String)cineplexArray.get(i);
				List<Cinema> cinemaList = new ArrayList();
				
				// get individual cineplex of the string separated by SEPARATOR
				StringTokenizer star = new StringTokenizer(st , SEPARATOR_IN_CINEPLEX);
				
	
				String idCineplex = star.nextToken().trim();	
				String location = star.nextToken().trim();	
				String cinemas = star.nextToken().trim();	
				
				StringTokenizer star1 = new StringTokenizer(cinemas , SEPARATOR_OUT_CINEMA);
				while(star1.hasMoreTokens()) {
					List<Seat> seatList = new ArrayList();
					StringTokenizer star2 = new StringTokenizer(star1.nextToken().trim() , SEPARATOR_IN);
					String idCinema = star2.nextToken().trim();
					String showtime = star2.nextToken().trim();
					while(star2.hasMoreTokens()) {
						String[] newseat_array = star2.nextToken().trim().split(" ");
						Seat newseat = new Seat(newseat_array[0], Boolean.valueOf(newseat_array[1]));
						seatList.add(newseat);
					}
					Cinema newcinema = new Cinema(idCinema, showtime, seatList);
					cinemaList.add(newcinema);
				}
	
				Cineplex newCineplex = new Cineplex(idCineplex, location, cinemaList);
				cineplexList.add(newCineplex);
			}	
		}catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
	}
	
	public void addCineplex(Cineplex cineplex) {
		cineplexList.add(cineplex);
		updateNewCinplex();
	}
	
	public void printList(Showtime showtime) {
		for(Cineplex cineplex : cineplexList) {
			if(cineplex.getId().equals(showtime.getCineplexId())) {
				List<Cinema> cinemaList = cineplex.getCinemaList();
				for(Cinema cinema : cinemaList) {
					SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm:ss yyyy",Locale.ROOT);
					Calendar cal = Calendar.getInstance();
					try {
						cal.setTime(sdf.parse(cinema.getShowtime()));
						if(cal.equals(showtime.getCalendar())) {
							System.out.print("\n* Showtime : ");
							System.out.print(cal.getTime());
							System.out.print("\n* Location : ");
							System.out.print(cineplex.getLocation());
							System.out.printf("\n* Cineplex : %s ",cineplex.getId());
							System.out.printf("\n* Cinema : %s ",cinema.getId());
							System.out.print("\n* Seats (XX indicates seat not available) : \n");
							int rowLim = cinema.countRows();
							cinema.printSeats(rowLim);
						}
					}catch (ParseException e) {
						e.printStackTrace();
					}	
				}	
			}
		}
	}
	
	public void printList() {
		for(Cineplex cineplex : cineplexList) {
			System.out.print("id : ");
			System.out.print(cineplex.getId());
			System.out.print("Location : ");
			System.out.print(cineplex.getLocation());
			System.out.print("Location : ");
			System.out.print(cineplex.getLocation());
			System.out.print("\nCinemas's details : ");
			List<Cinema> cinemaList = cineplex.getCinemaList();
			for(Cinema cinema : cinemaList) {
				System.out.printf("\n%sst Cinema : ",cinema.getId());
				System.out.println();
				int rowLim = cinema.countRows();
				cinema.printSeats(rowLim);
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
		PrintWriter out = new PrintWriter(new FileWriter(fileName, true));
		try {
			for (int i = 0; i < data.size() ; i++) {
				out.append((String)data.get(i) + '\n');
			}
	    }
	    finally {
	      out.close();
	    }
	}
	
	public static void writeNew(String fileName, List data) throws IOException  {
		PrintWriter out = new PrintWriter(new FileWriter(fileName));
		try {
			for (int i = 0; i < data.size() ; i++) {
				out.write((String)data.get(i) + '\n');
			}
	    }
	    finally {
	      out.close();
	    }
	}
	
	public void updateCineplex(List<Ticket> ticketList) throws IOException {
		String showtimeStrArr = ticketList.get(0).getShowTime().getTime().toString();
		String showtimeStr = showtimeStrArr.substring(4,20) + showtimeStrArr.substring(24,28);
		for(Cineplex cineplex : cineplexList) {
			for(Cinema cinema : cineplex.getCinemaList()) {
				System.out.println(cinema.getShowtime());
				if(cinema.getShowtime().equals(showtimeStr)) {
					for(int j = 0; j < cinema.getSeatList().size(); j++) {
						for(int i = 0; i < ticketList.size(); i++) {
							String seatId = ticketList.get(i).getSeat().getId();
							if(cinema.getSeatList().get(j).getId().equals(seatId)) {
								cinema.getSeatList().get(j).bookSeat();
								sync("cineplex.txt", cinema.getShowtime());
							}
						}
					}
				}
			} 
		}
	}
	
	
	public void sync(String filename, String showtime) throws IOException {
		List alw = new ArrayList() ;	// to store Cineplex data
		for(Cineplex cineplex : cineplexList) {
			StringBuilder st =  new StringBuilder();
			st.append(cineplex.getId());
			st.append(SEPARATOR_IN_CINEPLEX);
			st.append(cineplex.getLocation());
			st.append(SEPARATOR_IN_CINEPLEX);
			for(Cinema cinema : cineplex.getCinemaList()) {
				st.append(cinema.getId());
				st.append(SEPARATOR_IN);
				if(cinema.getShowtime().equals(showtime))
					st.append(showtime);
				else 
					st.append(cinema.getShowtime());
				st.append(SEPARATOR_IN);
				int rowLim = cinema.countRows();
				int numLim = cinema.getSeatList().size() / rowLim;
				char row = 'A';
				for (int i = 0; i < numLim; i++) {
					for (int j = 0; j < rowLim; j++) {
						String seatID = Character.toString((char) (row + i)) + (j + 1);
						st.append(seatID);
						for(Seat seat : cinema.getSeatList()) {
							if(seat.getId().equals(seatID)) {
								if(seat.isOccupied())
									st.append(" true");
								else
									st.append(" false");
							}
						}
						st.append(SEPARATOR_IN);
					}
				}
				st.append(SEPARATOR_OUT_CINEMA);
			}
			alw.add(st.toString());
		}
		writeNew("cineplex.txt",alw);
	}
	
	public void updateNewCinplex() {
		List alw = new ArrayList() ;	// to store Cineplex data
		Scanner scan = new Scanner(System.in);
		
		for(int k = 0; k < cineplexList.size(); k++) {
			StringBuilder st =  new StringBuilder();
			st.append(cineplexList.get(k).getId());
			st.append(SEPARATOR_IN_CINEPLEX);
			st.append(cineplexList.get(k).getLocation());
			st.append(SEPARATOR_IN_CINEPLEX);
			for(int l = 0; l < cineplexList.get(k).getCinemaList().size(); l++) {
				st.append(cineplexList.get(k).getCinemaList().get(l).getId());
				st.append(",");
				st.append(cineplexList.get(k).getCinemaList().get(l).getShowtime());
				st.append(",");
				char row = 'A';
				for (int i = 0; i < 4; i++)
					for (int j = 0; j < 6; j++)
					{
						String seatID = Character.toString((char) (row + i)) + (j + 1);
						st.append(seatID);
						st.append(" false");
						st.append(SEPARATOR_IN);
					}
				st.append(SEPARATOR_OUT_CINEMA);
			}
			alw.add(st.toString());
		}
		try {
			writeNew("cineplex.txt",alw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] aArgs) throws IOException {
		List alw = new ArrayList() ;	// to store Cineplex data
		Scanner scan = new Scanner(System.in);

		int idCineplex = 0;	
		boolean moreCineplex = true;
		
		/*
		while(moreCineplex) {
			StringBuilder st =  new StringBuilder();
			idCineplex++;
			st.append(idCineplex);
			st.append(SEPARATOR_IN_CINEPLEX);
			System.out.print("\nEnter the following cineplex's details : ");
			if(idCineplex != 1)
				scan.nextLine();
			System.out.print("\n* Location: ");
			String location = scan.nextLine();
			st.append(location);
			st.append(SEPARATOR_IN_CINEPLEX);
			boolean moreCinema = true;
			int idCinema = 0;
			while(moreCinema) {
				idCinema++;
				System.out.printf("\nEnter the %dst cinema's details : ", idCinema);
				st.append(idCinema);
				st.append(SEPARATOR_IN);
				System.out.print("\n* How many rows in this cinema: ");
				int rowLim = scan.nextInt();
				System.out.print("\n* How many seat numbers per row: ");
				int numLim = scan.nextInt();
				char row = 'A';
				for (int i = 0; i < rowLim; i++)
					for (int j = 0; j < numLim; j++)
					{
						String seatID = Character.toString((char) (row + i)) + (j + 1);
						st.append(seatID);
						st.append(" false");
						st.append(SEPARATOR_IN);
					}
				System.out.print("\nAdd Cinema (y/n) : ");
				moreCinema = scan.next().equals("y");
				st.append(SEPARATOR_OUT_CINEMA);
			}
			alw.add(st.toString());
			moreCineplex = scan.next().equals("y");
		}
		write("cineplex.txt",alw);
		*/
		CineplexController cineplexCtrl = new CineplexController("cineplex.txt");
		cineplexCtrl.printList();
	}

	@Override
	public void print(int choice, Movie movie) {
		// TODO Auto-generated method stub
		
	}

	
}




















