package MicroC_language.analysis.lattices;

/**
 * Thrown to indicate that two elements of a {@link CompleteLattice Complete Lattice} are
 * incomparable.
 */
public class IncomparableLatticeException extends Exception {
	
//Fields
	
//Constructors
	public IncomparableLatticeException(){
		super();
	}
	
	/**
	 * Constructs an IncomparableLatticeException class with the specified detail message.
	 * @param msg
	 */
	public IncomparableLatticeException(String msg){
		super(msg);
	}
	
	/**
	 * Constructs an IncomparableLatticeException class with a detail message
	 * containing the string representations of the elements that are incomparable.
	 * @param e1
	 * element of the lattice
	 * @param e2
	 * element of the lattice
	 * @param lattice
	 * where the elements e1 and e2 are incomparable
	 */
	public 	<
			L extends CompleteLattice<V>, 
			V extends LatticeElement<V>
			>
		IncomparableLatticeException(V e1, V e2, L lattice)
	{
		super("Incomparable elements: " + 
				lattice.stringRepresentation(e1) + 
				"," + 
				lattice.stringRepresentation(e2));
	}
	
}
