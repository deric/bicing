package Bicing.test;

import Bicing.test.solution.SolutionTest;
import Bicing.test.structure.StateTest;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author Tomas Barton 
 */
public class AllBicingTests {


    	public static Test suite() {
		TestSuite suite = new TestSuite();
                suite.addTest(new TestSuite(SolutionTest.class));
                suite.addTest(new TestSuite(StateTest.class));
                return suite;
        }
}
