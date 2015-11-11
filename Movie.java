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
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Movie {
	
	// private data
	private String title;
	private String status;
	private String synopsis;
	private String director;
	private List<String> cast;
	private String type;
	private double overallRating;	// return "NA" if equals 0
	private List<Showtime> showTime;
	private List<Review> reviewList;
	
	// constructor
	public Movie (String title, String status, String synopsis,
			String director, List<String> cast, String type,
			List<Showtime> showTime, List<Review> reviewList)
	{
		this.title = title;
		this.status = status;
		this.synopsis = synopsis;
		this.director = director;
		this.cast = cast;
		this.type = type;
		this.showTime = showTime;
		this.reviewList = reviewList;
		this.overallRating = averaging ();
	}
	
	public Movie() {
		
	}
	
	// update the overallRating every time reviewList is modified
	private double averaging ()
	{
		if (reviewList.size() == 1)
			return 0;
		else
		{
			double total = 0;
			for (int i = 0; i < reviewList.size(); i++)
				total += reviewList.get(i).getRating();
			return total / reviewList.size();
		}
	}
	
	// get & set method
	public void setTitle (String title)
	{
		this.title = title;
	}
	public void setStatus (String status)
	{
		this.status = status;
	}
	public void setSynopsis (String synopsis)
	{
		this.synopsis = synopsis;
	}
	public void setDirector (String director)
	{
		this.director = director;
	}
	public void setCast (List<String> cast)
	{
		this.cast = cast;
	}
	public void setType (String type)
	{
		this.type = type;
	}
	public void setShowTime (List<Showtime> showTime)
	{
		this.showTime = showTime;
	}
	public void setReviewList (List<Review> reviewList)
	{
		this.reviewList = reviewList;
		this.overallRating = averaging ();
	}
	
	public String getTitle ()
	{
		return title;
	}
	public String getStatus ()
	{
		return status;
	}
	public String getSynopsis ()
	{
		return synopsis;
	}
	public String getDirector ()
	{
		return director;
	}
	public List<String> getCast ()
	{
		return cast;
	}
	public String getType ()
	{
		return type;
	}
	public List<Showtime> getShowTime ()
	{
		return showTime;
	}
	public List<Review> getReviewList ()
	{
		return reviewList;
	}
	public double getOverallRating ()
	{
		return overallRating;
	}
}
