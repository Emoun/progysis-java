package dk.emoun.progysis.worklist;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dk.emoun.progysis.lattices.CompleteLattice;
import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;


/**
 * Defines a constraint that is dependent on other constraint's values to calculate its own value.
 * @param <L>
 * The Complete Lattice type of the values each instance of CompoundConstraint is dependent upon.
 * @param <V>
 * The type of the lattice elements comprising this CompoundConstraint
 */
public class CompoundConstraint
			<
			L extends CompleteLattice<V>,
			V extends LatticeElement<V>
			> 
			implements Evaluable<V>
{

//Fields
	/**
	 * A list of constraints that are evaluated when the value of the instance is
	 * to be decided.
	 */
	private List<Evaluable<V>> constraints;
	
	/**
	 * The Complete Lattice over the values of the instance
	 */
	private L lattice;
//Constructors
	
	/**
	 * Constructs a CompoundConstraint that evaluates to the joined value of the given constraints
	 * @param lattice
	 * @param constraints
	 */
	public CompoundConstraint(L lattice,Evaluable<V>...constraints){
		this.lattice = lattice;
		this.constraints = new ArrayList<Evaluable<V>>();
		
		for(Evaluable<V> c: constraints){
			this.constraints.add(c);
		}
	}
	
//Methods	
	
	@Override
	public V value() {
		V result = lattice.getBottom();
		for(Evaluable<V> c: constraints){
			result = lattice.join(result, c);
		}
		return result;
	}

}
