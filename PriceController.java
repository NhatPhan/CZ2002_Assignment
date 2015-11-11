import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.Console;

public class PriceController {
	private PriceSettings settings;
	public static final String SEPARATOR_OUT = "|";
	public static final String SEPARATOR_IN = ",";
	public static final String[] MOVIETYPEPRICE_DF = {"2D", "3D", "IMAX"};
	public static final String[] CLASSPRICE_DF = {"Platinum Movie Suites", "Elite", "Ultima", "Dolby Atmos"};
	public static final String[] AGEPRICE_DF = {"Children(ages 2-12)", "Adults(ages 13 & up)", "Seniors(ages 65+)", "Student Discount"};
	public static final String[] DAYPREFPRICE_DF = {"Mon - Thu", " Sun and eve of PH & PH"};
	
	public PriceController(String filename) {
		// read String from text file
		try {
			ArrayList priceArray = (ArrayList)read(filename);
			String st = (String)priceArray.get(0);
			
			// get individual 'fields' of the string separated by SEPARATOR
			StringTokenizer star = new StringTokenizer(st , SEPARATOR_OUT);	// pass in the string to the string tokenizer using delimiter "|"

			String  movieTypePrice_str = star.nextToken().trim();	// first token
			String  classPrice_str = star.nextToken().trim();	// second token
			String  agePrice_str = star.nextToken().trim();	// third token
			String  dayPrefPrice_str = star.nextToken().trim();	// fourth token
			
			StringTokenizer movieTypePrice_star = new StringTokenizer(movieTypePrice_str, SEPARATOR_IN);
			StringTokenizer classPrice_star = new StringTokenizer(classPrice_str, SEPARATOR_IN);
			StringTokenizer agePrice_star = new StringTokenizer(agePrice_str, SEPARATOR_IN);
			StringTokenizer dayPrefPrice_star = new StringTokenizer(dayPrefPrice_str, SEPARATOR_IN);
			
			double[] movieTypePrice = new double[3];
			double[] classPrice = new double[4];
			double[] agePrice = new double[4];
			double[] dayPrefPrice = new double[2];
			
			//get movieTypePrice values
			for(int i = 0; i < movieTypePrice.length; i++) {
				movieTypePrice[i] = Double.parseDouble(movieTypePrice_star.nextToken().trim());
			}
			
			//get classPrice values
			for(int i = 0; i < classPrice.length; i++) {
				classPrice[i] = Double.parseDouble(classPrice_star.nextToken().trim());
			}
			
			//get agePrice values
			for(int i = 0; i < agePrice.length; i++) {
				agePrice[i] = Double.parseDouble(agePrice_star.nextToken().trim());
			}
			
			//get dayPrefPrice values
			for(int i = 0; i < dayPrefPrice.length; i++) {
				dayPrefPrice[i] = Double.parseDouble(dayPrefPrice_star.nextToken().trim());
			}
			
			settings = new PriceSettings(movieTypePrice, classPrice, agePrice, dayPrefPrice);
			
		}catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
	}
	
