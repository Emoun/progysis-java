package dk.emoun.progysis.lattices;

import java.util.Set;

public abstract class EnumPowerSet<S extends EnumPowerSet<S,E>, E extends Enum<E>> extends PowerSet<S, E>{

	
//Constructors
	public EnumPowerSet(){
		super();
	}
	
	public EnumPowerSet(E...enums){
		super(enums);
	}
	
	public EnumPowerSet(Set<E> enumSet){
		super(enumSet);
	}
	


}
