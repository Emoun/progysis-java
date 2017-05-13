package dk.emoun.progysis.lattices;

/**
 * Represents a complete lattice over lattice elements V.<br>
 * The interface specifies a set of method related to all Complete Lattices.<br>
 * <br>
 * 
 * @param <V>
 * The type of the elements of the Complete lattice implementation.<br>
 */
public interface CompleteLattice<V extends LatticeElement<V>> {
	
//methods
	
	/**
	 * @return
	 * the bottom element (the least element) of the complete lattice.<br>
	 */
	public V getBottom();
	
	/**
	 * Returns whether the lattice element is the bottom element of the lattice.
	 * @return
	 * Whether the element is the bottom element
	 */
	public boolean isBottom();
	
	/**
	 * Compares the ordering of the two elements in the complete lattice.<br>
	 * Returns whether {@code this} <= {@code other} ({@code this} is more precise than {@code other}).<br>
	 * Note: by definition, if e1.compare(e2)==true and e2.compare(e1)==true then e1 is the same element as e2 in the lattice. 
	 * @param other
	 * Element of the lattice
	 * @return
	 * Whether {@code this} <= {@code other}
	 */
	public abstract boolean compare(Evaluable<V> other);
		
	/**
	 * Joins the current instance with the given elements returning the most precise (least fixed point) element
	 * in the lattice larger than both:<br>
	 * <br>
	 * Given j = e1.join(e2) then 
	 * e1.compare(j) == true && 
	 * e2.compare(j) == true<br>
	 * <br>
	 * @param other
	 * Element of the lattice
	 * @return
	 * the most precise (least fixed point) element
	 * in the lattice larger than both the invoked element and the given.
	 */
	public abstract V join(Evaluable<V> other);
	
	/**
	 * @return
	 * a string representation of the invoked element in the lattice
	 */
	public String stringRepresentation();
}
