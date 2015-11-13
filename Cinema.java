import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Stores information about a cinema for a specific showtime, including its identity and seat arrangement
 * @author Owlie
 *
 */
public class Cinema {
	
	// private data
	/**
	 * Cinema identity to system
	 */
	private String id;
	/**
	 * Show time of a specific movie at this cinema
	 */
	private String showtime;
	/**
	 * Cinema's seat arrangement for the movie time slot
	 */
	private List<Seat> seatList;
	
	// constructor
	/**
	 * Constructs new instance of seat arrangement with specified size
	 * @param id identity of the cinema to show movie
	 * @param showtime timeslot of the movie at this cinema
	 * @param rowLim max. number of seat rows 
	 * @param numLim max. number of seat columns
	 */
	public Cinema (String id, String showtime, int rowLim, int numLim)
	{
		this.id = id;
		this.showtime = showtime;
		seatGen (rowLim, numLim);		
	}
	
	/**
	 * Constructs new instance of seat arrangement with pre-defined seat data
	 * @param id identity of the cinema to show movie
	 * @param showtime timeslot of the movie at this cinema
	 * @param seatList pre-defined seat data
	 */
	public Cinema (String id, String showtime, List<Seat> seatList) 
	{
		this.id = id;
		this.showtime = showtime;
		this.seatList = seatList;
	}
	
	// built-in seat generator
	/**
	 * Built-in seat arrangement generator for constructing specified size of cinema 
	 * @param rowLim max. number of seat rows
	 * @param numLim max. number of seat columns
	 */
	private void seatGen (int rowLim, int numLim)
	{
		seatList = new ArrayList<Seat>(rowLim * numLim);
		char row = 'A';
		for (int i = 0; i < rowLim; i++)
			for (int j = 0; j < numLim; j++)
			{
				String seatID = Character.toString((char) (row + i)) + (j + 1);
				Seat newSeat = new Seat(seatID);
				seatList.add(newSeat);
			}
	}
	
	// get & set method
	/**
	 * Returns the identity of the cinema
	 * @return identity of the cinema
	 */
	public String getId ()
	{
		return id;
	}
	/**
	 * Returns the timeslot of the movie at this cinema
	 * @return timeslot of the movie at this cinema
	 */
	public String getShowtime ()
	{
		return showtime;
	}
	/**
	 * Returns the seat arrangement of the cinema at this timeslot
	 * @return the seat arrangement of the cinema at this timeslot
	 */
	public List<Seat> getSeatList ()
	{
		return seatList;
	}
	/**
	 * Sets the identity of the cinema
	 * @param id new identity of the cinema
	 */
	public void setID (String id)
	{
		this.id = id;
	}
	/**
	 * Sets the timeslot of the movie at the cinema
	 * @param showtime new timeslot of the movie at the cinema 
	 */
	public void setShowtime (String showtime)
	{
		this.showtime = showtime;
	}
	/**
	 * Returns number of rows of the seat arrangement
	 * @return number of rows of the seat arrangement
	 */
	public int countRows() 
	{
		int count = 0;
		String firstrowChar = seatList.get(0).getId().substring(0,1);
		for(Seat seat : seatList) {
			if(seat.getId().substring(0,1).equals(firstrowChar))
				count++;
			else
				break;
		}
		return count;
	}
	/**
	 * Prints seat arrangement outline for movie-goers to choose seats
	 * ~~ indicates the cinema screen
	 * XX indicates occupied seats
	 * A1 indicates free seats
	 * @param rowLim horizontal size of the seat arrangement
	 */
	public void printSeats(int rowLim) {
		List<String> occupiedSeat = new ArrayList();
		char row = 'A';
		int numLim = seatList.size() / rowLim;
		for(Seat seat : seatList) {
			if(seat.isOccupied())
				occupiedSeat.add(seat.getId());
		}
		System.out.print("\t");
		for (int i = 0; i < 3 * rowLim + 3; i++)
			System.out.print("#");
		System.out.print("\n\t# ");
		for (int i = 0; i < 3 * rowLim - 1; i++) {
				System.out.print("~");
		}
		System.out.println(" #");
		for (int i = 0; i < numLim; i++) {
			System.out.print("\t# ");
			for (int j = 0; j < rowLim; j++) {
				boolean occupied = false;
				String seatId = Character.toString((char) (row + i)) + (j + 1);
				for(String id : occupiedSeat) {
					if(id.equals(seatId)) 
						occupied = true;
				}
				if(occupied)
					System.out.print("XX");
				else
					System.out.print(seatId);
				System.out.print(" ");
			}
			System.out.println("#");
		}
		System.out.print("\t");
		for (int i = 0; i < 3 * rowLim + 3; i++)
			System.out.print("#");
		System.out.println();
	}
}
