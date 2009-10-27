package aima.test.probabilitytest;

import junit.framework.TestCase;
import aima.probability.BayesNet;
import aima.probability.BayesNetNode;
import aima.probability.EnumerationAsk;
import aima.probability.Query;

/**
 * @author Ravi Mohan
 * 
 */

public class EnumerationAskTest extends TestCase {

	public void testEnumerationAskAimaExample() {
		Query q = new Query("Burglary",
				new String[] { "JohnCalls", "MaryCalls" }, new boolean[] {
						true, true });
		double[] probs = EnumerationAsk.ask(q, createBurglaryNetwork());
		assertEquals(0.284, probs[0], 0.001);
		assertEquals(0.716, probs[1], 0.001);
	}

	public void testEnumerationAllVariablesExcludingQueryKnown() {
		Query q = new Query("Alarm", new String[] { "Burglary", "EarthQuake",
				"JohnCalls", "MaryCalls" }, new boolean[] { false, false, true,
				true });

		double[] probs = EnumerationAsk.ask(q, createBurglaryNetwork());
		assertEquals(0.557, probs[0], 0.001);
		assertEquals(0.442, probs[1], 0.001);
	}

	private BayesNet createBurglaryNetwork() {
		BayesNetNode burglary = new BayesNetNode("Burglary");
		BayesNetNode earthquake = new BayesNetNode("EarthQuake");
		BayesNetNode alarm = new BayesNetNode("Alarm");
		BayesNetNode johnCalls = new BayesNetNode("JohnCalls");
		BayesNetNode maryCalls = new BayesNetNode("MaryCalls");

		alarm.influencedBy(burglary, earthquake);
		johnCalls.influencedBy(alarm);
		maryCalls.influencedBy(alarm);

		burglary.setProbability(true, 0.001);// TODO behaviour changes if
		// root node
		earthquake.setProbability(true, 0.002);

		alarm.setProbability(true, true, 0.95);
		alarm.setProbability(true, false, 0.94);
		alarm.setProbability(false, true, 0.29);
		alarm.setProbability(false, false, 0.001);

		johnCalls.setProbability(true, 0.90);
		johnCalls.setProbability(false, 0.05);

		maryCalls.setProbability(true, 0.70);
		maryCalls.setProbability(false, 0.01);

		BayesNet net = new BayesNet(burglary, earthquake);
		return net;
	}
}