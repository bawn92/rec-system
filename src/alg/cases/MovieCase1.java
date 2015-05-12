package alg.cases;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MovieCase1 implements Case
{
	private Integer id; // the case id
	private String title; // the movie title
	private Set<String> genres; // the movie genres
	private Set<String> directors; // the movie directors
	private Set<String> actors; // the lead actors
	private Double mean;
	private Double pop;
	
	/**
	 * constructor - creates a new MovieCase object
	 * @param id - the case id
	 */
	public MovieCase1(final Integer id) {
		this.id = id;
		title = null;
		genres = new HashSet<String>();
		directors = new HashSet<String>();
		actors = new HashSet<String>();
		mean = Double.NaN;
		pop = Double.NaN;
	}
	
	/**
	 * constructor - creates a new MovieCase object
	 * @param id - the case id
	 * @param title - the movie title
	 * @param genres - the movie genres
	 * @param directors - the movie directors
	 * @param actors - the lead actors
	 */
	public MovieCase1(final Integer id, final String title, final ArrayList<String> genres, final ArrayList<String> directors, final ArrayList<String> actors, final Double mean, final Double pop)
	{
		this(id);
		this.title = title;
		
		for(String genre: genres)
			this.genres.add(genre);
		
		for(String director: directors)
			this.directors.add(director);
		
		for(String actor: actors)
			this.actors.add(actor);
		
		this.mean = mean;
		
		this.pop = pop;
		
	}
	
	/**
	 * @returns the case id
	 */
	public Integer getId()
	{
		return id;
	}
	
	public Double get_pop(){

		return  pop;
	}
	
	public Double getMean(){
		
		
		return mean;
	}
	
	/**
	 * @returns the movie title
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * @returns the movie genres
	 */
	public Set<String> getGenres()
	{
		return genres;
	}
	
	/**
	 * @returns the movie directors
	 */
	public Set<String> getDirectors()
	{
		return directors;
	}
	
	/**
	 * @returns the lead actors
	 */
	public Set<String> getActors()
	{
		return actors;
	}
	
	/**
	 * @returns a string representation of the MovieCase object
	 */
	public String toString()
	{
		return id + " " + title + " " + genres.toString() + " " + directors.toString() + " " + actors.toString();
	}
}
