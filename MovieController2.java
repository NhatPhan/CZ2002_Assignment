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
import java.util.Collection;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.ParseException;
import java.util.Collections;

public class MovieController {
	private List<Movie> movieList = new ArrayList();
	public static final String SEPARATOR_IN1 = "#";
	public static final String SEPARATOR_IN = ",";
	public static final String SEPARATOR_IN_SHOWTIME = "|";
	public static final String SEPARATOR_OUT_CLASS = "&";
		
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
				out.append((String)data.get(i) + '\n');
			}
	    }
	    finally {
	      out.close();
	    }
	}
	
	
	public void printList(Movie movie) {
		System.out.println(movie.getTitle());
	}
	
	public void searchMoviesByTitle(String title) {
		boolean found = false;
		System.out.println("-----------------------------------------");
		for(int i = 0; i < movieList.size(); i++) {
			if(movieList.get(i).getTitle().toLowerCase().contains(title.toLowerCase())) {
				printList(movieList.get(i));
				found = true;
			}
		}
		if(!found)
			System.out.println("No Movies Found!");
		System.out.println("-----------------------------------------");
	}
	
	public void searchMoviesByStatus(String status) {
		boolean found = false;
		System.out.println("-----------------------------------------");
		for(int i = 0; i < movieList.size(); i++) {
			if(movieList.get(i).getStatus().equals(status)) {
				printList(movieList.get(i));
				found = true;
			}
		}
		if(!found)
			System.out.println("No Movies Found!");
		System.out.println("-----------------------------------------");
	}
	
	public List<Showtime> printDetails(Movie movie) {
		System.out.print("-----------------------------------------------------------------------------------------------------");
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
		System.out.println("\n-----------------------------------------------------------------------------------------------------");
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
			alw.add(st.toString());
			System.out.print("\n* Add Movie (y or n): ");
			moreMovie = scan.nextLine().equals("y");
		}
		write("movie.txt",alw);	
		*/
	}	
}


