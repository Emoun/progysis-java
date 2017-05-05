package dk.emoun.progysis.worklist;

import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;

/**
 * A constraint type that evaluates to the given value
 * @param <V> 
 * the type of the value
 */
public class BaseConstraint<V extends LatticeElement<V>> implements Evaluable<V>{
	
//Fields
	/**
	 * The lattice element the instance evaluates to
	 */
	private V element;
	
//Constructors
	/**
	 * Constructs a constraint that evaluates to the given element
	 * @param e
	 */
	public BaseConstraint(V e) {
		this.element = e;
	}
	
//Methods

	@Override
	public V value() {
		return element;
	}
}
