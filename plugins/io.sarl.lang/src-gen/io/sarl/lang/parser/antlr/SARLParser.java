/* $Id: oct. 19, 2015 10:05:53$
 * File is automatically generated by the Xtext language generator. Do not change it.
 *
 */
package io.sarl.lang.parser.antlr;

import com.google.inject.Inject;

import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import io.sarl.lang.services.SARLGrammarAccess;

public class SARLParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {
	
	@Inject
	private SARLGrammarAccess grammarAccess;
	
	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	
	@Override
	protected io.sarl.lang.parser.antlr.internal.InternalSARLParser createParser(XtextTokenStream stream) {
		return new io.sarl.lang.parser.antlr.internal.InternalSARLParser(stream, getGrammarAccess());
	}
	
	@Override 
	protected String getDefaultRuleName() {
		return "SarlScript";
	}
	
	public SARLGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(SARLGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
	
}
