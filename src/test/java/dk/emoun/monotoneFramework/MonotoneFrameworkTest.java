package MicroC_language.analysis.monotoneFramework;

import static org.junit.Assert.*;

import org.junit.Test;

import MicroC_language.analysis.ProgramGraph;
import MicroC_language.analysis.Transition;
import MicroC_language.analysis.detectionOfSigns.DSMFMapper;
import MicroC_language.analysis.detectionOfSigns.SignPowerSet;
import MicroC_language.analysis.detectionOfSigns.SignSet;
import MicroC_language.analysis.detectionOfSigns.SignsTotalFunction;
import MicroC_language.analysis.lattices.IncomparableLatticeException;
import MicroC_language.analysis.worklist.ConstraintSystem;
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
