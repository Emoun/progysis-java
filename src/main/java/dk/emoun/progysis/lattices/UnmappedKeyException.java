package dk.emoun.progysis.lattices;

/**
 * Thrown to indicate that a {@link TotalFunction Total Function} instance does not map 
 * a specified key.
 */
public class UnmappedKeyException extends RuntimeException{
	
	/**
	 * Construct a new UnmappedKeyException indicating
	 * a {@link TotalFunction} instance does not map the given key value.
	 * @param key
	 */
	public <K> UnmappedKeyException(K key){
		super(key.toString());
	}
}
