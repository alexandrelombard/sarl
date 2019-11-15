package io.sarl.intellij.antlr.lexer;

import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.project.Project;
import io.sarl.intellij.antlr.misc.Pair;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenSource;

/** Make a PsiBuilder look like a source of ANTLR tokens. PsiBuilder
 *  provides tokens created by the lexer created in
 *  {@link ParserDefinition#createLexer(Project)}. This is the bridge
 *  between the ANTLR lexer and parser objects. Normally we just create
 *  a {@link org.antlr.runtime.CommonTokenStream} but the IDE has
 *  control and asks our ParserDefinition for the lexer and parser. This
 *  is how we hook them together. When IDE ask ParserDefinition for a
 *  parser, we will create one of these attached to the PsiBuilder.
 */
public class PSITokenSource implements TokenSource {
	protected PsiBuilder builder;

	public PSITokenSource(PsiBuilder builder) {
		this.builder = builder;
	}

	@Override
	public String getSourceName() {
		return null;
	}

	/** Create an ANTLR Token from the current token type of the builder
	 *  then advance the builder to next token (which ultimately calls an
	 *  ANTLR lexer). The {@link ANTLRLexerAdaptor} creates tokens via
	 *  an ANTLR lexer but converts to {@link TokenIElementType} and here
	 *  we have to convert back to an ANTLR token using what info we
	 *  can get from the builder. We lose info such as the original channel.
	 *  So, whitespace and comments (typically hidden channel) will look like
	 *  real tokens. Jetbrains uses {@link ParserDefinition#getWhitespaceTokens()}
	 *  and {@link ParserDefinition#getCommentTokens()} to strip these before
	 *  our ANTLR parser sees them.
	 */
	@Override
	public Token nextToken() {
		ProgressIndicatorProvider.checkCanceled();

		TokenIElementType ideaTType = (TokenIElementType)builder.getTokenType();
		int type = ideaTType!=null ? ideaTType.getANTLRTokenType() : Token.EOF;

		int channel = Token.DEFAULT_CHANNEL;
		Pair<TokenSource, CharStream> source = new Pair<>(this, null);
		String text = builder.getTokenText();
		int start = builder.getCurrentOffset();
		int length = text != null ? text.length() : 0;
		int stop = start + length - 1;
		// PsiBuilder doesn't provide line, column info
		int line = 0;
		int charPositionInLine = 0;
		CommonToken t = new CommonToken(type, text);
//		Token t = tokenFactory.create(source, type, text, channel, start, stop, line, charPositionInLine);
		builder.advanceLexer();
//		System.out.println("TOKEN: "+t);
		return t;
	}



}
