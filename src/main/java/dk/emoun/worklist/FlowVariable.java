package MicroC_language.analysis.worklist;

import java.util.ArrayList;
import java.util.List;

import MicroC_language.analysis.lattices.CompleteLattice;
import MicroC_language.analysis.lattices.Evaluable;
import MicroC_language.analysis.lattices.LatticeElement;


/**
 * Represents equations/constraints of a constraint system.<br>
 * A flow variable has a list of constraints, the values of which, 
 * joined, equal the value of the flow variable.<br>
 * 
 * @param <L>
 * The Complete Lattice type the constraints (and the flow variable) evaluate to elements of.
 * @param <V>
 * The type of the lattice elements the constraints (and the flow variable) evaluates to.
 */
public class FlowVariable
		<
		L extends CompleteLattice<V>, 
		V extends LatticeElement<V>
		> 
		implements Evaluable<V>
{
	
//Fields
	
	/**
	 * Instance of the lattice the constraints evaluate to elements of.
	 */
	private L lattice;
	
	/**
	 * The constraint who's values, joined, equal the flow variable's value.
	 */
	private List<Evaluable<V>> constraints;
//Constructors
	
	/**
	 * Constructs a Flow Variable evaluating to an element
	 * of the given lattice. When the constructor returns 
	 * the flow variable can no constraints, 
	 * meaning it evaluates to the bottom element of the given lattice.
	 * @param lattice
	 */
	public FlowVariable(L lattice){
		this.constraints = new ArrayList<Evaluable<V>>();
		this.lattice = lattice;
	}
//Method
	
	/**
	 * Add a constraint to the flow variable.
	 * @param c
	 */
	public void addConstaint(Evaluable<V> c){
		this.constraints.add(c);
	}
	
	@Override
	public V value() {
		V result = lattice.getBottom();
		
		for(Evaluable<V> c: constraints){
			result = lattice.join(result, c);
		}
		
		return result;		
	}
	
	/**
	 * Returns whether this flow variable has a constraint that is dependent on
	 * the given flow variable.
	 * @param v
	 * @return
	 */
	public boolean influenceBy(int v){
		for(Evaluable<V> c: constraints){
			if(c instanceof FlowVariableConstraint){
				if(((FlowVariableConstraint)c).getInfluencedBy() == v){
					return true;
				}
			}
		}
		return false;
	}










}