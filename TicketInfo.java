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

public class TicketInfo {
	private Customer customer;
	private String movieTitle;
	private String seatId;
	private String classPref;
	private Showtime showtime;
	
	public TicketInfo(Customer customer, String movieTitle, String seatId, String classPref, Showtime showtime) {
		super();
		this.customer = customer;
		this.movieTitle = movieTitle;
		this.seatId = seatId;
		this.classPref = classPref;
		this.showtime = showtime;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public String getSeatId() {
		return seatId;
	}

	public void setSeatId(String seatId) {
		this.seatId = seatId;
	}

	public String getClassPref() {
		return classPref;
	}

	public void setClassPref(String classPref) {
		this.classPref = classPref;
	}

	public Showtime getShowtime() {
		return showtime;
	}

	public void setShowtime(Showtime showtime) {
		this.showtime = showtime;
	}
	
	
}
