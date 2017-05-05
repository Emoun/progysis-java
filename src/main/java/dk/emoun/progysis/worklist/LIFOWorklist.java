package dk.emoun.progysis.worklist;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A worklist who's internal ordering is that of a stack (Last in, first out).
 */
public class LIFOWorklist implements Worklist{
	//Fields
		private Deque<Integer> stack;
		
	//Constructors
		public LIFOWorklist(){
			this.stack = new ArrayDeque<Integer>();
		}
		
	//methods 
		@Override
		public boolean isEmpty() {
			return stack.isEmpty();
		}

		@Override
		public void insert(int flowVariable) {
			stack.push(flowVariable);
		}

		@Override
		public int extractNextFlowVariable() {
			return stack.pop();
		}
}
