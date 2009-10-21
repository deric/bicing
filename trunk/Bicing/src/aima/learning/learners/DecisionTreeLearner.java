/*
 * Created on Jul 25, 2005
 *
 */
package aima.learning.learners;

import java.util.Iterator;
import java.util.List;

import aima.learning.framework.DataSet;
import aima.learning.framework.Example;
import aima.learning.framework.Learner;
import aima.learning.inductive.ConstantDecisonTree;
import aima.learning.inductive.DecisionTree;
import aima.util.Util;

/**
 * @author Ravi Mohan
 * 
 */

public class DecisionTreeLearner implements Learner {
	private DecisionTree tree;

	private String defaultValue;

	public DecisionTreeLearner() {
		this.defaultValue = "Unable To Classify";

	}

	// used when you have to test a non induced tree (eg: for testing)
	public DecisionTreeLearner(DecisionTree tree, String defaultValue) {
		this.tree = tree;
		this.defaultValue = defaultValue;
	}

	public void train(DataSet ds) {
		List<String> attributes = ds.getNonTargetAttributes();
		this.tree = decisionTreeLearning(ds, attributes,
				new ConstantDecisonTree(defaultValue));
	}

	public String predict(Example e) {
		return (String) tree.predict(e);
	}

	public int[] test(DataSet ds) {
		int[] results = new int[] { 0, 0 };

		for (Example e : ds.examples) {
			if (e.targetValue().equals(tree.predict(e))) {
				results[0] = results[0] + 1;
			} else {
				results[1] = results[1] + 1;
			}
		}
		return results;
	}

	private DecisionTree decisionTreeLearning(DataSet ds,
			List<String> attributeNames, ConstantDecisonTree defaultTree) {
		if (ds.size() == 0) {
			return defaultTree;
		}
		if (allExamplesHaveSameClassification(ds)) {
			return new ConstantDecisonTree(ds.getExample(0).targetValue());
		}
		if (attributeNames.size() == 0) {
			return majorityValue(ds);
		}
		String chosenAttribute = chooseAttribute(ds, attributeNames);

		DecisionTree tree = new DecisionTree(chosenAttribute);
		ConstantDecisonTree m = majorityValue(ds);

		List<String> values = ds.getPossibleAttributeValues(chosenAttribute);
		for (String v : values) {
			DataSet filtered = ds.matchingDataSet(chosenAttribute, v);
			List<String> newAttribs = Util.removeFrom(attributeNames,
					chosenAttribute);
			DecisionTree subTree = decisionTreeLearning(filtered, newAttribs, m);
			tree.addNode(v, subTree);

		}

		return tree;
	}

	private ConstantDecisonTree majorityValue(DataSet ds) {
		Learner learner = new MajorityLearner();
		learner.train(ds);
		return new ConstantDecisonTree(learner.predict(ds.getExample(0)));
	}

	private String chooseAttribute(DataSet ds, List<String> attributeNames) {
		double greatestGain = 0.0;
		String attributeWithGreatestGain = attributeNames.get(0);
		for (String attr : attributeNames) {
			double gain = ds.calculateGainFor(attr);
			if (gain > greatestGain) {
				greatestGain = gain;
				attributeWithGreatestGain = attr;
			}
		}

		return attributeWithGreatestGain;
	}

	private boolean allExamplesHaveSameClassification(DataSet ds) {
		String classification = ds.getExample(0).targetValue();
		Iterator<Example> iter = ds.iterator();
		while (iter.hasNext()) {
			Example element = iter.next();
			if (!(element.targetValue().equals(classification))) {
				return false;
			}

		}
		return true;
	}

	public DecisionTree getDecisionTree() {
		return tree;
	}

}
