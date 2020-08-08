package misc.proteinalignment;

/** simple alphabet exception class
 * http://www.cs.utexas.edu/~mobios/cs329e/rosetta/src/InvalidAlphabetException.java
 * @author smriti
 *
 */

public class InvalidAlphabetException extends RuntimeException {
	
	public InvalidAlphabetException (char alphabet, String message) {
		super ("Alphabet " + alphabet + ": " + message) ;
	}	

	public InvalidAlphabetException (String message) {
		super (message) ;
	}	

}
