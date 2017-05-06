package dk.emoun.progysis.lattices;

/**
 * Represents a complete lattice over lattice elements V.<br>
 * The interface specifies a set of method related to all Complete Lattices.<br>
 * Implementations of the methods of this interface should not change the internal state 
 * of the lattice instance.<br>
 * <br>
 * Conventionally, each implementing class should have a public static final field
 * of a default instance of the lattice class, that can be used to manipulate lattice elements.
 * This field should be named 'I'. E.g:<br>
 * <br>
 * class ExampleLattice extends CompleteLattice<SignSet>&#123;<br> 
 * public static final ExampleLattice I = new ExampleLattice();<br>
 * ...<br>
 * &#125;
 * 
 * 
 * @param <V>
 * The type of the elements of the Complete lattice implementation.<br>
 */
public interface CompleteLattice<V extends LatticeElement<V>> {
	
//methods
	
	/**
	 * 
	 * @return
	 * the bottom element (the least element) of the complete lattice.<br>
	 */
	public abstract V getBottom();
	
	/**
	 * Returns whether the given element is the bottom element of the lattice.
	 * @param e
	 * Element of the lattice
	 * @return
	 * Whether the given element is the top element
	 */
	public boolean isBottom(Evaluable<V> e);
	
	/**
	 * Compares the ordering of the two elements in the complete lattice.<br>
	 * Returns whether e1 <= e2 (e1 is more precise than e2).<br>
	 * Note: calling 'compare(getBottom(), getTop) must return {@code true} (by the definition of top and bottom)<br>
	 * Note: by definition, if compare(e1,e2)==true and compare(e2,e1)==true then e1 is the same element as e2 in the lattice. 
	 * @param e1
	 * Element of the lattice
	 * @param e2
	 * Element of the lattice
	 * @return
	 * Whether e1 <= e2 (e1 is more precise than e2)
	 * @throws IncomparableLatticeException
	 * If e1 and e2 are incomparable in the lattice.
	 */
	public abstract boolean compare(Evaluable<V> e1, Evaluable<V> e2) throws IncomparableLatticeException;
		
	/**
	 * Joins the two lattice elements returning the most precise (least fixed point) element
	 * in the lattice larger than both e1 and e2:<br>
	 * <br>
	 * compare(e1, join(e1, e2)) == true && 
	 * compare(e2, join(e1, e2)) == true<br>
	 * <br>
	 * The returned instance must be new and independent element of the lattice.<br>
	 * Additionally, should not alter the given elements.
	 * @param e1
	 * Element of the lattice
	 * @param e2
	 * Element of the lattice
	 * @return
	 * the most precise (least fixed point) element
	 * in the lattice larger than both e1 and e2
	 */
	public abstract V join(Evaluable<V> e1, Evaluable<V> e2);
	
	/**
	 * @param e
	 * Element of the lattice
	 * @return
	 * a string representation of the element in the lattice
	 */
	public String stringRepresentation(Evaluable<V> e);
}
