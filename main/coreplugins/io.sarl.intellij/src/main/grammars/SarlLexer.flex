package io.sarl.intellij.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static io.sarl.intellij.psi.SarlTypes.*;

%%

%{
  public _SarlLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _SarlLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+


%%
<YYINITIAL> {
  {WHITE_SPACE}      { return WHITE_SPACE; }

  "number"           { return SARL_NUMBER; }
  "string"           { return SARL_STRING; }
  "id"               { return SARL_ID; }


}

[^] { return BAD_CHARACTER; }
