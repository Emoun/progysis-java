package MicroC_language.analysis;

import MicroC_language.elements.ElementaryBlock;
import MicroC_language.elements.LogicalEquivalence;

/**
 * Implements edges in a program graph.
 */
public class Transition implements LogicalEquivalence<Transition>{
	
//Fields
	/**
	 * The state in the program graph the transition is pointing to.
	 */
	private int to;
	
	/**
	 * The action of the edge.
	 */
	private ElementaryBlock block;
	
//Constructors
	
	/**
	 * Constructs a new transition that points to the given state
	 * and representing the given elementary block (action).
	 * @param to
	 * The state in the program graph the transition is pointing to.
	 * @param block
	 * The action of the edge.
	 */
	public Transition(int to, ElementaryBlock block){
		if(to <0){
			throw new IllegalArgumentException("Transition target invalid: " + to);
		}
		if(block == null){
			throw new IllegalArgumentException("Block was null");
		}
		
		this.to = to;
		this.block = block;
	}
	
//Methods
	
	/**
	 * @return
	 * The state in the program graph the transition is pointing to.
	 */
	public int getTo(){
		return this.to;
	}
	
	/**
	 * 
	 * @return
	 * The action of the edge.
	 */
	public ElementaryBlock getBlock(){
		return this.block;
	}
	
	@Override
	public boolean equals(Object o){
		return (o instanceof Transition)? this.logicEquals((Transition) o): false;
	}
	
	@Override
	public boolean logicEquals(Transition o) {
		return 	this.to == o.to &&
				this.block.logicEquals(o.block);
	}
	

}