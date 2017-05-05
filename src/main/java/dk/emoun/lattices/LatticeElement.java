package MicroC_language.analysis.lattices;

/**
 * Defines a Complete Lattice element.<br>
 * All lattice elements evaluate to the exact same instance 
 * as the one the {@link #value} method was invoked on.
 * @param <V>
 * The type of the implementing lattice element
 */
public interface LatticeElement
		<
		V extends LatticeElement<V>
		> 
		extends Evaluable<V>
{

}
