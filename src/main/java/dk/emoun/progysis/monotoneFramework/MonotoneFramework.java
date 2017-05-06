package dk.emoun.progysis.monotoneFramework;

import java.util.Set;
import java.util.function.Function;

import dk.emoun.progysis.lattices.CompleteLattice;
import dk.emoun.progysis.lattices.LatticeElement;
import dk.emoun.progysis.lattices.TotalFunction;
import dk.emoun.progysis.worklist.BaseConstraint;
import dk.emoun.progysis.worklist.ConstraintSystem;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.EdgeReversedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

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
	 * Functions as E'
	 */
	private SimpleDirectedGraph<Integer, K> programGraph; 
	
	/**
	 * The formal q0 in the Monotone Framework
	 */
	private int q0;
	
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
	 * @param programGraph
	 * The program graph to run an analysis on. 
	 * All vertices in the graph must have a zero or positive unique value.
	 * @param q0
	 * The initial state of the program graph, the formal q0 in the constructed Monotone Framework.
	 * @param forwardAnalysis
	 * Whether the framework implements a forward analysis (then should be {@code true}) 
	 * or backwards analysis (should be {@code false}).
	 */
	public MonotoneFramework(	L latticeAndExtremalValue, 
								F monotoneFunctions, 
								SimpleDirectedGraph<Integer,K> programGraph,
								int q0,
								boolean forwardAnalysis) 
	{
		this.latticeAndExtremalValue = latticeAndExtremalValue; 
		this.monotoneFunctionMapper = monotoneFunctions;
		this.programGraph = programGraph;
		this.q0 = q0;
		this.forwardAnalysis = forwardAnalysis;
	}
	
//Methods
	
	/**
	 * Constructs the Constrain System that the instance gives rise to.
	 * @return
	 */
	public ConstraintSystem<L,L> constraintSystem(){
		DirectedGraph<Integer,K> graphToAnalyse;
		
		if(forwardAnalysis){
			graphToAnalyse = this.programGraph;
		}else{
			graphToAnalyse = new EdgeReversedGraph<Integer, K>(this.programGraph);
		}
		
		ConstraintSystem<L,L> cS = new ConstraintSystem<L,L>(	
										latticeAndExtremalValue, 
										graphToAnalyse.vertexSet().size()
										);
		
		//assign the initial state the extremal value
		cS.addIndependentConstraintToVariable(
				q0, new BaseConstraint<L>(latticeAndExtremalValue));
		
		for(int i = 0; i<cS.getNumberOfFlowVariables(); i++){
			Set<K> outgoingTransitions = 
					graphToAnalyse.outgoingEdgesOf(i);
			
			//Calculate the constraints for the states this state transitions to
			for(K action :outgoingTransitions){
					int qs = i,
						qt = graphToAnalyse.getEdgeTarget(action);
					
					Function<L, L> calculateConstraintValueGivenState = 
							(L state) -> monotoneFunctionMapper
										.apply(action, state)
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
