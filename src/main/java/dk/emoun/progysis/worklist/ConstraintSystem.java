package dk.emoun.progysis.worklist;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import dk.emoun.progysis.lattices.CompleteLattice;
import dk.emoun.progysis.lattices.Evaluable;
import dk.emoun.progysis.lattices.LatticeElement;

/**
 * Implements a constraint system of equations/constraints.<br>
 * <br>
 * Each ConstraintSystem contains a set of {@link FlowVariable}s implementing the equations. 
 * The flow variables are referenced by integers 0..n, where n+1 is the total number of flow variables.<br> 
 * Flow variables can derive their values from a set of constraint, some of which may be dependent on
 * other flow variables. The method {@link #addIndependentConstraintToVariable}
 * adds the given constraint to a flow variable, meaning the variable now joins the value of the constraint
 * with the values of other constraints (previously added to the variable) to derive its value.<br>
 * Likewise, the method {@link #addConstraintToVariableDependentOnVariable} does the
 * same, except it adds a constraint that is dependent on the value of other flow variables.<br>
 * <br>
 * Each flow variable is mapped to its element value in a given lattice.
 * By default, all variables are mapped to the bottom value of the lattice.<br>
 * The value of a flow variable never changes, except when the method {@link #updateValueOf}
 * is called, mapping the flow variable to the value it evaluates to. When this evaluation is run,
 * if the flow variable is dependent on other flow variables, these variables' value is not recalculated, but the
 * previously 'updated' value is used.<br>
 * <br>
 * Example: we have the two flow variables x1,x2, and x3 where x1 is dependent on x2's value and x2 is dependent on x3's value.<br>
 * When the constraint system is initialized, all variables map to the bottom value.<br>
 * The update is called on x3, and since it is independent, it evaluates to 'T'.<br>
 * The update is then called on x1. Since x2 has yet to be updated, meaning it is still bottom, x1 evaluates to bottom too, not changing its mapping.<br>
 * The update is then called on x2. Since x3 now has the value 'T', x2 evaluates to 'Ts'.<br>
 * The update is called again on x1, and since x2 now has the value 'Ts', x1 evaluates to 'Tv'.<br>
 * <br>
 * The method {@link #getValueOf} returns the currently mapped (I.E. last updated value) of the given flow variable.
 * This method does not recalculate the variable, so it may return an outdated value. This is by design.
 * 
 * @param <L>
 * The Complete Lattice the flow variable values are elements of.
 * @param <V>
 * The lattice elements the flow variables evaluate to.
 */
public class ConstraintSystem<L extends CompleteLattice<V>, V extends LatticeElement<V>> {
	
//Fields
	
	/**
	 * The flow variables (equations/constraints) of the constraint system
	 */
	private FlowVariable<L,V>[] flowVariables;
	
	/**
	 * The last updated values of the flow variables.
	 */
	private List<V> flowVariableCurrentValues;
	
	/**
	 * An instance of the lattice the flow variables evaluate to an element of.
	 */
	private L lattice;
	
//Constraints
	
	/**
	 * Constructs a new Constraint system with the given number of flow variables 
	 * who evaluate to elements of the given lattice.<br>
	 * When the constructor returns all flow variables have no constraints, meaning they evaluate to the 
	 * bottom element, and they are all updated to that element.
	 * @param lattice
	 * @param numberOfFlowVariables
	 */
	public ConstraintSystem(L lattice, int numberOfFlowVariables){
		this.lattice = lattice;
		this.flowVariables = new FlowVariable[numberOfFlowVariables];
		this.flowVariableCurrentValues = new ArrayList<V>();
		
		for(int i = 0; i<this.flowVariables.length; i++){
			this.flowVariables[i] = new FlowVariable<L, V>(this.lattice);
			this.flowVariableCurrentValues.add(this.lattice.getBottom());
		}
	}
//Methods
	
	/**
	 * @return
	 * An instance of the lattice the flow variables evaluate to an element of.
	 */
	public L getLattice(){
		return this.lattice;
	}
	
