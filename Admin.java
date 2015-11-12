import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Admin {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MovieController movieCtrl = new MovieController("movie.txt");
		List<Movie> movieList = movieCtrl.getMovieList();
		Scanner sc = new Scanner(System.in);
		boolean cont = true;
		while(cont) {
			System.out.println("\n======================== ADMIN ==============================");
			System.out.println("|1. Create New Movie                                        |");
			System.out.println("|2. Update Existing Movie                                   |");
			System.out.println("|3. Remove Movie (End of Showing):                          |");
			System.out.println("|4. Change System Settings                                  |");
			System.out.println("|5. Exit                                                    |");
			System.out.println("=============================================================");
			int choice = sc.nextInt();
			switch (choice)
			{
				case 1:
					System.out.println("================== CURRENT MOVIE DATABASE ===================");
					for (Movie movie : movieList)
						movieCtrl.printList(movie);
					movieCtrl.addMovie();
					System.out.println("================== MOVIE DATABASE IS UPDATED! ===================");
					break;
				case 2:
					System.out.println("================== CURRENT MOVIE DATABASE ===================");
					for (Movie movie : movieList)
						movieCtrl.printList(movie);
					movieCtrl.updateMovie();
					System.out.println("================== MOVIE DATABASE IS UPDATED! ===================");
					break;
				case 3:
				try {
					System.out.println("================== CURRENT MOVIE DATABASE ===================");
					for (Movie movie : movieList)
						movieCtrl.printList(movie);
					movieCtrl.deleteMovie();
					System.out.println("================== MOVIE DATABASE IS UPDATED! ===================");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					break;
				case 4:
					PriceController priceCtrl = new PriceController("pricesettings.txt");
					System.out.println("================== CURRENT PRICE DATABASE ===================");
					priceCtrl.configureSetting();
					System.out.println("================== PRICE DATABASE IS UPDATED! ===================");
					break;
				case 5:
					cont = false;
					break;
				default:
					break;
			}

		}
	}
}
