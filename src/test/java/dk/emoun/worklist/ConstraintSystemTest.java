package MicroC_language.analysis.worklist;

import static org.junit.Assert.*;

import java.util.function.Function;

import org.junit.Test;

import MicroC_language.analysis.detectionOfSigns.DSMFMapper;
import MicroC_language.analysis.detectionOfSigns.SignPowerSet;
import MicroC_language.analysis.detectionOfSigns.SignsTotalFunction;
import MicroC_language.analysis.lattices.IncomparableLatticeException;

public class ConstraintSystemTest {
	
	SignsTotalFunction stf = new SignsTotalFunction("x");
	
	@Test
	public void simpleTest() throws IncomparableLatticeException{
		
		ConstraintSystem<SignsTotalFunction, SignsTotalFunction> cS =
				new ConstraintSystem<SignsTotalFunction, SignsTotalFunction>(stf, 2);
		
		cS.addIndependentConstraintToVariable(
				0, 
				new BaseConstraint<SignsTotalFunction>(stf)
			);
		
		Function<SignsTotalFunction, SignsTotalFunction> x2C = 
				(SignsTotalFunction s) -> stf.join(s, s.getTop())
		;
		
		cS.addConstraintToVariableDependentOnVariable(1, 0, x2C);
		
		assertTrue(stf.compare(cS.getValueOf(0), stf));
		assertTrue(stf.compare(stf, cS.getValueOf(0)));
		
		assertTrue(stf.compare(cS.getValueOf(1), stf));
		assertTrue(stf.compare(stf, cS.getValueOf(1)));
		
		SignsTotalFunction x1New = cS.updateValueOf(0);
		assertTrue(stf.compare(x1New, stf));
		assertTrue(stf.compare(stf, x1New));
		
		assertTrue(stf.compare(cS.getValueOf(1), stf));
		assertTrue(stf.compare(stf, cS.getValueOf(1)));
		
		SignsTotalFunction x2New = cS.updateValueOf(1);
		assertTrue(stf.compare(x2New, stf.getTop()));
		assertTrue(stf.compare(stf.getTop(), x2New));
	}
	
	
	
}