	/**
	 * Add a new constraint to a variable, which is dependent on another variable.
	 * @param variableToAddTo
	 * The flow variable to add the constraint to.
	 * @param dependencyVariable
	 * The flow variable the constraint is dependent upon
	 * @param constraintCalculator
	 * Given the value of the flow variable the constraint is dependent on, returns the value of the constraint evaluates to.
	 */
	public void addConstraintToVariableDependentOnVariable(int variableToAddTo, int dependencyVariable, Function<V,V> constraintCalculator){
		if(!isValidFlowVariable(dependencyVariable))
		{
			throw new IllegalArgumentException("The dependecy variable does not exist: " + dependencyVariable);
		}
		validateFlowVariable(variableToAddTo);
		
		FlowVariableConstraint<L, V> dependentConstraint = new FlowVariableConstraint<L,V>(this, dependencyVariable, constraintCalculator);
		this.flowVariables[variableToAddTo].addConstaint(dependentConstraint);
	}
	
	/**
	 * Adds a constraint to the given flow variable.
	 * @param variableToAddTo
	 * @param constraint
	 */
	public void addIndependentConstraintToVariable(int variableToAddTo, Evaluable<V> constraint){
		validateFlowVariable(variableToAddTo);
		
		this.flowVariables[variableToAddTo].addConstaint(constraint);
	}
	
	/**
	 * Gets the previously calculated value of the given flow variable.
	 * @param flowVariable
	 * @return
	 */
	public V getValueOf(int flowVariable) {
		validateFlowVariable(flowVariable);
		return this.flowVariableCurrentValues.get(flowVariable);
	}
	
	/**
	 * Recalculates the value of the given flow variable and
	 * returns the calculated value. Additionally, the variable
	 * is now mapped to that value, which means the next call to 
	 * {@link #getValueOf} on the given variable will return
	 * the same value as returned by this invocation.
	 * @param flowVariable
	 * @return
	 */
	public V updateValueOf(int flowVariable){
		validateFlowVariable(flowVariable);
		FlowVariable<L ,V> fV = this.flowVariables[flowVariable];
		this.flowVariableCurrentValues.set(flowVariable, fV.value());
		return getValueOf(flowVariable);
	}
	
	/**
	 * 
	 * @return
	 * the number of flow variables this constraint system has.
	 */
	public int getNumberOfFlowVariables(){
		return this.flowVariables.length;
	}
	
	/**
	 * Gets the list of all the flow variables that have constraints which
	 * are dependent on the given flow variable.
	 * @param v
	 * @return
	 */
	public List<Integer> getVariablesInfluencedBy(int v){
		List<Integer> influenced = new ArrayList<Integer>();
		for(int i = 0; i< flowVariables.length; i++){
			if(flowVariables[i].influenceBy(v)){
				influenced.add(i);
			}
		}
		return influenced;
	}

	
	public String currentValuesString(){
		StringBuilder b = new StringBuilder();
		
		for(int i = 0; i<getNumberOfFlowVariables(); i++){
			b.append("A(" + i + ") >= " + lattice.stringRepresentation(getValueOf(i)) + "\n");
		}
		
		String result = b.toString();
		return result.substring(0, result.length()-1);
		
	}
	
//Private methods
	
	/**
	 * Validates that the given flow variable (referenced by the given number)
	 * is present in the constraint system.<br>
	 * @param variableToAddTo
	 * @throws IllegalArgumentException
	 * if the flow variable is not present.
	 */
	private void validateFlowVariable(int variableToAddTo) {
		if(!isValidFlowVariable(variableToAddTo)){
			throw new IllegalArgumentException("The flow variable to add the constraint to does not exist: " + variableToAddTo);
		}
	}
	
	/**
	 * Returns whether the given flow variable (referenced by the given number)
	 * is present in the constraint system
	 * @param vaiableNumber
	 * @return
	 * {@code true} if present, otherwise {@code false}
	 */
	private boolean isValidFlowVariable(int vaiableNumber) {
		return 	vaiableNumber >= 0 &&
				vaiableNumber < flowVariables.length;
	}
}