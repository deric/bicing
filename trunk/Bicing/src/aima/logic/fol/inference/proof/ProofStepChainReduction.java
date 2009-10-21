package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import aima.logic.fol.kb.data.Chain;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepChainReduction extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();
	private Chain reduction = null;
	private Chain nearParent, farParent = null;
	private Map<Variable, Term> subst = null;

	public ProofStepChainReduction(Chain reduction, Chain nearParent,
			Chain farParent, Map<Variable, Term> subst) {
		this.reduction = reduction;
		this.nearParent = nearParent;
		this.farParent = farParent;
		this.subst = subst;
		this.predecessors.add(farParent.getProofStep());
		this.predecessors.add(nearParent.getProofStep());
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return reduction.toString();
	}

	public String getJustification() {
		return "Reduction: " + nearParent.getProofStep().getStepNumber() + ","
				+ farParent.getProofStep().getStepNumber() + " " + subst;
	}
	// END-ProofStep
	//
}
