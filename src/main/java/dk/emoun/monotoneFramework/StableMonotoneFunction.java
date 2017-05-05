package MicroC_language.analysis.monotoneFramework;

import MicroC_language.analysis.lattices.Evaluable;
import MicroC_language.analysis.lattices.LatticeElement;

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
