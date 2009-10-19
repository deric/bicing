/*
 * Created on Sep 14, 2004
 *
 */
package aima.search.informed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aima.util.Util;
import aima.search.framework.Node;
import aima.search.framework.NodeExpander;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchUtils;

/**
 * @author Ravi Mohan
 *  
 */
public class SimulatedAnnealingSearch extends NodeExpander implements Search {
    	public enum SearchOutcome {
		FAILURE, SOLUTION_FOUND
	};

	private final Scheduler scheduler;

	private SearchOutcome outcome = SearchOutcome.FAILURE;

	private Object lastState = null;

	private int steps;
        private boolean trace=false;
        

	public SimulatedAnnealingSearch() {
		this.steps = 10000;
		this.scheduler = new Scheduler();
	}

        public SimulatedAnnealingSearch(int steps, int stiter, int k, double lamb) {
		this.steps = steps;
		this.scheduler = new Scheduler(k,lamb,stiter);
	}

        public void traceOn(){trace=true;};
        
	public List search(Problem p) throws Exception {
		clearInstrumentation();
		Node current = new Node(p.getInitialState());
		Node next = null;
                Node best=current;
		List ret = new ArrayList();
               Random rnd= new Random();
                
                List children= expandNode(current, p);
         
		for (int step = 0; step < this.steps; step++) {
			double temp= scheduler.getTemp(step);
 			if (children.size() > 0) {
				//TODO take care of no possible expansion situation?
				next = (Node) Util.selectRandomlyFromList(children);
				double deltaE = getValue(p,next ) - getValue(p, current);
				//System.out.print("deltaE = "+deltaE+"\n");
                                double al=rnd.nextDouble();
                                double prob=Math.exp(deltaE / temp);
                                
                                if ( trace && (deltaE < 0.0)&& (al < prob)) System.out.println("Pr Acep="+prob+" Delta E="+deltaE+" Temp= "+temp);
                  // if ((deltaE < 0.0) && (al < prob))
                  //     System.out.println("Pr Acep="+prob+" "+al+" Delta E="+deltaE+" "+ getValue(p, current) +" "+ getValue(p, next)+" Temp= "+step);

				if ((deltaE > 0.0)||(al <= prob)){
                                    current=next;
                                   if (getValue(p, current) > getValue(p, best)) best=current;
                                   // changed=true;
                                } 
                                
                              
			}
                        children = expandNode(current, p);

		}
                lastState=best.getState();


                ret = SearchUtils.actionsFromNodes(best.getPathFromRoot());
		return ret;//Total Failure
	}
        
        
	private double getHeuristic(Problem p, Node aNode) {
		return p.getHeuristicFunction().getHeuristicValue(aNode.getState());
	}

	private double getValue(Problem p, Node n) {
		return -1 * getHeuristic(p, n); //assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
		//SA deals with gardient DESCENT
	}
	public SearchOutcome getOutcome() {
		return outcome;
	}

	public Object getLastSearchState() {
		return lastState;
	}

}