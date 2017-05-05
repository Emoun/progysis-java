package MicroC_language.analysis;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A utility class representing a Program Graph.<br>
 * <p>
 * The class holds information about what states the graph holds and what transitions they have.<br>
 * Methods are provided for graph generation: create a state with {@link #newState()} or add a transition
 * with {@link #addOutgoingTransition(int, Transition)}.<br>
 * The graph held at any point by an instance of the class is not necessarily a valid Program Graph.
 * Therefore, {@link #valid()} is provided to test whether the graph is valid.
 */
public class ProgramGraph {

// Fields

    private List<List<Transition>> graph;
    private int initialState, finalState;

// Constructors

    /**
     * Creates a new Program Graph with the given graph and final states.<br>
     * The given graph must be valid.<br>
     * The given arguments are used as is by the new instance, therefore any
     * outside changes to them will change the state of the new graph instance.
     *
     * @param graph
     * @param finalStates a list of final states
     */
    public ProgramGraph(List<List<Transition>> graph, int initialState, int finalState) {
        validateGraph(graph, initialState, finalState);
        this.graph = graph;
        this.initialState = initialState;
        this.finalState = finalState;
    }

    /**
     * Creates a new, empty Program Graph.<br>
     * The graph has no states or transitions after initialization.
     */
    public ProgramGraph() {
        this(new ArrayList<List<Transition>>(), -1, -1);
    }


// Methods for graph generation

    /**
     * Creates a new state in the graph and returns its number.<br>
     * The new state has no outgoing transitions.<br>
     * This method does not validate that the resulting graph is valid.
     *
     * @return
     */
    public int newState() {
        return newState(new ArrayList<Transition>());
    }

    /**
     * Creates a new state in the graph with the given outgoing transitions.
     * Returns the state's number in the graph.<br>
     * This method does not validate the resulting graph.
     *
     * @param transitions outgoing transitions of the new state
     * @return the number of the new state in the graph
     */
    public int newState(List<Transition> transitions) {
        int stateNr = graph.size();
        graph.add(transitions);
        return stateNr;
    }

    /**
     * Makes the given state final.
     *
     * @param s
     */
    public void setFinalState(int s) {
        this.finalState = s;
    }
    
    /**
     * @return
     * the currently set final state
     */
    public int getFinalState(){
    	return this.finalState;
    }
    
    /**
     * Makes the given state final.
     *
     * @param s
     */
    public void setInitialState(int s) {
        this.initialState = s;
    }
    
    /**
     * @return
     * the currently set final state
     */
    public int getInitialState(){
    	return this.initialState;
    }

    /**
     * Adds the given transition to the outgoing set of the given state.<br>
     * This method does not validate the resulting graph.
     *
     * @param state to add the outgoing transition to
     * @param t     transition to add as outgoing to the given state.
     */
    public void addOutgoingTransition(int state, Transition t) {
        validateStateNumber(state);
        if (t == null) {
            throw new IllegalArgumentException("Transition was null");
        }

        graph.get(state).add(t);
    }
    
    /**
     * 
     * @return
     * a reversed version of the invoking instance:<br>
     * for each (s,s') in G there is a (s',s) in G.reverse()
     * G.initialState == G.reverse().finalState
     * G.finalState == G.reverse().initialState
     */
    public ProgramGraph reverse(){
    	
    	
    	//Initialize transition list
    	List<List<Transition>> reversedTransitions = new ArrayList<List<Transition>>();
    	for(int i = 0; i<this.graph.size(); i++){
    		reversedTransitions.add(new ArrayList<Transition>());
    	}
    	
    	//reverse all transitions
    	for(int i = 0; i<this.graph.size(); i++){
    		for(Transition t: graph.get(i)){
    			reversedTransitions.get(t.getTo()).add(new Transition(i, t.getBlock()));
    		}
    	}
    	
    	return new ProgramGraph(reversedTransitions, this.finalState, this.initialState);
    }
// Methods for graph analysis

    /**
     * @return {@code true} if the graph has sound transitions, otherwise {@code false}.
     */
    public boolean valid() {
        return validString() == null;
    }

    /**
     * @return {@code null} if the graph has sound transitions, otherwise a string explaining
     * the errors encountered in the graph
     */
    public String validString() {
        try {
            validateGraph(this.graph, this.initialState, this.finalState);
            return null;
        } catch (IllegalArgumentException err) {
            return err.getMessage();
        }
    }

    /**
     * @param s
     * @return {@code true} if the given state is final, otherwise {@code false}
     */
    public boolean isFinal(int s) {
        return this.finalState == s;
    }

    /**
     * The returned list is unmodifiable.
     *
     * @param state
     * @return
     */
    public List<Transition> getOutgoingTransitions(int state) {
        validateStateNumber(state);
        return Collections.unmodifiableList(this.graph.get(state));
    }

    @Override
    public boolean equals(Object o){
    	if(o instanceof ProgramGraph){
    		ProgramGraph other = (ProgramGraph) o;
    		try{
    			//Test that the graph is equal
	    		for(int qs = 0; qs<this.graph.size(); qs++){
	    			for(Transition t: this.graph.get(qs)){
	    				if(!other.graph.get(qs).contains(t)){
	    					return false;
	    				}
	    			}
	    			for(Transition t: other.graph.get(qs)){
	    				if(!this.graph.get(qs).contains(t)){
	    					return false;
	    				}
	    			}
	    		}
	    		//Test that the initial and final states are equal
	    		return 	this.initialState 	== other.initialState &&
	    				this.finalState 	== other.finalState;
    		}catch(IndexOutOfBoundsException err){
    			return false;
    		}
    		
    		
    	}
    	return false;
    }
    
    /**
     * @return
     * the number of states currently in the graph
     */
    public int getNumberOfStates(){
    	return graph.size();
    }
// Static methods
    
    /**
     * Validates that the graph has sound transitions.<br>
     * If the graph is invalid, an {@link IllegalArgumentException} is
     * thrown with an error message specifying what was wrong with the graph.
     *
     * @param graph
     */
    public static void validateGraph(List<List<Transition>> graph, int initialState, int finalState) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph was null");
        }

        // Track the outgoing transition targets
        List<Integer> to;

        for (int i = 0; i < graph.size(); i++) {
            to = new ArrayList<Integer>();
            List<Transition> transitions = graph.get(i);
            if(transitions == null){
            	throw new IllegalArgumentException("Transition list for state " + i + "is null");
            }
			for (Transition t : transitions) {
                if (t.getTo() == i) {
                    throw new IllegalArgumentException("State " + i + " has a transition to itself");
                }
                if (t.getTo() >= graph.size()) {
                    throw new IllegalArgumentException("State " + i + " has a transition to non-existing state " + t.getTo());
                }

                if (to.contains(t.getTo())) {
                    throw new IllegalArgumentException("State " + i + " has multiple transition to the same state " + t.getTo());
                } else {
                    to.add(t.getTo());
                }
            }
        }
        if(graph.size()>0){
	        if(initialState <0 || initialState >= graph.size()){
	        	throw new IllegalArgumentException("Invalid initial state: " + initialState);
	        }
	        if(finalState <0 || finalState >= graph.size()){
	        	throw new IllegalArgumentException("Invalid final state: " + initialState);
	        }
        }
    }

// Private methods

    private void validateStateNumber(int state) {
        if (state < 0 || state >= graph.size()) {
            throw new IllegalArgumentException("Invalid state nr: " + state);
        }
    }
}