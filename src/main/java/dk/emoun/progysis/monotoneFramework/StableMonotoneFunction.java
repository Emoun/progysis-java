package dk.emoun.progysis.monotoneFramework;

import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;

/**
 * A type of Monotone Function which resulting state is the same type
 * as the state parameter
 * @param <K>
 * The statement type the function evaluates
 * @param <R>
 * The state type the function evaluates from and to
 */
public interface StableMonotoneFunction
			<
				K,
				R extends LatticeElement<R>
			> 
			extends MonotoneFunction<K,R, R>
{

}
