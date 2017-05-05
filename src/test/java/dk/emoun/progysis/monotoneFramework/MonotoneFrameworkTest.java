package dk.emoun.progysis.monotoneFramework;

import static org.junit.Assert.*;

import org.junit.Test;

import dk.emoun.progysis.ProgramGraph;
import dk.emoun.progysis.lattices.IncomparableLatticeException;
import dk.emoun.progysis.progysis.Transition;
import dk.emoun.progysis.progysis.detectionOfSigns.DSMFMapper;
import dk.emoun.progysis.progysis.detectionOfSigns.SignPowerSet;
import dk.emoun.progysis.progysis.detectionOfSigns.SignSet;
import dk.emoun.progysis.progysis.detectionOfSigns.SignsTotalFunction;
import dk.emoun.progysis.progysis.worklist.ConstraintSystem;
import MicroC_language.elements.Type;
import MicroC_language.elements.Variable;
import MicroC_language.elements.elementaryBlocks.DeclarationBlock;

public class MonotoneFrameworkTest {
	
//Helper methods
	
	private static final SignsTotalFunction stf = new SignsTotalFunction("x");


	public ProgramGraph getDecXProgram(){
		DeclarationBlock decX = new DeclarationBlock(new Variable("x", Type.INT),0,0);
		
		ProgramGraph graph = new ProgramGraph();
		graph.setInitialState(graph.newState());
		graph.setFinalState(graph.newState());
		
		graph.addOutgoingTransition(graph.getInitialState(), new Transition(graph.getFinalState(), decX));
		
		
		return graph;
	}
	
	
//test methods
	
	@Test
	public void simpleDetectionOfSigns() throws IncomparableLatticeException{
		ProgramGraph graph = getDecXProgram();
		MonotoneFramework<SignsTotalFunction, SignSet, DSMFMapper> detectionOfSigns = 
				new MonotoneFramework<SignsTotalFunction, SignSet, DSMFMapper>(
						stf,
						DSMFMapper.I,
						graph,
						true
				);
		ConstraintSystem<SignsTotalFunction,SignsTotalFunction> cS= 
				detectionOfSigns.constraintSystem();
		
		assertEquals(2, cS.getNumberOfFlowVariables());
		SignsTotalFunction x0 = cS.getValueOf(0);
		assertTrue(stf.isBottom(x0));
		assertTrue(stf.compare(stf, x0));
		assertTrue(stf.compare(x0, stf));
		
		SignsTotalFunction x1 = cS.getValueOf(1);
		assertTrue(stf.isBottom(x1));
		assertTrue(stf.compare(stf, x1));
		assertTrue(stf.compare(x1, stf));
		
		SignsTotalFunction x1Updated = cS.updateValueOf(1);
		assertTrue(stf.isTop(x1Updated));
		assertTrue(stf.compare(stf.getTop(), x1Updated));
		assertTrue(stf.compare(x1Updated, stf.getTop()));
	}
	
}
