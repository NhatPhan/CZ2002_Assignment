import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collections;
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

public class MovieController implements IPrinter {
	
	/* file IO seperator */
	public static final String SEPARATOR_IN1 = "#";
	public static final String SEPARATOR_IN = ",";
	public static final String SEPARATOR_IN_SHOWTIME = "|";
	public static final String SEPARATOR_OUT_CLASS = "&";
	public static final String[] cineplexLocations = {"53 Ang Mo Kio Ave 3 AMK Hub Level 4 Singapore 569933",
	"1 Bukit Batok Central Link West Mall", "5 Stadium Walk Level 3 Kallang Leisure Park"};
	
	private List<Movie> movieList = new ArrayList(); // copy of data from storage for processing
	
	public static boolean admin = false; // access privilege
		
	public MovieController(String filename) {
		// read String from text file
		try {
			ArrayList movieArray = (ArrayList)read(filename);
			for (int i = 0 ; i < movieArray.size() ; i++) {
				List<String> cast = new ArrayList();
				List<Showtime> showtime = new ArrayList();
				List<Review> review = new ArrayList();
				String st = (String)movieArray.get(i);		
				// pass in the string to the string tokenizer using delimiter "&"
				StringTokenizer star = new StringTokenizer(st , SEPARATOR_OUT_CLASS);
				// first token of star
				String  firstStr = star.nextToken().trim();	
				// pass in the string to the string tokenizer using delimiter ","
				StringTokenizer star1 = new StringTokenizer(firstStr, SEPARATOR_IN1);
				String title = star1.nextToken().trim();
				String status = star1.nextToken().trim();
				String synopsis = star1.nextToken().trim();
				String director = star1.nextToken().trim();
				String type = star1.nextToken().trim();
				// second token of star (Cast)
				String  secondStr = star.nextToken().trim();
				// pass in the string to the string tokenizer using delimiter ","
				StringTokenizer star2 = new StringTokenizer(secondStr, SEPARATOR_IN);
				while(star2.hasMoreTokens()) {
					cast.add(star2.nextToken().trim());
				}
				// third token of star (Showtime)
				String  thirdStr = star.nextToken().trim();
				// pass in the string to the string tokenizer using delimiter ","
				StringTokenizer star3 = new StringTokenizer(thirdStr, SEPARATOR_IN);
				SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm:ss yyyy",Locale.ROOT);
				try {
					while(star3.hasMoreTokens()) {
						Calendar cal = Calendar.getInstance();
						String  subStr = star3.nextToken().trim();
						StringTokenizer substar3 = new StringTokenizer(subStr, SEPARATOR_IN_SHOWTIME);
						String cineplexId = substar3.nextToken().trim();
						String cinemaId = substar3.nextToken().trim();
						String date = substar3.nextToken().trim();
						cal.setTime(sdf.parse(date));
						Showtime newShowtime = new Showtime(cineplexId, cinemaId, cal);
						showtime.add(newShowtime);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				// second token of star (Review)
				String  fourthStr = star.nextToken().trim();
				// pass in the string to the string tokenizer using delimiter ","
				StringTokenizer star4 = new StringTokenizer(fourthStr, SEPARATOR_IN);
				String rating_review;
				String rating;
				String single_review;
				while(star4.hasMoreTokens()) {
					rating_review = star4.nextToken().trim();
					StringTokenizer rating_review_star = new StringTokenizer(rating_review, "::");
					rating = rating_review_star.nextToken().trim();
					single_review = rating_review_star.nextToken().trim();
					Review newreview = new Review(Double.parseDouble(rating),single_review);
					review.add(newreview);
				}
				// Append the instance to the Account[] account
				Movie movie = new Movie(title,status,synopsis,director,cast,type,showtime,review);
				movieList.add(movie);
			}
		}catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
	}
	
	public List<Movie> getMovieList() {
		return movieList;
	} 
	
	@Override
	public void print(int choice, Movie movie) {
		switch(choice) {
			case 1: 
				printList(movie);
				break;
			case 2:
				printTopMovie();
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
	/*	
	// Append fixed content to the given file
	public static void write(String fileName, List data) throws IOException  {
		PrintWriter out = new PrintWriter(new FileWriter(fileName));
		try {
			for (int i = 0; i < data.size() ; i++) {
				out.append((String)data.get(i) + '\n');
			}
	    }
	    finally {
	      out.close();
	    }
	}
	*/
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
	
	
	public void searchMoviesByTitle(String title) {
		boolean found = false;
		System.out.println("\n-------------------------------------------------------------");
		for(int i = 0; i < movieList.size(); i++) {
			if(movieList.get(i).getTitle().toLowerCase().contains(title.toLowerCase())) {
				printList(movieList.get(i));
				found = true;
			}
		}
		if(!found)
			System.out.println("No Movies Found!");
		System.out.println("-------------------------------------------------------------");
	}
	
	public void printList(Movie movie) {
		System.out.println(movie.getTitle());
	}
	
	public void searchMoviesByStatus(String status) {
		boolean found = false;
		System.out.println("\n-------------------------------------------------------------");
		for(int i = 0; i < movieList.size(); i++) {
			if(movieList.get(i).getStatus().equals(status)) {
				printList(movieList.get(i));
				found = true;
			}
		}
		if(!found)
			System.out.println("No Movies Found!");
		System.out.println("-------------------------------------------------------------");
	}
	
	public List<Showtime> printDetails(Movie movie) {
		System.out.println("\n-------------------------------------------------------------");
		System.out.print("\n* Title: ");
		System.out.print(movie.getTitle());
		System.out.print("\n* Status: ");
		System.out.print(movie.getStatus());
		System.out.print("\n* Synopsis: ");
		System.out.print(movie.getSynopsis());
		System.out.print("\n* Director: ");
		System.out.print(movie.getDirector());
		System.out.print("\n* Type: ");
		System.out.print(movie.getType());
		System.out.printf("\n* Overall Rating: %.2f / 5",movie.getOverallRating());
		List<String> cast = movie.getCast();
		System.out.print("\n* Cast: ");
		for(int j = 0; j < cast.size(); j++) {
			System.out.print(cast.get(j));
			if(j < cast.size() - 1)
				System.out.print(" , ");
		}
		
		List<Showtime> showtime = movie.getShowTime();
		System.out.print("\n* Showtimes: ");
		for(int j = 0; j < showtime.size(); j++) {
			System.out.printf("\n\tIndex %d: Cineplex %s, Cinema %s ",j + 1,showtime.get(j).getCineplexId(), showtime.get(j).getCinemaId());
			System.out.print(showtime.get(j).getCalendar().getTime());	
		}
		List<Review> reviewList = movie.getReviewList();
		System.out.print("\n* Reviews: ");
		for(Review review : reviewList) {
			System.out.println();
			System.out.println("\t" +review.getRating() + " / 5");
			System.out.println("\t" + review.getReview());
		}
		System.out.println("-------------------------------------------------------------");
		return showtime;
	}
	
	
	public void checkAndSelectSeats(Showtime showtime) {
		CineplexController cineplexCtrl = new CineplexController("cineplex.txt");
		cineplexCtrl.printList(showtime);
	}
	
	public void printTopMovie() {
		List<Double> ratingList = new ArrayList();
		for(Movie movie : movieList) {
			ratingList.add(movie.getOverallRating());
		}
		Collections.sort(ratingList);
		List<Double> sortedRatingList = ratingList.subList(0, 5);
		int i = 0;
		for(Movie movie : movieList) { 
			for(double rating : sortedRatingList) {
				if(rating == movie.getOverallRating()) {
					i++;
					System.out.printf("%d. %s",i,movie.getTitle());
				}
				if(i == 5)
					return;
			}
		}
	}
	
	// get & set access privilege
	public static void setAccess ()
	{
		MovieController.admin = true;
	}
	public static boolean getAccess ()
	{
		return MovieController.admin;
	}
	
	// create a new movie
	public void addMovie ()
	{
		Scanner sc = new Scanner (System.in);
		List<String> newMovie = toStringList(movieList);
		while (true)
		{
			CineplexController cineplexCtrl = new CineplexController("cineplex.txt");
			StringBuilder st =  new StringBuilder();
			System.out.println("\n======================== ADD MOVIE ==========================");
			System.out.print("\n* Title: ");
			String title = sc.nextLine();
			st.append(title);
			st.append(SEPARATOR_IN1);
			System.out.print("\n* Status: ");
			String status = sc.nextLine();
			st.append(status);
			st.append(SEPARATOR_IN1);
			System.out.print("\n* Synopsis: ");
			String synopsis = sc.nextLine();
			st.append(synopsis);
			st.append(SEPARATOR_IN1);
			System.out.print("\n* Director: ");
			String director = sc.nextLine();
			st.append(director);
			st.append(SEPARATOR_IN1);
			System.out.print("\n* Type: ");
			String type = sc.nextLine();
			st.append(type);
			st.append(SEPARATOR_OUT_CLASS);
			System.out.print("\n* Cast: ");
			String cast = sc.nextLine();
			while(!cast.equals("NONE")) {
				st.append(cast);
				st.append(SEPARATOR_IN);
				cast = sc.nextLine();
			}
			st.deleteCharAt(st.length() - 1);
			st.append(SEPARATOR_OUT_CLASS);
			System.out.print("\n* showTime: ");
			String movieShowTime;
			boolean moreMovieShowTime = true;
			while(moreMovieShowTime) {
				List<Cinema> cinemaList = new ArrayList();
				System.out.print("\n* CineplexId : ");
				String cineplexId = sc.next();
				System.out.print("\n* CinemaId : ");
				String cinemaId = sc.next();
				System.out.print("\n* Day (00 to 31): ");
				String day = sc.next();
				System.out.print("\n* Month (Jan to Dec) : ");
				String month = sc.next();
				System.out.print("\n* Year : ");
				String year = sc.next();
				System.out.print("\n* Hour : ");
				String hour = sc.next();
				System.out.print("\n* Minute : ");
				String minute = sc.next();
				movieShowTime = cineplexId + "|" + cinemaId + "|" + month + " " + day + " " + hour + ":" + minute + ":00 " + year;
				String movieShowTimeStr = month + " " + day + " " + hour + ":" + minute + ":00 " + year;
				st.append(movieShowTime);
				System.out.print("\n* Add More Showtime (y or n): ");
				moreMovieShowTime = sc.next().equals("y");
				if (moreMovieShowTime) {st.append(SEPARATOR_IN); }
				Cinema newCinema = new Cinema(cinemaId, movieShowTimeStr, 4, 6);
				cinemaList.add(newCinema);
				Cineplex newcineplex = new Cineplex(cineplexId, cineplexLocations[Integer.parseInt(cineplexId) - 1], cinemaList);
				cineplexCtrl.addCineplex(newcineplex);
			}
			st.append(SEPARATOR_OUT_CLASS);
			System.out.print("\n* Rating and Written Review: ");
			String rating;
			String review;
			boolean moreReview = true;
			while(moreReview) {
				System.out.print("\n*Rating: ");
				rating = sc.next();
				sc.nextLine();
				System.out.print("\n*Review: ");
				review = sc.nextLine();
				st.append(rating);
				st.append("::");
				st.append(review);
				System.out.print("\n* Add Movie Feedback (y or n): ");
				moreReview = sc.nextLine().equals("y");
				if(moreReview) {st.append(SEPARATOR_IN); }
			}
			newMovie.add(st.toString());
			System.out.println("=============================================================\n");
			System.out.print("\n* Add Movie (y or n): ");
			if (sc.nextLine().equals("n"))
				break;
		}
		try {
			write("movie.txt",newMovie);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sync();
	}
	
	private void sync ()
	{
		MovieController updated = new MovieController ("movie.txt");
		this.movieList = updated.getMovieList();
	}
	
	public void updateMovie ()
	{
		Scanner sc = new Scanner (System.in);
		String none = "NONE";
		
		while (true)
		{
			System.out.println("\n====================== UPDATE MOVIE =========================");
			System.out.print("Enter EXACT Movie Title to be updated: ");	// search for movie to update
			String title = sc.nextLine();
			if (title.equals(none))
				break;
			int index = -1;
			for (int i = 0; i < movieList.size(); i++)
				if (movieList.get(i).getTitle().equals(title))
				{
					index = i;
					break;
				}
			if (index == -1)	// search failed
			{
				System.out.println("No Movie Title existed in the database!!");
				break;
			}
			System.out.println("(NONE = no update)");
			Movie item = movieList.get(index);
			System.out.print("Enter Updated Status: ");	// update STATUS
			String status = sc.nextLine();
			if (!status.equals(none))
				item.setStatus(status);
			System.out.print("Enter Updated Synopsis: ");	// update SYNOPSIS
			String synopsis = sc.nextLine();
			if (!synopsis.equals(none))
				item.setSynopsis(synopsis);
			System.out.print("Enter Updated Type: ");	// update TYPE
			String type = sc.nextLine();
			if (!type.equals(none))
				item.setType(type);
			System.out.println("Show Time Update:");	// update SHOWTIME
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd HH:mm:ss yyyy",Locale.ROOT);
			while (true)	// multiple instance of update
			{
				System.out.print("Enter Updated CineplexId: ");
				String cineplexId = sc.next();
				sc.nextLine();
				if (cineplexId.equals("0"))
					break;
				System.out.print("Enter Updated CinemaId: ");
				String cinemaId = sc.next();
				sc.nextLine();
				if (cinemaId.equals("0"))
					break;
				Calendar cal = Calendar.getInstance();	// build new Showtime
				System.out.print("Enter Day (00 to 31): ");
				String day = sc.nextLine();
				System.out.print("Enter Month (Jan to Dec) : ");
				String month = sc.nextLine();
				System.out.print("Enter Year : ");
				String year = sc.nextLine();
				System.out.print("Enter Hour : ");
				String hour = sc.nextLine();
				System.out.print("Enter Minute : ");
				String minute = sc.nextLine();
				String date = month + " " + day + " " + hour + ":" + minute + ":00 " + year;
				try {
					cal.setTime(sdf.parse(date));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Showtime st = new Showtime(cineplexId, cinemaId, cal);
				index = -1;
				for (int i = 0; i < item.getShowTime().size(); i++)	// search for existing showtime in system
					if (item.getShowTime().get(i).equals(st))
					{
						index = i;
						break;
					}
				if (index == -1)	// if no match, create new Showtime for movie
				{
					List<Showtime> update = item.getShowTime();
					update.add(st);
					item.setShowTime(update);
				}
				else	// else exit update routine
					break;
			}
		}
		System.out.println("=============================================================\n");
		try {
			write("movie.txt", toStringList(movieList));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sync();
	}

	public void deleteMovie () throws IOException
	{
		Scanner sc = new Scanner (System.in);
		
		while (true)
		{
			System.out.println("\n======================= DELETE MOVIE ========================");
			System.out.println("Enter EXACT Movie Title to be deleted: ");
			String title = sc.nextLine();
			if (title.equals("NONE"))
				break;
			for (Movie movie : movieList)
				if (movie.getTitle().equals(title))
				{
					movie.setStatus("End Of Showing");
					write("movie.txt", toStringList(movieList));
					sync();
					return;
				}
			System.out.println("No Movie Title existed in the database!!");
		}
		System.out.println("=============================================================");
	}
	
	public static List<String> toStringList (List<Movie> movieList)
	{
		List<String> toFile = new ArrayList();
		
		for (Movie movie : movieList)
		{
			StringBuilder st =  new StringBuilder();
			st.append(movie.getTitle());
			st.append(SEPARATOR_IN1);
			st.append(movie.getStatus());
			st.append(SEPARATOR_IN1);
			st.append(movie.getSynopsis());
			st.append(SEPARATOR_IN1);
			st.append(movie.getDirector());
			st.append(SEPARATOR_IN1);
			st.append(movie.getType());
			st.append(SEPARATOR_OUT_CLASS);
			for (String actor : movie.getCast())
			{
				st.append(actor);
				st.append(SEPARATOR_IN);
			}
			st.deleteCharAt(st.length() - 1);
			st.append(SEPARATOR_OUT_CLASS);
			for (Showtime showtime : movie.getShowTime())
			{
				st.append(showtime.getCineplexId());
				st.append(SEPARATOR_IN_SHOWTIME);
				st.append(showtime.getCinemaId());
				st.append(SEPARATOR_IN_SHOWTIME);
				String showtimeStrLong = showtime.getCalendar().getTime().toString();
				String showtimeStr = showtimeStrLong.substring(4,19) + showtimeStrLong.substring(23,28);
				st.append(showtimeStr);
				st.append(SEPARATOR_IN);
			}
			st.deleteCharAt(st.length() - 1);
			st.append(SEPARATOR_OUT_CLASS);
			for (Review review : movie.getReviewList())
			{
				st.append(review.getRating());
				st.append("::");
				st.append(review.getReview());
				st.append(SEPARATOR_IN);
			}
			st.deleteCharAt(st.length() - 1);
			toFile.add(st.toString());
		}
		
		return toFile;
	}
	
	public static void main(String[] aArgs) throws IOException {
		
		/*
		List alw = new ArrayList() ;// to store Professors data
		Scanner scan = new Scanner(System.in);
		boolean moreMovie = true;
		while(moreMovie) {
			StringBuilder st =  new StringBuilder();
			//Movie
			System.out.print("\nEnter the following movie's details : ");
			System.out.print("\n* Title: ");
			String title = scan.nextLine();
			st.append(title);
			st.append(SEPARATOR_IN1);
			System.out.print("\n* Status: ");
			String status = scan.nextLine();
			st.append(status);
			st.append(SEPARATOR_IN1);
			System.out.print("\n* Synopsis: ");
			String synopsis = scan.nextLine();
			st.append(synopsis);
			st.append(SEPARATOR_IN1);
			System.out.print("\n* Director: ");
			String director = scan.nextLine();
			st.append(director);
			st.append(SEPARATOR_IN1);
			System.out.print("\n* Type: ");
			String type = scan.nextLine();
			st.append(type);
			st.append(SEPARATOR_OUT_CLASS);
			System.out.print("\n* Cast: ");
			boolean moreCast = true;
			String cast;
			while(moreCast) {
				cast = scan.nextLine();
				st.append(cast);
				System.out.print("\n* Add actor(y or n): ");
				moreCast = scan.next().equals("y");
				scan.nextLine();
				if(moreCast) {st.append(SEPARATOR_IN); }
			}
			st.append(SEPARATOR_OUT_CLASS);
			System.out.print("\n* showTime: ");
			String movieShowTime;
			boolean moreMovieShowTime = true;
			while(moreMovieShowTime) {
				System.out.print("\n* CineplexId : ");
				String cineplexId = scan.next();
				System.out.print("\n* CinemaId : ");
				String cinemaId = scan.next();
				System.out.print("\n* Days Of Week (Mon to Sun) : ");
				String dayOfWeek = scan.next();
				System.out.print("\n* Day (00 to 31): ");
				String day = scan.next();
				System.out.print("\n* Month (Jan to Dec) : ");
				String month = scan.next();
				System.out.print("\n* Year : ");
				String year = scan.next();
				System.out.print("\n* Hour : ");
				String hour = scan.next();
				System.out.print("\n* Minute : ");
				String minute = scan.next();
				movieShowTime = cineplexId + "|" + cinemaId + "|" + dayOfWeek + " " + month + " " + day + " " + hour + ":" + minute + ":00 " + year;
				st.append(movieShowTime);
				moreMovieShowTime = scan.next().equals("y");
				if(moreMovieShowTime) {st.append(SEPARATOR_IN); }
			}
			st.append(SEPARATOR_OUT_CLASS);
			System.out.print("\n* Rating and Written Review: ");
			String rating;
			String review;
			boolean moreReview = true;
			while(moreReview) {
				System.out.print("\n*Rating: ");
				rating = scan.next();
				scan.nextLine();
				System.out.print("\n*Review: ");
				review = scan.nextLine();
				st.append(rating);
				st.append("::");
				st.append(review);
				System.out.print("\n* Add Movie Review (y or n): ");
				moreReview = scan.nextLine().equals("y");
				if(moreReview) {st.append(SEPARATOR_IN); }
			}
			alw.add(st.toStringList());
			System.out.print("\n* Add Movie (y or n): ");
			moreMovie = scan.nextLine().equals("y");
		}
		write("movie.txt",alw);	
		*/
		MovieController mc = new MovieController("movie.txt");
		for (Movie movie : mc.getMovieList())
			mc.printDetails(movie);
		mc.addMovie();
		for (Movie movie : mc.getMovieList())
			mc.printDetails(movie);
		mc.updateMovie();
		for (Movie movie : mc.getMovieList())
			mc.printDetails(movie);
		mc.deleteMovie();
		for (Movie movie : mc.getMovieList())
			mc.printDetails(movie);
		
	}
}

