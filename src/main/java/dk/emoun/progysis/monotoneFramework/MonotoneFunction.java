package dk.emoun.progysis.monotoneFramework;

import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;

/**
 * An interface representing monotone function functionality
 * @param <K>
 * The action type the function evaluates.
 * @param <T>
 * The state type the function evaluates.
 */
public interface MonotoneFunction<K,T extends LatticeElement<T>> 
{
	
	/**
	 * @param action
	 * @return
	 * Whether the function is applicable for the given action.
	 */
	public boolean applicableFor(K action);
	
	/**
	 * Evaluates the monotone function with 
	 * the given action and state returning the result.
	 * Even if the result is equivalent to the given state it may be a different instance.
	 * The function assumes that {@link #applicableFor(action)} == true.
	 * @param action
	 * @param state
	 * @return
	 * The new state
	 */
	public T apply(K action, Evaluable<T> state);
}
