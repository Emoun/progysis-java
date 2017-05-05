package dk.emoun.progysis.lattices;

public class LatticeUtilities {
	
//CompleteLattice methods
	/**
	 * Returns whether the two lattice elements are equal in the Complete Lattice.
	 * @param element1
	 * @param element2
	 * @param lattice
	 * Complete Lattice over the given elements
	 * @return
	 * {@code true}if the two elements are equal in the lattice, otherwise {@code false}.
	 */
	public static 
		<
		L extends CompleteLattice<V>,
		V extends LatticeElement<V>
		> 
	boolean equal(Evaluable<V> element1, Evaluable<V> element2, L lattice) 
	{
		try {
			return	equalProtected(element1, element2, lattice);
		} catch (IncomparableLatticeException e) {
			return false;
		}
	}
	
	/**
	 * Returns whether the two lattice elements are equal in the Complete Lattice.
	 * @param element1
	 * @param element2
	 * @param lattice
	 * Complete Lattice over the given elements
	 * @return
	 * {@code true}if the two elements are equal in the lattice, otherwise {@code false}.
	 * @throws IncomparableLatticeException
	 */
	public static 
		<
		L extends CompleteLattice<V>,
		V extends LatticeElement<V>
		> 
	boolean equalProtected(Evaluable<V> element1, Evaluable<V> element2, L lattice) 
			throws IncomparableLatticeException
	{
		return 	lattice.compare(element1, element2) &&
				lattice.compare(element2, element1);
	}
	
	/**
	 * Returns whether the given element is equal to the top element in the given lattice.<br>
	 * Note: The top element is defined as {@link CompleteLattice#getTop()}. This can be inconsistent
	 * when using {@link TotalFunction TotalFunctions} since two instance of the same TotalFunction
	 * can map different key, making them incomparable. 
	 * In such a case, this method will return <code>false</code><br>
	 * Alternatively {@link #isTop(TotalFunction)} can be used to mitigate this.
	 * @param element
	 * @param lattice
	 * A complete lattice over the type of the given element
	 * @return
	 * whether the given element is equal to the top element in the given lattice
	 */
	public static 
		<
		L extends CompleteLattice<V>,
		V extends LatticeElement<V>
		> 
	boolean isTop(Evaluable<V> element, L lattice)
	{
		return equal(element, lattice.getTop(), lattice);
	}
	
	/**
	 * Returns whether the given element is equal to the bottom element in the given lattice.<br>
	 * Note: The bottom element is defined as {@link CompleteLattice#getTop()}. This can be inconsistent
	 * when using {@link TotalFunction TotalFunctions} since two instance of the same TotalFunction
	 * can map different key, making them incomparable. 
	 * In such a case, this method will return <code>false</code><br>
	 * Alternatively {@link #isBottom(TotalFunction)} can be used to mitigate this.
	 * @param element
	 * @param lattice
	 * A complete lattice over the type of the given element
	 * @return
	 * whether the given element is equal to the bottom element in the given lattice
	 */
	public static 
		<
		L extends CompleteLattice<V>,
		V extends LatticeElement<V>
		> 
	boolean isBottom(Evaluable<V> element, L lattice)
	{
		return equal(element, lattice.getBottom(), lattice);
	}
	
	/**
	 * Returns whether the two Total Function instances are equal.
	 * @param r1
	 * @param r2
	 * @return
	 * {@code true}if the two functions are equal, otherwise {@code false}.
	 * @throws IncomparableLatticeException
	 */
	public static  <R extends TotalFunction<R,?,?,?>>
	boolean equal(R r1,R r2) 
			throws IncomparableLatticeException
	{
		return 	equal(r1, r2,r1);
	}
	
	/**
	 * Returns whether the given Total Function instance is equal to 
	 * the top element of the Total Function (mapping the same keys).<br>
	 * @param r
	 * Total Function mapping
	 * @return
	 * If the given Total Instance instance maps all its keys to the top element.
	 */
	public static 	<
						R extends TotalFunction<R,K,L,V>,
						K,
						L extends CompleteLattice<V>,
						V extends LatticeElement<V>
					>
	boolean isTop(R r)
	{
		return isTop(r, r);
	}
	
	/**
	 * Returns whether the given Total Function instance is equal to 
	 * the bottom element of the Total Function (mapping the same keys).<br>
	 * @param r
	 * Total Function mapping
	 * @return
	 * If the given Total Instance instance maps all its keys to the bottom element.
	 */
	public static 	<
						R extends TotalFunction<R,K,L,V>,
						K,
						L extends CompleteLattice<V>,
						V extends LatticeElement<V>
					>
	boolean isBottom(R r)
	{
		return isBottom(r, r);
	}
	
}
