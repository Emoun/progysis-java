package dk.emoun.progysis.programGraph;

/**
 * Implements edges in a program graph.
 */
public class Transition<T>{
	
//Fields
	/**
	 * The state in the program graph the transition is pointing to.
	 */
	private int to;
	
	/**
	 * The action of the edge.
	 */
	private T action;
	
//Constructors
	
	/**
	 * Constructs a new transition that points to the given state
	 * and representing the given elementary block (action).
	 * @param to
	 * The state in the program graph the transition is pointing to.
	 * @param action
	 * The action of the edge.
	 */
	public Transition(int to, T action){
		if(to <0){
			throw new IllegalArgumentException("Transition target invalid: " + to);
		}
		if(action == null){
			throw new IllegalArgumentException("Block was null");
		}
		
		this.to = to;
		this.action = action;
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
	public T getAction(){
		return this.action;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Transition){
			Transition other = (Transition) o;
			return this.to == other.to && this.action.equals(other.action);
		}
		return false;
	}
	

}