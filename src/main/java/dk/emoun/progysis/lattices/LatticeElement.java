package dk.emoun.progysis.lattices;

/**
 * Defines a Complete Lattice element.<br>
 * All lattice elements evaluate to the exact same instance 
 * as the one the {@link #value} method was invoked on.<br>
 * Instantiating a LatticeElement using the empty constructor must result 
 * in the bottom element of the lattice.
 * @param <V>
 * The type of the implementing lattice element
 */
public abstract class LatticeElement
		<
		V extends LatticeElement<V>
		> 
		implements Evaluable<V>, CompleteLattice<V>
{
	
//Overriding methods
	@Override
	public V value(){
		return (V)this;
	}
}
