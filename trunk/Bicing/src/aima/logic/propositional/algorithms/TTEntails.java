/*
 * Created on Dec 4, 2004
 *
 */
package aima.logic.propositional.algorithms;

import java.util.List;
import java.util.Set;

import aima.logic.propositional.parsing.PEParser;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.logic.propositional.visitors.SymbolCollector;
import aima.util.Converter;
import aima.util.SetOps;
import aima.util.Util;

/**
 * @author Ravi Mohan
 * 
 */

public class TTEntails {
	public boolean ttEntails(KnowledgeBase kb, String alpha) {
		Sentence kbSentence = kb.asSentence();
		Sentence querySentence = (Sentence) new PEParser().parse(alpha);
		SymbolCollector collector = new SymbolCollector();
		Set<Symbol> kbSymbols = collector.getSymbolsIn(kbSentence);
		Set<Symbol> querySymbols = collector.getSymbolsIn(querySentence);
		Set<Symbol> symbols = new SetOps<Symbol>().union(kbSymbols,
				querySymbols);
		List<Symbol> symbolList = new Converter<Symbol>().setToList(symbols);
		return ttCheckAll(kbSentence, querySentence, symbolList, new Model());
	}

	public boolean ttCheckAll(Sentence kbSentence, Sentence querySentence,
			List symbols, Model model) {
		if (symbols.isEmpty()) {
			if (model.isTrue(kbSentence)) {
				// System.out.println("#");
				return model.isTrue(querySentence);
			} else {
				// System.out.println("0");
				return true;
			}
		} else {
			Symbol symbol = (Symbol) Util.first(symbols);
			List rest = Util.rest(symbols);

			Model trueModel = model.extend(new Symbol(symbol.getValue()), true);
			Model falseModel = model.extend(new Symbol(symbol.getValue()),
					false);
			return (ttCheckAll(kbSentence, querySentence, rest, trueModel) && (ttCheckAll(
					kbSentence, querySentence, rest, falseModel)));
		}
	}
}