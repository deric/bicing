package Bicing.test;

import Bicing.test.solution.SolutionTest;
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
                return suite;
        }
}
