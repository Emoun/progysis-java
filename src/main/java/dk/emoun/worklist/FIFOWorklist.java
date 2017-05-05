package MicroC_language.analysis.worklist;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A worklist who's internal ordering is that of a queue (first in, first out).
 */
public class FIFOWorklist implements Worklist{

//Fields
	private Queue<Integer> queue;
	
//Constructors
	public FIFOWorklist(){
		this.queue = new ArrayDeque<Integer>();
	}
	
//methods 
	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public void insert(int flowVariable) {
		queue.add(flowVariable);
	}

	@Override
	public int extractNextFlowVariable() {
		return queue.poll();
	}

}
