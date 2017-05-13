package dk.emoun.progysis.lattices;

import java.util.Set;

/**
 * A Powerset over the instances of an enum.
 * @param <S>
 * @param <E>
 */
public abstract class EnumPowerSet<S extends EnumPowerSet<S,E>, E extends Enum<E>> extends PowerSet<S, E>{

	
//Constructors
	/**
	 * Creates the bottom element of the lattices.
	 */
	public EnumPowerSet(){
		super();
	}
	
	/**
	 * Creates an element with the given values as its set.
	 * Duplications are ignored.
	 * @param enums
	 * Values of the set.
	 */
	public EnumPowerSet(E...enums){
		super(enums);
	}
	
	/**
	 * Creates an element in the lattice with the given set.
	 * @param enumSet
	 */
	public EnumPowerSet(Set<E> enumSet){
		super(enumSet);
	}
	


}
