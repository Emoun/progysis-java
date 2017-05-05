package MicroC_language.analysis.worklist;

import MicroC_language.analysis.lattices.CompleteLattice;
import MicroC_language.analysis.lattices.LatticeElement;
import MicroC_language.analysis.lattices.LatticeUtilities;

public class AbstractWorklistAlgorithm {

//Method
	/**
	 * Runs the Abstract Worklist Algorithm, given a specific implementation of
	 * a worklist and a constraint system. The state of the constraint system is changed by
	 * this invocation into the solved state.
	 * @param w
	 * An instance of a worklist. Should not contain any flow variables.
	 * @param cS
	 * The constraint system to solve.
	 */
	public 	static <
			L extends CompleteLattice<V>, 
			V extends LatticeElement<V>
			>
	void solveConstraintSystem(Worklist w, ConstraintSystem<L,V> cS){
		L lattice = cS.getLattice();
		int numberOfFlowVariables = cS.getNumberOfFlowVariables();
		
		for(int i = 0; i<numberOfFlowVariables; i++){
			w.insert(i);
		}
		
		int fV;
		V oldValue, newValue;
		while(!w.isEmpty()){
			fV = w.extractNextFlowVariable();
			
			oldValue = cS.getValueOf(fV);
			newValue = cS.updateValueOf(fV);
			
			if(!LatticeUtilities.equal(oldValue, newValue, lattice)){
				for(int i: cS.getVariablesInfluencedBy(fV)){
					w.insert(i);
				}
			}
		}		
	}
	
}
