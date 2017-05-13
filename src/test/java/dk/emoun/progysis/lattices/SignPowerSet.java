package dk.emoun.progysis.lattices;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SignPowerSet extends LatticeElement<SignPowerSet> {
	


//Fields
	private Set<Sign> signSet;
	
//Constructors
	public SignPowerSet(){
		this.signSet = Collections.emptySet();
	}
	
	public SignPowerSet(Sign...signs){
		Set<Sign> temp = new HashSet<Sign>();
		for(Sign s: signs){
			temp.add(s);
		}
		makeSetImmutable(temp);
	}
	
	public SignPowerSet(Set<Sign> signSet){
		makeSetImmutable(signSet);
	}
	
//overriding methods

	@Override
	public SignPowerSet getBottom() {
		return new SignPowerSet();
	}
	
	@Override
	public boolean isBottom() {
		return getSignSet().size() == 0;
	}
	
	@Override
	public boolean compare(Evaluable<SignPowerSet> other) {
		if(this.isBottom()){
			//intrinsically holds
			return true;
		}
		
		Set<Sign> v1 = getSignSet();
		Set<Sign> v2 = other.value().getSignSet();
		return v2.containsAll(v1);
	}

	@Override
	public SignPowerSet join(Evaluable<SignPowerSet> other) {
		Set<Sign> result = new HashSet<Sign>();
		result.addAll(getSignSet());
		result.addAll(other.value().getSignSet());
		return new SignPowerSet(result);
	}
	
	@Override
	public String stringRepresentation() {
		Set<Sign> v = getSignSet();
		StringBuilder b = new StringBuilder();
		
		b.append('{');
		for(Sign s: v){
			b.append(s);
			b.append(',');
		}
		String result = b.toString();
		return result.substring(0, result.length()-1) + '}';
	}

//other methods
	public static SignPowerSet createElement(Sign... signs){
		return new SignPowerSet(signs);
	}	
	
	public Set<Sign> getSignSet(){
		return Collections.unmodifiableSet(signSet);
	}
	
//private methods
	private void makeSetImmutable(Set<Sign> signSet) {
		this.signSet = Collections.unmodifiableSet(signSet);
	}
}
