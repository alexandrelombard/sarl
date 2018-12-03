/*
 * $Id$
 *
 * File is automatically generated by the Xtext language generator.
 * Do not change it.
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2018 the original authors or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.lang.idea.lang.parser;

import com.google.inject.Inject;
import com.intellij.lang.PsiBuilder;
import io.sarl.lang.idea.lang.SARLElementTypeProvider;
import io.sarl.lang.idea.parser.antlr.internal.PsiInternalSARLParser;
import io.sarl.lang.services.SARLGrammarAccess;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.antlr.runtime.TokenStream;
import org.eclipse.xtext.idea.parser.AbstractPsiAntlrParser;
import org.eclipse.xtext.idea.parser.AbstractXtextPsiParser;

public class SARLPsiParser extends AbstractXtextPsiParser {

	private static final Set<String> INITIAL_HIDDEN_TOKENS = new HashSet<String>(Arrays.asList("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT"));

	@Inject 
	private SARLGrammarAccess grammarAccess;

	@Inject 
	private SARLElementTypeProvider elementTypeProvider;

	@Override
	protected AbstractPsiAntlrParser createParser(PsiBuilder builder, TokenStream tokenStream) {
		return new PsiInternalSARLParser(builder, tokenStream, elementTypeProvider, grammarAccess);
	}

	@Override
	protected Set<String> getInitialHiddenTokens() {
		return INITIAL_HIDDEN_TOKENS;
	}
}