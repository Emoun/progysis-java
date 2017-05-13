package dk.emoun.progysis.lattices;

import java.util.Set;

public class SignPowerSet extends EnumPowerSet<SignPowerSet, Sign> {

//Constructors
	public SignPowerSet(){
		super();
	}
	
	public SignPowerSet(Sign...enums){
		super(enums);
	}
	
	public SignPowerSet(Set<Sign> enumSet){
		super(enumSet);
	}
	
//Overriding methods
	@Override
	public SignPowerSet createPowerSet(Set<Sign> enumSet) {
		return new SignPowerSet(enumSet);
	}
}
