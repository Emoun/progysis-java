package dk.emoun.progysis.monotoneFramework;

import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;

/**
 * Maps several MonotoneFunctions to a single function.<br>
 * The mapper is only applicable if at least one of its members functions are so.
 * When the mapper is {@link #apply(Object, Evaluable) applied} it find any one of its members
 * and applies it. If more than one member is applicable which one is chosen is undefined.
 * @param <K>
 * @param <T>
 */
public class MonotoneFunctionMapper 
	<
		K,
		T extends LatticeElement<T>
	> 
	implements MonotoneFunction<K,T>
{
	
//Fields
	private MonotoneFunction<K,T>[] functions;
	
//Constructors
	MonotoneFunctionMapper(MonotoneFunction<K,T>... functions){
		this.functions = functions;
	}
	
	
//methods
	@Override
	public boolean applicableFor(K action) {
		return getApplicableFunction(action) != null;
	}

	@Override
	public T apply(K action, Evaluable<T> state) {
		MonotoneFunction<K,T> f = getApplicableFunction(action);
		
		if(f != null){
			return f.apply(action, state);
		}
		throw new IllegalStateException("No applicable function");
	}
	
	/**
	 * @param action
	 * @return
	 * an arbitrary member function that is {@link MonotoneFunction.applicableFor applicable}
	 * for the action.<br>
	 * If no such function is found, {@code null} is returned. 
	 */
	public MonotoneFunction<K,T> getApplicableFunction(K action){
		for(MonotoneFunction<K,T> f: functions){
			if(f.applicableFor(action)){
				return f;
			}
		}
		return null;
	}
}
