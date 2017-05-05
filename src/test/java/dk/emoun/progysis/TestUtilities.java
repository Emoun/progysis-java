package dk.emoun.progysis;

import dk.emoun.progysis.programGraph.ProgramGraph;
import dk.emoun.progysis.programGraph.Transition;
import dk.emoun.progysis.lattices.CompleteLattice;
import dk.emoun.progysis.lattices.IncomparableLatticeException;
import dk.emoun.progysis.lattices.LatticeElement;
import dk.emoun.progysis.worklist.ConstraintSystem;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TestUtilities {
	
	public static <T>void addTransition(ProgramGraph<T> pg, int from, int to, T action) {
		pg.addOutgoingTransition(from, new Transition<T>(to, action));
	}
	
	public static <L extends CompleteLattice<V>, V extends LatticeElement<V>>void 
	assertComparison(V v1, V v2,L lattice)
	{
		try {
			assertTrue(lattice.compare(v1, v2));
		} catch (IncomparableLatticeException e) {
			fail("Incomparable: " + e.getMessage());
		}
	}
	
	public static <L extends CompleteLattice<V>, V extends LatticeElement<V>>void
	assertEquals(V expected, V actual, L lattice)
	{
		String actualString = lattice.stringRepresentation(actual);
		try {
			if(!lattice.compare(expected, actual)){
				fail("Was smaller than expected: " + actualString);
			}else if(!lattice.compare(actual, expected)){
				fail("Was larger than expected: " + actualString);
			}
		} catch (IncomparableLatticeException e) {
			fail("Incomparable: " + e.getMessage());
		}
	}
	
	public static <L extends CompleteLattice<V>, V extends LatticeElement<V>>void 
	assertFlowVariableValuesEqual(
			L lattice, 
			ConstraintSystem<L,V> cS, 
			List<V> values) 
	{
		if(cS.getNumberOfFlowVariables() != values.size()){
			fail("Number of values does not equal number of flow variables: " + 
					cS.getNumberOfFlowVariables() + "," + values.size()
					);
		}
		for(int i = 0; i<cS.getNumberOfFlowVariables(); i++){
			try{
				assertEquals(values.get(i), cS.getValueOf(i), lattice);
			}catch(AssertionError e){
				fail("Variable " + i + " mismatch: " + e.getMessage());
			}
		}
	}
	
	public static <L extends CompleteLattice<V>, V extends LatticeElement<V>>void 
	assertFlowVariableValuesEqual(
			L lattice, 
			ConstraintSystem<L,V> cS, 
			V... values) 
	{
		List<V> valueList = new ArrayList<V>();
		for(int i = 0; i<values.length; i++){
			valueList.add(values[i]);
		}
		assertFlowVariableValuesEqual(lattice, cS, valueList);
	}
	
	public static <L extends CompleteLattice<V>, V extends LatticeElement<V>>void 
	assertAllFlowVariableValuesEqual(
			L lattice, 
			ConstraintSystem<L,V> cS, 
			V value) 
	{
		List<V> values = new ArrayList<V>();
		for(int i = 0; i<cS.getNumberOfFlowVariables(); i++){
			values.add(value);
		}
		assertFlowVariableValuesEqual(lattice, cS, values);
	}
}
