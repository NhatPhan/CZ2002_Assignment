import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class Cineplex {
	
	// private data
	private String id;
	private String location;
	private List<Cinema> cinemaList;
	
	// constructor
	public Cineplex (String id, String location, List<Cinema> cinemaList)
	{
		this.id = id;
		this.location = location;
		this.cinemaList = cinemaList;
	}
	
	// get & set method
	public void setID (String id)
	{
		this.id = id;
	}
	public void setLocation (String location)
	{
		this.location = location;
	}
	public void setCinema (List<Cinema> cinemaList)
	// NOT SURE IF WE'RE GONNA NEED THIS, JUST CREATE IN
	// CASE WE NEED TO MODIFY THE LIST OF CINEMA FOR A
	// SPECIFIC CINEPLEX
	{
		this.cinemaList = cinemaList;
	}
	public String getId ()
	{
		return id;
	}
	public String getLocation ()
	{
		return location;
	}
	public List<Cinema> getCinemaList ()
	{
		return cinemaList;
	}
}
