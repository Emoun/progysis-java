package dk.emoun.progysis.lattices;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements general powerset lattices.
 * A Powerset is a lattice where the empty set is bottom, and top is a set of values.
 * The lattice is orderes such that all non-top elements of the lattice contain
 * a subset of the top set.<br>
 * Given two elements in the powerset e1 and e2, e1.compare(e2) == true if 
 * e1 is a subset of e2.
 *  
 * @param <S>
 * The type of the powerset.
 * @param <V>
 * The type of values in the sets of the powerset elements.
 */
public abstract class PowerSet<S extends PowerSet<S,V>, V> extends LatticeElement<S>{
	
//fields
	
	/**
	 * The set of values comprising the lattice element
	 */
	private Set<V> valueSet;
//Constructors
	
	/**
	 * Creates the bottom element of the lattices.
	 */
	public PowerSet(){
		this.valueSet = Collections.emptySet();
	}
	
	/**
	 * Creates an element with the given values as its set.
	 * Duplications are ignored.
	 * @param values
	 * Values of the set.
	 */
	public PowerSet(V...values){
		Set<V> temp = new HashSet<V>();
		for(V s: values){
			temp.add(s);
		}
		setValueSetImmutable(temp);
	}
	
	/**
	 * Creates an element in the lattice with the given set.
	 * @param valueSet
	 */
	public PowerSet(Set<V> valueSet){
		setValueSetImmutable(valueSet);
	}
	
//abstract methods
	
	/**
	 * Creates a new element in the lattice with the given set.
	 * @param valueSet
	 * The set of the element
	 * @return
	 * The created element
	 */
	public abstract S createPowerSet(Set<V> valueSet);
	
//methods
	
	/** 
	 * @return
	 * all the values of the set in the element
	 */
	public Set<V> getValueSet(){
		return valueSet;
	}
		
	
//Overriding methods
	@Override
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
	/**
	 * Sets the valueSet to an unmoddifiable version of the given set.
	 * @param enumSet
	 */
	private void setValueSetImmutable(Set<V> enumSet) {
		this.valueSet = Collections.unmodifiableSet(enumSet);
	}
}
