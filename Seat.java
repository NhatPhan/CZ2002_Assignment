import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class Seat {
	
	// private data
	private String ID;
	private boolean isOccupied = false;
	
	// constructor
	public Seat (String ID)
	{
		this.ID = ID;
	}
	
	public Seat (String ID, boolean isOccupied) 
	{
		this.ID = ID;
		this.isOccupied = isOccupied;
	}
	
	// get & set method
	public String getId ()
	{
		return ID;
	}
	public boolean isOccupied ()
	{
		return isOccupied;
	}
	public void bookSeat ()
	{
		isOccupied = true;
	}
	public void unbookSeat ()
	{
		isOccupied = false;
	}
	
}
