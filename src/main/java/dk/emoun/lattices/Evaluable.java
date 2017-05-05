package MicroC_language.analysis.lattices;

/**
 * Represents objects which are/can be evaluated to a lattice element.
 * @param <V>
 * The type of the lattice element implementers evaluate to.
 */
public interface Evaluable<V extends LatticeElement<V>> {

	/**
	 * 
	 * @return
	 * The lattice element the instance represents currently.<br>
	 * Does not necessarily return the same result with every invocation.
	 */
	public V value();
}
