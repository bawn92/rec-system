package alg.cases;

import java.util.Set;

public interface Case {
	
	/**
	 * @returns the case id
	 */
	public Integer getId();
	public Set<String> getDirectors();
	public Set<String> getActors();
	public Set<String> getGenres();
	
}
