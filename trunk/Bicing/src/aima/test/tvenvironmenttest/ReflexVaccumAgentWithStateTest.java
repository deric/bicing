package aima.test.tvenvironmenttest;

import junit.framework.TestCase;

import aima.basic.BasicEnvironmentView;
import aima.basic.vaccum.ReflexVaccumAgentWithState;
import aima.basic.vaccum.TrivialVaccumEnvironment;

/**
 * @author Ciaran O'Reilly
 * 
 */

public class ReflexVaccumAgentWithStateTest extends TestCase {
	private ReflexVaccumAgentWithState agent;

	private StringBuffer envChanges;

	@Override
	public void setUp() {
		agent = new ReflexVaccumAgentWithState();
		envChanges = new StringBuffer();
	}

	public void testCleanClean() {
		TrivialVaccumEnvironment tve = new TrivialVaccumEnvironment("Clean",
				"Clean");
		tve.addAgent(agent, "A");

		tve.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command);
			}
		});

		tve.stepUntilDone();

		assertEquals("RightNoOP", envChanges.toString());
	}

	public void testCleanDirty() {
		TrivialVaccumEnvironment tve = new TrivialVaccumEnvironment("Clean",
				"Dirty");
		tve.addAgent(agent, "A");

		tve.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command);
			}
		});

		tve.stepUntilDone();

		assertEquals("RightSuckNoOP", envChanges.toString());
	}

	public void testDirtyClean() {
		TrivialVaccumEnvironment tve = new TrivialVaccumEnvironment("Dirty",
				"Clean");
		tve.addAgent(agent, "A");

		tve.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command);
			}
		});

		tve.stepUntilDone();

		assertEquals("SuckRightNoOP", envChanges.toString());
	}

	public void testDirtyDirty() {
		TrivialVaccumEnvironment tve = new TrivialVaccumEnvironment("Dirty",
				"Dirty");
		tve.addAgent(agent, "A");

		tve.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command);
			}
		});

		tve.stepUntilDone();

		assertEquals("SuckRightSuckNoOP", envChanges.toString());
	}

}
