
public class Review {
	
	// private data
	private double rating;
	private String writtenReview;
	
	// constructor
	public Review (double rating, String writtenReview)
	{
		this.rating = rating;
		this.writtenReview = writtenReview;
	}
	
	// get & set method
	public double getRating ()
	{
		return rating;
	}
	public String getReview ()
	{
		return writtenReview;
	}
	public void setRating (double rating)
	{
		this.rating = rating;
	}
	public void setReview (String writtenReview)
	{
		this.writtenReview = writtenReview;
	}
	
	// FOR MOVIE-GOERS' REVIEW, I ASSUMED THAT THEY ARE
	// ALLOWED TO CHANGE THEIR THOUGHT ABOUT THE MOVIE
}
