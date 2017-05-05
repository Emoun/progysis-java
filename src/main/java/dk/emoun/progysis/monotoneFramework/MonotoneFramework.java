package dk.emoun.progysis.monotoneFramework;

import java.util.List;
import java.util.function.Function;

import dk.emoun.progysis.lattices.CompleteLattice;
import dk.emoun.progysis.lattices.LatticeElement;
import dk.emoun.progysis.lattices.TotalFunction;
import dk.emoun.progysis.worklist.BaseConstraint;
import dk.emoun.progysis.worklist.ConstraintSystem;
import dk.emoun.progysis.programGraph.ProgramGraph;
import dk.emoun.progysis.programGraph.Transition;

/**
 * Represents Monotone Frameworks which use Program Graphs and have the form:<br>
 * 	<ul>
 * 		<li>L' is the {@link CompleteLattice Complete Lattice} of the Monotone Framework.</li>
 * 		<li>F' is the set of Monotone Functions of the Monotone Framework.</li>
 * 		<li>E' is a finite set of edges (with action) of the program graph</li>
 * 		<li>q0 is the initial (or final) node of the program graph</li>
 * 		<li>i is an extremal value for q0</li>
 * 		<li>f is the mapping, from the actions of the program graph to transfer functions in F'</li>
 * 		<li></li>
 * 	</ul>
 * 
 * An instance of a Monotone Framework gives rise to a {@link ConstraintSystem},
 * which can be constructed using the {@link #constraintSystem()} method.
 * 
 * @param <K>
 * The action type the monotone functions evaluate
 * @param <L>
 * The Complete Lattice type of the Monotone Framework
 * @param <V>
 * The Complete Lattice element type.
 * @param <F>
 * The type of the transfer functions in F'
 */
public class MonotoneFramework
		<
			K,
			L extends TotalFunction<L,?,? extends CompleteLattice<V>, V>,
			V extends LatticeElement<V>,
			F extends StableMonotoneFunction<K, L> 
		> 
{
	
//Fields
	/**
	 * The Complete Lattice of the Monotone Framework, and the extremal value for q0.<br>
	 * This is therefore both L' and i in the Monotone Framework.
	 */
	private L latticeAndExtremalValue; 
	
	/**
	 * This single Monotone Function functions as a mapping f
	 * to the formal Monotone Functions F'.
	 */
	private F monotoneFunctionMapper; 
	
	/**
	 * Functions as E' and q0
	 */
	private ProgramGraph<K> graph; 
	
	/**
	 * Whether the instance of defines a forward analysis
	 */
	private boolean forwardAnalysis;
//Constructors
	/**
	 * Constructs a Monotone Framework with the given form.
	 * @param latticeAndExtremalValue
	 * The Complete Lattice of the framework. 
	 * Its value is also assumed to be the extremal value of the framework.
	 * @param monotoneFunctions
	 * The monotone functions of the framework. It is assumed that
	 * if the framework formally is defined by multiple monotone functions,
	 * this function must contain them, implementing a way to choose between the formal functions.
	 * @param graph
	 * The program graph to run an analysis on. 
	 * Its initial state is assumed to be the formal q0 in the constructed Monotone Framework.
	 * @param forwardAnalysis
	 * Whether the framework implements a forward analysis (then should be {@code true}) 
	 * or backwards analysis (should be {@code false}).
	 */
	public MonotoneFramework(	L latticeAndExtremalValue, 
								F monotoneFunctions, 
								ProgramGraph<K> graph, 
								boolean forwardAnalysis) 
	{
		this.latticeAndExtremalValue = latticeAndExtremalValue; 
		this.monotoneFunctionMapper = monotoneFunctions;
		this.graph = graph;
		this.forwardAnalysis = forwardAnalysis;
	}
	
//Methods
	
	/**
	 * Constructs the Constrain System that the instance gives rise to.
	 * @return
	 */
	public ConstraintSystem<L,L> constraintSystem(){
		int q0 = graph.getInitialState();
		ProgramGraph<K> graphToAnalyse;
		
		if(forwardAnalysis){
			graphToAnalyse = this.graph;
		}else{
			graphToAnalyse = this.graph.reverse();
		}
		
		ConstraintSystem<L,L> cS = new ConstraintSystem<L,L>(	
										latticeAndExtremalValue, 
										graphToAnalyse.getNumberOfStates()
										);
		
		//assign the initial state the extremal value
		cS.addIndependentConstraintToVariable(
				q0, new BaseConstraint<L>(latticeAndExtremalValue));
		
		for(int i = 0; i<cS.getNumberOfFlowVariables(); i++){
			List<Transition<K>> outgoingTransitions = 
					graphToAnalyse.getOutgoingTransitions(i);
			
			//Calculate the constraints for the states this state transitions to
			for(int j = 0; j<outgoingTransitions.size(); j++){
					final Transition<K> transition = outgoingTransitions.get(j);
					int qs = i,
						qt = transition.getTo();
					
					Function<L, L> calculateConstraintValueGivenState = 
							(L state) -> monotoneFunctionMapper
										.applyFunction(transition.getAction(), state)
							;
					
					cS.addConstraintToVariableDependentOnVariable(
							qt, 
							qs, 
							calculateConstraintValueGivenState
							);
			}
		}
		return cS;
	}

}
