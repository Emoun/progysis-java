package dk.emoun.progysis.lattices;

public class LatticeUtilities {
	
//CompleteLattice methods
	/**
	 * Returns whether the two lattice elements are equal in their Complete Lattice.
	 * @param element1
	 * @param element2
	 * Complete Lattice over the given elements
	 * @return
	 * {@code true}if the two elements are equal in the lattice, otherwise {@code false}.
	 */
	public static 
		<
		V extends LatticeElement<V>
		> 
	boolean equal(Evaluable<V> element1, Evaluable<V> element2) 
	{
		return 	element1.value().compare(element2) &&
				element2.value().compare( element1);
	}
	
	/**
	 * Returns whether the two lattice elements are unordered in the Complete Lattice,
	 * i.e. (element1 <= element2) == {@code false} and (element2 <= element1) == {@code false}.
	 * @param element1
	 * @param element2
	 * @return
	 * {@code true}if the two elements are unordered in the lattice, otherwise {@code false}.
	 */
	public static 
		<
		V extends LatticeElement<V>
		> 
	boolean unordered(Evaluable<V> element1, Evaluable<V> element2) 
	{
		return 	!element1.value().compare(element2) &&
				!element2.value().compare(element1);
	}
		
	/**
	 * Returns whether the two Total Function instances are equal.
	 * @param r1
	 * @param r2
	 * @return
	 * {@code true}if the two functions are equal, otherwise {@code false}.
	 */
	public static  <R extends TotalFunction<R,?,?,?>>
	boolean equal(R r1,R r2) 
	{
		return 	equal(r1, r2);
	}	
}
