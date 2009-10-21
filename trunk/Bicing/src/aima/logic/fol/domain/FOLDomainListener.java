package aima.logic.fol.domain;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface FOLDomainListener {
	void skolemConstantAdded(FOLDomainSkolemConstantAddedEvent event);

	void skolemFunctionAdded(FOLDomainSkolemFunctionAddedEvent event);
	
	void answerLiteralNameAdded(FOLDomainAnswerLiteralAddedEvent event);
}
