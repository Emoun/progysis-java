package dk.emoun.progysis.worklist;

/**
 * Defines the interface of a worklist.<br>
 * A worklist handles the order in which flow variables are to be evaluated
 * when an analysis is run. <br>
 * <br>
 * For this purpose each worklist must implement the method {@link #extractNextFlowVariable} 
 * which returns a reference to the next flow variable that should be evaluated. <br>
 * The method {@link #insert} is used to tell the worklist that the given flow variable
 * needs to be recalculated, which will make the worklist put it inside its order.
 * Instances of a worklist have an internal order in which the flow variables should be executed
 * and using this method will put the variable into the ordering. 
 * A flow variable may be present in the order more than once.<br>
 * <br>
 * When an instance of a worklist is initialized, its internal ordering must be empty.
 */
public interface Worklist {
	
	/**
	 * 
	 * @return
	 * Whether the worklist contains any more flow variables that need to be calculated.
	 */
	public boolean isEmpty();
	
	/**
	 * Inserts the given flow variable into the ordering of the flow variable.
	 * @param flowVariable
	 */
	public void insert(int flowVariable);
	
	/**
	 * 
	 * @return
	 * The next flow variable that should be evaluated. The returned flow variable 
	 * is removed from the worklist's ordering.
	 */
	public int extractNextFlowVariable();
	
}
