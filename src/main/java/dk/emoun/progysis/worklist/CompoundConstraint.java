package dk.emoun.progysis.worklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;


/**
 * Defines a constraint that is dependent on other constraint's values to calculate its own value.
 * @param <V>
 * The type of the lattice elements comprising this CompoundConstraint
 */
public class CompoundConstraint
			<
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
	
//Constructors
	
	public CompoundConstraint(){
		this.constraints = new ArrayList<Evaluable<V>>(); 
	}
	
	/**
	 * Constructs a CompoundConstraint that evaluates to the joined value of the given constraints
	 * @param constraints
	 */
	public CompoundConstraint(Evaluable<V>...constraints){
		this();
		
		for(Evaluable<V> c: constraints){
			this.constraints.add(c);
		}
	}
	
//Methods	
	
	@Override
	public V value() {
		if(constraints.isEmpty()){
			throw new IllegalStateException("No constraints");
		}
		Iterator<Evaluable<V>> values = constraints.iterator();
		V result = values.next().value();
		
		while(values.hasNext()){
			result = result.join(values.next());
		}
		
		return result;
	}
	
	/**
	 * Add a constraint.
	 * @param c
	 */
	public void addConstaint(Evaluable<V> c){
		this.constraints.add(c);
	}
	
	public List<Evaluable<V>> getConstraints(){
		return Collections.unmodifiableList(constraints);
	}
}
