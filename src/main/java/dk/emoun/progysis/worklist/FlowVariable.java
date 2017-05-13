package dk.emoun.progysis.worklist;

import java.util.ArrayList;
import java.util.List;

import dk.emoun.progysis.lattices.CompleteLattice;
import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;


/**
 * Represents equations/constraints of a constraint system.<br>
 * A flow variable has a list of constraints, the values of which, 
 * joined, equal the value of the flow variable.<br>
 * 
 * @param <V>
 * The type of the lattice elements the constraints (and the flow variable) evaluates to.
 */
public class FlowVariable
		<
		V extends LatticeElement<V>
		> 
		extends CompoundConstraint<V>
{
	
//Fields
		
//Constructors
	
	/**
	 * Constructs a Flow Variable evaluating to an element
	 * of the given lattice. When the constructor returns 
	 * the flow variable has no constraints.
	 * @param lattice
	 */
	public FlowVariable(){
		super();
	}
	
//Method
	
	/**
	 * Returns whether this flow variable has a constraint that is dependent on
	 * the given flow variable.
	 * @param v
	 * @return
	 */
	public boolean influenceBy(int v){
		for(Evaluable<V> c: getConstraints()){
			if(c instanceof FlowVariableConstraint){
				if(((FlowVariableConstraint)c).getInfluencedBy() == v){
					return true;
				}
			}
		}
		return false;
	}










}