package aima.learning.neural;

import java.util.ArrayList;


public class IrisNNDataSet extends NNDataSet {

	@Override
	public void setTargetColumns() {
		// assumed that data from file has been pre processed
		// TODO this should be
		// somewhere else,in the
		// super class.
		// Type != class Aargh! I want more
		// powerful type systems
		targetColumnNumbers = new ArrayList<Integer>();
		int size = nds.get(0).size();
		targetColumnNumbers.add(size - 1); // last column
		targetColumnNumbers.add(size - 2); // last but one column
		targetColumnNumbers.add(size - 3); // and the one before that

	}

}
