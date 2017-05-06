package dk.emoun.progysis.monotoneFramework;

import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;

/**
 * An interface representing monotone function functionality
 * @param <K>
 * The action type the function evaluates
 * @param <T>
 * The state type the function evaluates
 * @param <R>
 * The type of the state which is the result of the function application
 */
public interface MonotoneFunction
	<
		K,
		T extends LatticeElement<T>,
		R extends LatticeElement<?>
	> 
{
	
	/**
	 * 
	 * @param action
	 * @return
	 */
	public boolean applicableOn(K action);
	
	/**
	 * Evaluates the monotone function with 
	 * the given action and state returning the result.
	 * If the result is an equal to the given state, 
	 * the same state instance must be returned, 
	 * otherwise a new state instance is returned.
	 * @param action
	 * @param state
	 * @return
	 * The new state
	 */
	public R apply(K action, Evaluable<T> state);
}
