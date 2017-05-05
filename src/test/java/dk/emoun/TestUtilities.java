package MicroC_language.analysis;

import MicroC_language.analysis.lattices.CompleteLattice;
import MicroC_language.analysis.lattices.IncomparableLatticeException;
import MicroC_language.analysis.lattices.LatticeElement;
import MicroC_language.analysis.worklist.ConstraintSystem;
import MicroC_language.elements.ArrayVariableExpr;
import MicroC_language.elements.BinaryExpr;
import MicroC_language.elements.BinaryOperator;
import MicroC_language.elements.ConstantExpr;
import MicroC_language.elements.ElementaryBlock;
import MicroC_language.elements.Expression;
import MicroC_language.elements.Type;
import MicroC_language.elements.UnaryExpr;
import MicroC_language.elements.UnaryOperator;
import MicroC_language.elements.Variable;
import MicroC_language.elements.VariableExpr;
import MicroC_language.elements.elementaryBlocks.ArrayDeclarationBlock;
import MicroC_language.elements.elementaryBlocks.ArrayReadBlock;
import MicroC_language.elements.elementaryBlocks.AssignmentBlock;
import MicroC_language.elements.elementaryBlocks.DeclarationBlock;
import MicroC_language.elements.elementaryBlocks.ExpressionBlock;
import MicroC_language.elements.elementaryBlocks.ReadBlock;
import MicroC_language.elements.elementaryBlocks.SkipBlock;
import MicroC_language.elements.elementaryBlocks.WriteBlock;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class TestUtilities {
	
	public static ProgramGraph getGraphForProgram1(){
		/* Program:
		 { 	int i; int x; int y; int z;
			int A[10];
			while (i<10){ 
				read A[i]; 
				i=i+1;
			}
			while (i<10){ 
				if (A[i]+1>=0){ 
					x=x+A[i]; 
					i=i+1;
				} else { 
					i=i+1; 
					break; 
				}
				y=y+1;
			}
			write x/y;
			read z;
		}
		 */
		ProgramGraph pg = new ProgramGraph();
		
		int 	q0 = pg.newState(),
				q1 = pg.newState(),
				q2 = pg.newState(),
				q3 = pg.newState(),
				q4 = pg.newState(),
				q5 = pg.newState(),
				q6 = pg.newState(),
				q7 = pg.newState(),
				q8 = pg.newState(),
				q9 = pg.newState(),
				q10 = pg.newState(),
				q11 = pg.newState(),
				q12 = pg.newState(),
				q13 = pg.newState(),
				q14 = pg.newState(),
				q15 = pg.newState(),
				q16 = pg.newState(),
				q17 = pg.newState();
		Variable 	i = new Variable("i", Type.INT),
					x = new Variable("x", Type.INT),
					y = new Variable("y", Type.INT),
					z = new Variable("z", Type.INT),
					A = new Variable("A", Type.INT);
		
		ArrayVariableExpr Ai = new ArrayVariableExpr(A, new VariableExpr(i));
		BinaryExpr whileCondition = new BinaryExpr(new VariableExpr(i), new ConstantExpr(10), BinaryOperator.LT);
		AssignmentBlock incI = assignBlock(i, new BinaryExpr(new VariableExpr(i), new ConstantExpr(1), BinaryOperator.PLUS),0,0);
		BinaryExpr ifCondition = new BinaryExpr(new BinaryExpr(Ai, new ConstantExpr(1), BinaryOperator.PLUS), new ConstantExpr(0), BinaryOperator.GE);
		
		addTransition(pg, q0, q3, declareBlock(i,0,0));
		addTransition(pg, q3, q4, declareBlock(x,0,0));
		addTransition(pg, q4, q5, declareBlock(y,0,0));
		addTransition(pg, q5, q6, declareBlock(z,0,0));
		addTransition(pg, q6, q2, new ArrayDeclarationBlock(A, new ConstantExpr(10),0,0));
		addTransition(pg, q2, q8, exprBlock(whileCondition,0,0));
		addTransition(pg, q2, q7, exprBlock(not(whileCondition,0,0),0,0));
		addTransition(pg, q8, q9, new ArrayReadBlock(A, new VariableExpr(i),0,0));
		addTransition(pg, q9, q2, incI);
		addTransition(pg, q7, q10, exprBlock(not(whileCondition,0,0),0,0));
		addTransition(pg, q7, q11, exprBlock(whileCondition,0,0));
		addTransition(pg, q11, q13, exprBlock(ifCondition,0,0));
		addTransition(pg, q11, q14, exprBlock(not(ifCondition,0,0),0,0));
		addTransition(pg, q13, q15, assignBlock(x, new BinaryExpr(new VariableExpr(x), Ai, BinaryOperator.PLUS),0,0));
		addTransition(pg, q14, q16, incI);
		addTransition(pg, q15, q12, incI);
		addTransition(pg, q16, q10, new SkipBlock(0,0));
		addTransition(pg, q12, q7, assignBlock(y, new BinaryExpr(new VariableExpr(y), new ConstantExpr(1), BinaryOperator.PLUS),0,0));
		addTransition(pg, q10, q17, new WriteBlock(new BinaryExpr(new VariableExpr(x), new VariableExpr(y), BinaryOperator.DIV),0,0));
		addTransition(pg, q17, q1, new ReadBlock(z,0,0));
		pg.setInitialState(q0);
		pg.setFinalState(q1);
		
		return pg;
	}
	
	public static ProgramGraph getDetectionOfSignsReportExampleProgramGraph(){
		/*Program:
		 	{
		 		int x;
		 		int y;
		 		y = -1;
		 		x = 0;
		 		while(y<0){
		 			x = x + 1;
		 			read y;
		 		}
		 	}
		 
		 */
		
		ProgramGraph pg = new ProgramGraph();
		
		int 	q0 = pg.newState(),
				q1 = pg.newState(),
				q2 = pg.newState(),
				q3 = pg.newState(),
				q4 = pg.newState(),
				q5 = pg.newState(),
				q6 = pg.newState(),
				q7 = pg.newState();
		Variable 	x = new Variable("x", Type.INT),
					y = new Variable("y", Type.INT);
		
		ConstantExpr one = new ConstantExpr(1);
		UnaryExpr minus1 = new UnaryExpr(one, UnaryOperator.NEGATE);
		ConstantExpr zero = new ConstantExpr(0);
		VariableExpr exprX = new VariableExpr(x);
		BinaryExpr xLessThanZero = 
				new BinaryExpr(exprX, zero, BinaryOperator.LT);
		BinaryExpr xPlus1 = new BinaryExpr(exprX, one, BinaryOperator.PLUS);
		
		addTransition(pg, q0, q2, declareBlock(x,0,0));
		addTransition(pg, q2, q3, declareBlock(y,0,0));
		addTransition(pg, q3, q4, assignBlock(y, minus1,0,0));
		addTransition(pg, q4, q5, assignBlock(x, zero,0,0));
		addTransition(pg, q5, q6, exprBlock(xLessThanZero,0,0));
		addTransition(pg, q5, q1, exprBlock(not(xLessThanZero,0,0),0,0));
		addTransition(pg, q6, q7, assignBlock(x, xPlus1,0,0));
		addTransition(pg, q7, q5, new ReadBlock(y,0,0));
		
		
		pg.setInitialState(q0);
		pg.setFinalState(q1);
		
		return pg;
		
	}

	public static ProgramGraph getReachingDefinitionsReportExampleProgramGraph(){
		/*Program:
		 	{
  				int x; int A[3];
  				read x;

			  	if(x>2) {
    				A[0]=x;
    				x = x + 1;
    				write x;
  				} else {
    				write x;
  				}
			}
		 */

		ProgramGraph pg = new ProgramGraph();

		int 	q0 = pg.newState(),
				q1 = pg.newState(),
				q2 = pg.newState(),
				q3 = pg.newState(),
				q4 = pg.newState(),
				q5 = pg.newState(),
				q6 = pg.newState(),
				q7 = pg.newState(),
				q8 = pg.newState();

		Variable x = new Variable("x", Type.INT), A = new Variable("A", Type.INT);

		ConstantExpr one = new ConstantExpr(1);
		ConstantExpr two = new ConstantExpr(2);
		VariableExpr exprX = new VariableExpr(x);
		BinaryExpr xBiggerThanTwo = new BinaryExpr(exprX, two, BinaryOperator.GT);
		BinaryExpr xPlus1 = new BinaryExpr(exprX, one, BinaryOperator.PLUS);
		

		addTransition(pg, q0, q2, declareBlock(x,q0,q2));
		addTransition(pg, q2, q3, declareBlock(A,q2,q3));
		addTransition(pg, q3, q4, new ReadBlock(x, q3, q4));
		addTransition(pg, q4, q5, exprBlock(not(xBiggerThanTwo,q4,q5),q4,q5));
		addTransition(pg, q5, q1, new WriteBlock(exprX,q5,q1));
		addTransition(pg, q4, q6, exprBlock(xBiggerThanTwo,q4,q6));
		addTransition(pg, q6, q7, assignBlock(A, exprX,q6,q7));
		addTransition(pg, q7, q8, assignBlock(x, xPlus1,q7,q8));
		addTransition(pg, q8, q1, new WriteBlock(exprX,q8,q1));

		pg.setInitialState(q0);
		pg.setFinalState(q1);

		return pg;
	}

	public static void addTransition(ProgramGraph pg, int from, int to, ElementaryBlock b) {
		pg.addOutgoingTransition(from, new Transition(to, b));
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
	
	public static DeclarationBlock declareBlock(Variable v, int from , int to){
		return new DeclarationBlock(v,from,to);
	}
	
	public static ExpressionBlock exprBlock(Expression e, int from , int to){
		return new ExpressionBlock(e,from,to);
	}
	
	public static AssignmentBlock assignBlock(Variable v, Expression e, int from , int to){
		return new AssignmentBlock(v, e,from,to);
	}
	
	public static UnaryExpr not(Expression e, int from , int to){
		return new UnaryExpr(e, UnaryOperator.NOT);
	}
	
	public static UnaryExpr negate(Expression e, int from , int to){
		return new UnaryExpr(e, UnaryOperator.NEGATE);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
