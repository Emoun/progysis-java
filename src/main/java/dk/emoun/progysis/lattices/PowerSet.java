package dk.emoun.progysis.lattices;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class PowerSet<S extends PowerSet<S,V>, V> extends LatticeElement<S>{
	
//fields
	private Set<V> valueSet;
//Constructors
	public PowerSet(){
		this.valueSet = Collections.emptySet();
	}
	
	public PowerSet(V...values){
		Set<V> temp = new HashSet<V>();
		for(V s: values){
			temp.add(s);
		}
		setValueSetImmutable(temp);
	}
	
	public PowerSet(Set<V> enumSet){
		setValueSetImmutable(enumSet);
	}
	
//abstract methods

	public abstract S createPowerSet(Set<V> valueSet);
	
//methods

	public Set<V> getValueSet(){
		return valueSet;
	}
		
	
//Overriding methods
	public S getBottom(){
		return createPowerSet(Collections.emptySet());
	}
	
	@Override
	public boolean isBottom() {
		return valueSet.size() == 0;
	}

	@Override
	public boolean compare(Evaluable<S> other) {
		if(this.isBottom()){
			//intrinsically holds
			return true;
		}
		
		return valueSet.containsAll(other.value().getValueSet());
	}

	@Override
	public S join(Evaluable<S> other) {
		Set<V> result = new HashSet<V>();
		result.addAll(valueSet);
		result.addAll(other.value().getValueSet());
		return (createPowerSet(result));
	}

	@Override
	public String stringRepresentation() {
		if(valueSet.size() == 0){
			return "{}";
		}
		StringBuilder b = new StringBuilder();
		
		b.append('{');
		for(V s: valueSet){
			b.append(s);
			b.append(',');
		}
		String result = b.toString();
		return result.substring(0, result.length()-1) + '}';
	}
	
//Private Methods
	private void setValueSetImmutable(Set<V> enumSet) {
		this.valueSet = Collections.unmodifiableSet(enumSet);
	}
}