	public static void modify(String filename, int typeOfSetting, double[] data) throws IOException {
		List alw = new ArrayList() ;// to store Professors data
		try {
			PriceController price_ctrl = new PriceController(filename);
			PriceSettings settings = price_ctrl.getPriceSettings();
			double[] movieTypePrice = settings.getMovieTypePrice();
			double[] classPrice = settings.getClassPrice();
			double[] agePrice = settings.getAgePrice();
			double[] dayPrefPrice = settings.getDayPrefPrice();
			switch(typeOfSetting) {
				case 1: //movieTypePrice
					movieTypePrice = data;
					break;
				case 2: //classPrice
					classPrice = data;
					break;
				case 3: //agePrice
					agePrice = data;
					break;
				case 4: //dayPrefPrice
					dayPrefPrice = data;
					break;
			}
			StringBuilder st =  new StringBuilder();
			for(double mTP : movieTypePrice) {
				st.append(mTP);	
				st.append(SEPARATOR_IN);
			}
			st.append(SEPARATOR_OUT);
			for(double cP : classPrice) {
				st.append(cP);	
				st.append(SEPARATOR_IN);	
			}
			st.append(SEPARATOR_OUT);
			for(double aP : agePrice) {
				st.append(aP);	
				st.append(SEPARATOR_IN);
			}
			st.append(SEPARATOR_OUT);
			for(double dPP : dayPrefPrice) {
				st.append(dPP);	
				st.append(SEPARATOR_IN);	
			}
			alw.add(st.toString());
			write(filename,alw);	
			price_ctrl = new PriceController(filename);
			PriceSettings new_settings = price_ctrl.getPriceSettings();
			settings = new_settings;
		}catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}	
		
	}
	
	public PriceSettings getPriceSettings() {return settings; }
	
	public void printSettings() {
		double[] movieTypePrice = settings.getMovieTypePrice();
		double[] classPrice = settings.getClassPrice();
		double[] agePrice = settings.getAgePrice();
		double[] dayPrefPrice = settings.getDayPrefPrice();
		int i;
		System.out.println("Price of Movie Types : ");
		for(i = 0; i < movieTypePrice.length; i++) {
			System.out.printf("*** %s : %.2f\n", MOVIETYPEPRICE_DF[i], movieTypePrice[i]);	
		}
		System.out.println("\nPrice of Class Types : ");
		for(i = 0; i < classPrice.length; i++) {
			System.out.printf("*** %s : add %.2f\n", CLASSPRICE_DF[i],classPrice[i]);	
		}
		System.out.println("\nPrice of Age Types : ");
		for(i = 0; i < agePrice.length; i++) {
			System.out.printf("*** %s : add %.2f\n", AGEPRICE_DF[i], agePrice[i]);	
		}
		System.out.println("\nPrice of Day Pref Types : ");
		for(i = 0; i < dayPrefPrice.length; i++) {
			System.out.printf("*** %s : add %.2f\n", DAYPREFPRICE_DF[i], dayPrefPrice[i]);	
		}
	}
	
	public double calculatePrice(Ticket ticket) {
		Movie movie = ticket.getMovie();
		String movieType = movie.getType();
		String classPref = ticket.getClassPref();
		String age = ticket.getAge();
		String dayPref = ticket.getDayPref();
		int index_MovieType = 0;
		int index_class = 0;
		int index_age = 0;
		int index_dayPref = 0;
		for(int i = 0; i < MOVIETYPEPRICE_DF.length; i++){
			if(MOVIETYPEPRICE_DF[i].equals(movieType))
				index_MovieType = i;
		}
		for(int i = 0; i < CLASSPRICE_DF.length; i++){
			if(CLASSPRICE_DF[i].equals(classPref))
				index_class = i;
		}
		for(int i = 0; i < AGEPRICE_DF.length; i++){
			if(AGEPRICE_DF[i].equals(age))
				index_age = i;
		}
		for(int i = 0; i < DAYPREFPRICE_DF.length; i++){
			if(DAYPREFPRICE_DF[i].equals(dayPref))
				index_dayPref = i;
		}
		double[] movieTypePrice = settings.getMovieTypePrice();
		double[] classPrice = settings.getClassPrice();
		double[] agePrice = settings.getAgePrice();
		double[] dayPrefPrice = settings.getDayPrefPrice();
		double totalPrice = movieTypePrice[index_MovieType] + classPrice[index_class] + agePrice[index_age] + dayPrefPrice[index_dayPref];
		return totalPrice;
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
			for (int i =0; i < data.size() ; i++) {
				out.println((String)data.get(i));
			}
	    }
	    finally {
	      out.close();
	    }
	}
	
	/*
	public static void main(String[] aArgs) throws IOException {
		try {
			Scanner scan = new Scanner(System.in);
			PriceController price_ctrl = new PriceController("pricesettings.txt");
			PriceSettings settings = price_ctrl.getPriceSettings();
			price_ctrl.printSettings();
			System.out.println("Which kind of price setting do you want to modify (1: movie type, 2: class, 3: age, 4: day preference) : ");
			int typeOfSetting = scan.nextInt();
			switch(typeOfSetting) {
				case 1:
					double[] data = settings.getMovieTypePrice();
					System.out.print("Enter the price settings for movie types : ");
					for(int i = 0; i < data.length; i++) {
						System.out.printf("\n%s : ",MOVIETYPEPRICE_DF[i]);
						data[i] = scan.nextDouble();
					}
					price_ctrl.modify("pricesettings.txt", typeOfSetting, data);
					break;
				case 2:
					data = settings.getClassPrice();
					System.out.print("Enter the price settings for class : ");
					for(int i = 0; i < data.length; i++) {
						System.out.printf("\n%s : ",CLASSPRICE_DF[i]);
						data[i] = scan.nextDouble();
					}
					price_ctrl.modify("pricesettings.txt", typeOfSetting, data);
					break;
				case 3:
					data = settings.getAgePrice();
					System.out.print("Enter the price settings for movie types : ");
					for(int i = 0; i < data.length; i++) {
						System.out.printf("\n%s : ",AGEPRICE_DF[i]);
						data[i] = scan.nextDouble();
					}
					price_ctrl.modify("pricesettings.txt", typeOfSetting, data);
					break;
				case 4:
					data = settings.getDayPrefPrice();
					System.out.print("Enter the price settings for movie types : ");
					for(int i = 0; i < data.length; i++) {
						System.out.printf("\n%s : ",DAYPREFPRICE_DF[i]);
						data[i] = scan.nextDouble();
					}
					price_ctrl.modify("pricesettings.txt", typeOfSetting, data);
					break;
			}
			price_ctrl.printSettings();
		}catch (IOException e) {
			System.out.println("IOException > " + e.getMessage());
		}
	} 
	*/

}
