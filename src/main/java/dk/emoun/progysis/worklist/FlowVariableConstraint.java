package dk.emoun.progysis.worklist;

import java.util.function.Function;

import dk.emoun.progysis.lattices.CompleteLattice;
import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;

/**
 * A constraint who's value depends on what a specific flow variable evaluates to.
 * @param <V>
 * The type of the lattice elements the constraint evaluates to.
 */
public class FlowVariableConstraint
		<
		V extends LatticeElement<V>
		> 
		implements Evaluable<V>{
	
//Fields
	
	/**
	 * The constraint system which houses the flow variable this constraint is dependent on.
	 */
	private ConstraintSystem<V> parent;
	
	/**
	 * The number reference of the flow variable the constraint is dependent on.
	 */
	private int influencedBy;
	
	/**
	 * A function calculating the value of the constraint, given the value of the flow variable
	 * it is dependent on.
	 */
	private Function<V,V> constraintCalculator;
	
//Constructors
	
	/**
	 * Constructs a new constraint that is dependent on the given flow variable in the given constraint system.
	 * @param parent
	 * The constraint system which houses the flow variable this constraint is dependent on.
	 * @param influncedBy
	 * The number reference of the flow variable the constraint is dependent on.
	 * @param constraintCalculator
	 * A function calculating the value of the constraint, given the value of the flow variable
	 * it is dependent on.
	 */
	public FlowVariableConstraint(ConstraintSystem<V> parent, int influncedBy, Function<V,V> constraintCalculator) {
		this.constraintCalculator = constraintCalculator;
		this.influencedBy = influncedBy;
		this.parent = parent;
	}
//Methods
	
	@Override
	public V value() {
		return constraintCalculator.apply(parent.getValueOf(influencedBy));
	}
	
	/**
	 * 
	 * @return
	 * The number reference in a constraint system of the flow variable the constraint is dependent.
	 */
	public int getInfluencedBy(){
		return this.influencedBy;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}