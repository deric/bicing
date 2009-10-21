/*
 * Created on Feb 16, 2005
 *
 */
package aima.test.probabilitytest;

import aima.probability.Randomizer;

/**
 * @author Ravi Mohan
 * 
 */

public class MockRandomizer implements Randomizer {

	private double[] values;

	private int index;

	public MockRandomizer(double[] values) {
		this.values = values;
		this.index = 0;
	}

	public double nextDouble() {
		if (index == values.length) {
			index = 0;
		}

		double value = values[index];
		index++;
		return value;
	}

}