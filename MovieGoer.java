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

public class MovieGoer {
	public static void main(String[] args) throws IOException {
		MovieController movieCtrl = new MovieController("movie.txt");
		List<Movie> movieList = movieCtrl.getMovieList();
		boolean cont = true;
		Scanner scan = new Scanner(System.in);
		while(cont) {
			System.out.println("\n===================== MOVIE-GOER ============================");
			System.out.println("|1. Search and List movies                                  |");
			System.out.println("|2. View movie details – including reviews and ratings      |");
			System.out.println("|3. View booking history                                    |");
			System.out.println("|4. List the Top 5 ranking by overall reviewers’ ratings    |");
			System.out.println("=============================================================");
			int choice = scan.nextInt();
			List<Showtime> showtime = new ArrayList<Showtime>();
			List<Seat> seatList = new ArrayList<Seat>();
			List<TicketInfo> ticketInfo = new ArrayList<TicketInfo>();
			Movie movie = new Movie();
			int showtimeNumber = 0;
			switch(choice) {
				case 1:
					boolean research = true;
					while(research) {
						System.out.println("\n===================== SEARCH & LIST =========================");
						System.out.println("|1. Movies's Title											|");
						System.out.println("|2. Now Showing Movies										|");
						System.out.println("|3. Coming Soon Movies										|");
						System.out.println("=============================================================");
						int searchType = scan.nextInt();
						switch(searchType) {
							case 1:
								System.out.print("Enter your search term for the movie's title: ");
								scan.nextLine();
								String title = scan.nextLine();
								movieCtrl.searchMoviesByTitle(title);
								break;
							case 2:
								movieCtrl.searchMoviesByStatus("Now Showing");
								break;
							case 3:
								movieCtrl.searchMoviesByStatus("Coming Soon");
								break;
							default:
								break;
						}
						System.out.print("Do you want to change your search (y or n) : ");
						research = scan.next().equals("y");
					}
					break;
				case 2:
					boolean viewDetail = true;
					while(viewDetail) {
						System.out.println("\n==================== VIEW MOVIE DETAILS =====================");
						System.out.print("Enter the (exact) movie's title to view details: ");
						scan.nextLine();
						String title = scan.nextLine();
						boolean movieFound = false;
						for(int j = 0; j < movieList.size(); j++) {
							if(movieCtrl.getMovieList().get(j).getTitle().equals(title)) {
								movie = movieCtrl.getMovieList().get(j);
								movieFound = true;
								
							} 
						}
						if(!movieFound || movie.getStatus().equals("End Of Showing")) {
							System.out.println("\nThetitle you enter is not correct or The movie is End Of Showing.");
							System.out.println("You can choose Search For Movies for the correct title or View Details to enter again");
							System.out.println("=============================================================");
							System.out.println();
							viewDetail = false;
						}
						else {
							List<Ticket> ticketList = new ArrayList<Ticket>();
							showtime = movieCtrl.printDetails(movie);
							System.out.println("\n===================== MOVIE OPTIONS ==========================");
							System.out.println("|1. Check seat availability and select seat for this movie	|");
							System.out.println("|2. View Other Movie's Details								|");		
							System.out.println("|3. Exit													|");
							System.out.println("=============================================================");
							int case2Choice = scan.nextInt();
							String[] MOVIETYPEPRICE_DF = {"2D", "3D", "IMAX"};
							String[] CLASSPRICE_DF = {"Platinum Movie Suites", "Elite", "Ultima", "Dolby Atmos"};
							String[] AGEPRICE_DF = {"Children(ages 2-12)", "Adults(ages 13 & up)", "Seniors(ages 65+)", "Student Discount"};
							String[] DAYPREFPRICE_DF = {"Mon - Thu", " Sun and eve of PH & PH"};
							switch(case2Choice) {
								case 1:
									System.out.print("Enter the index of your desire showtime: ");
									showtimeNumber = scan.nextInt();
									movieCtrl.checkAndSelectSeats(showtime.get(showtimeNumber - 1));
									System.out.print("Enter the number of seats you want to select: ");
									scan.nextLine();
									int numOfSeat = scan.nextInt();
									System.out.print("\nEnter your desire seats: ");
									for(int i = 0; i < numOfSeat; i++) {
										String seatId = scan.next();
										Seat seat = new Seat(seatId);
										seatList.add(seat);
									}
									System.out.println("\n=================== TICKET BOOKING ==========================");
									System.out.println("|1. Book and purchase ticket with selected seats			|");
									System.out.println("|2. Exit													|");
									System.out.println("=============================================================");
									if(scan.nextInt() == 2)
										viewDetail = false;
									else {
										double totalPrice = 0;
										System.out.println("\n==================== TICKET CHOICE ==========================");
										scan.nextLine();
										for(int i = 0; i < seatList.size(); i++) {
											System.out.printf("\n%dst Ticket : ",i + 1);
											System.out.print("\n* Name : ");
											String name = scan.nextLine();
											System.out.print("* Mobile : ");
											String mobile = scan.nextLine();
											System.out.print("* Email : ");
											String email = scan.nextLine();
											System.out.print("* Class Pref (1.Platinum Movie Suites, 2.Elite, 3.Ultima, 4.Dolby Atmos) : ");
											String classPref = CLASSPRICE_DF[scan.nextInt() - 1];
											System.out.print("* Age Pref (1.Children(ages 2-12), 2.Adults(ages 13 & up), 3.Seniors(ages 65+), 4.Student Discount) : ");
											String age = AGEPRICE_DF[scan.nextInt() - 1];
											if(i < seatList.size() - 1) 
												scan.nextLine();
											String dayPref;
											switch(showtime.get(showtimeNumber - 1).getCalendar().DAY_OF_WEEK) {
												case 0:
												case 6:
													dayPref = DAYPREFPRICE_DF[1];
													break;
												default:
													dayPref = DAYPREFPRICE_DF[0];
													break;
											}
											Customer newCustomer = new Customer(name, mobile, email);
											Ticket newticket = new Ticket(newCustomer, movie, classPref, age, dayPref, seatList.get(i), showtime.get(showtimeNumber - 1).getCalendar());
											TicketInfo newTincketInfo = new TicketInfo(newCustomer, movie.getTitle(), seatList.get(i).getId(), classPref, showtime.get(showtimeNumber - 1));
											totalPrice += newticket.printTicket();
											ticketList.add(newticket);	
											ticketInfo.add(newTincketInfo);
										}
										System.out.println("=============================================================\n");
										System.out.print("Proceed to purchase the above tickets (y) or exit(n): ");
										boolean purchase = scan.next().equals("y");
										if(purchase) {
											Calendar cal = Calendar.getInstance();
											Transaction transaction;
											Random rand = new Random();
											int randomNum = rand.nextInt(1000) + 1;
											String transactionid = Integer.toString(randomNum);
											transaction = new Transaction(transactionid, cal.getTime().toString() , totalPrice, ticketInfo);
											TransactionController transCtrl = new TransactionController("transaction.txt");
											transCtrl.add(transaction);
											transCtrl.getTransHistory(transaction.getId());
											CineplexController cineplexCtrl = new CineplexController("cineplex.txt");
											cineplexCtrl.updateCineplex(ticketList);
											viewDetail = false;
										}
										System.out.println("=============================================================");
									}
									break;				
								case 2:
									break;
								case 3:
									viewDetail = false;
									break;	
							}
						}
						System.out.println("=============================================================\n");
					}
					break;
				case 3:
					System.out.print("Enter your transaction ID: ");
					String id = scan.next();
					TransactionController transCtrl = new TransactionController("transaction.txt");
					transCtrl.getTransHistory(id);
					break;
				case 4:
					movieCtrl.printTopMovie();
					break;
			}
		}
	}
}
