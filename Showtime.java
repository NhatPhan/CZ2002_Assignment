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

public class Showtime {
	private String cineplexId;
	private String cinemaId;
	private Calendar date;
	
	public Showtime(String cineplexId, String cinemaId, Calendar date) {
		this.cineplexId = cineplexId;
		this.cinemaId = cinemaId;
		this.date = date;
	}
	public String getCineplexId() {
		return cineplexId;
	}
	public String getCinemaId() {
		return cinemaId;
	}
	public Calendar getCalendar() {
		return date;
	}
	public void setCineplexId(String cineplexId) {
		this.cineplexId = cineplexId;
	}
	public void setCinemaId(String cinemaId) {
		this.cinemaId = cinemaId;
	}
	public void setCalendar(Calendar date) {
		this.date = date;
	}
}






