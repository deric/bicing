package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepClauseFactor extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();
	private Clause factor = null;
	private Clause factorOf = null;

	public ProofStepClauseFactor(Clause factor, Clause factorOf) {
		this.factor = factor;
		this.factorOf = factorOf;
		this.predecessors.add(factorOf.getProofStep());
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return factor.toString();
	}

	public String getJustification() {
		return "Factor of " + factorOf.getProofStep().getStepNumber();
	}
	// END-ProofStep
	//
}
