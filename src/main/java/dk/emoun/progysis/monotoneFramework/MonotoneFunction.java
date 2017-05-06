package dk.emoun.progysis.monotoneFramework;

import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;

/**
 * An interface representing monotone function functionality
 * @param <K>
 * The statement type the function evaluates
 * @param <T>
 * The state type the function evaluates.
 */
public interface MonotoneFunction<K,T extends LatticeElement<T>> 
{
	/**
	 * Evaluates the monotone function with 
	 * the given statement and state returning the result.
	 * If the result is an equal to the given state, 
	 * the same state instance must be returned, 
	 * otherwise a new state instance is returned.
	 * @param statement
	 * @param state
	 * @return
	 * The new state
	 */
	public T applyFunction(K statement, Evaluable<T> state);
}
