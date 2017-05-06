package dk.emoun.progysis.monotoneFramework;

import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;

public class MonotoneFunctionMapper 
	<
		K,
		T extends LatticeElement<T>,
		R extends LatticeElement<?>
	> 
	implements MonotoneFunction<K,T, R>
{
	
//Fields
	private MonotoneFunction<K,T, R>[] functions;
	
//Constructors
	MonotoneFunctionMapper(MonotoneFunction<K,T, R>... functions){
		this.functions = functions;
	}
	
	
//methods
	@Override
	public boolean applicableOn(K action) {
		return getApplicableFunction(action) != null;
	}

	@Override
	public R apply(K action, Evaluable<T> state) {
		MonotoneFunction<K,T,R> f = getApplicableFunction(action);
		
		if(f != null){
			return f.apply(action, state);
		}
		throw new IllegalStateException("No applicable function");
	}

	public MonotoneFunction<K,T,R> getApplicableFunction(K action){
		for(MonotoneFunction<K,T, R> f: functions){
			if(f.applicableOn(action)){
				return f;
			}
		}
		return null;
	}
}
