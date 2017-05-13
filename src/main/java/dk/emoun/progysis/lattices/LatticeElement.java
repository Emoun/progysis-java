package dk.emoun.progysis.lattices;

/**
 * Defines a Complete Lattice element.<br>
 * All lattice elements evaluate to the exact same instance 
 * as the one the {@link #value} method was invoked on.
 * @param <V>
 * The type of the implementing lattice element
 */
public abstract class LatticeElement
		<
		V extends LatticeElement<V>
		> 
		implements Evaluable<V>, CompleteLattice<V>
{
	
}
