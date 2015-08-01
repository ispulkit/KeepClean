package fluper.webradic.keepcleanf;


public class bid {

	  private String title;
	    private double rating;
	    
	    public bid(){
	    	
	    	
	    }
	
	    public void setTitle(String name) {
	        this.title = name;
	    }
	 
	    public bid(String name, double rating){
	    	
	    	this.title=name;
	    	this.rating = rating;
	    	
	    }
	    
	    public String getTitle(){
	    	return title;
	    	
	    }

	    public double getRating() {
	        return rating;
	    }
	    
	    public void setRating(double rating) {
	        this.rating = rating;
	    }

}