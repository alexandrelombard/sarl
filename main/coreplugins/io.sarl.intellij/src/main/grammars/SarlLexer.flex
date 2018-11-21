package io.sarl.intellij.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static io.sarl.intellij.psi.SarlElementTypes.*;

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

LINE_COMMENT=("//".*|"/"\*.*\*"/")
BLOCK_COMMENT="/"\*(.|\n)*\*"/"
SPACE=[ \t\n\x0B\f\r]+
ID=[:letter:][a-zA-Z_0-9]*
NUMBER=[0-9]+(\.[0-9]*)?
STRING=('([^'\\]|\\.)*'|\"([^\"\\]|\\.)*\")

%%
<YYINITIAL> {
  {WHITE_SPACE}        { return WHITE_SPACE; }

  ";"                  { return SEMI; }
  "="                  { return EQ; }
  "("                  { return LP; }
  ")"                  { return RP; }
  "+"                  { return OP_1; }
  "-"                  { return OP_2; }
  "*"                  { return OP_3; }
  "/"                  { return OP_4; }
  "!"                  { return OP_5; }
  "float"              { return FLOAT; }

  {LINE_COMMENT}       { return LINE_COMMENT; }
  {BLOCK_COMMENT}      { return BLOCK_COMMENT; }
  {SPACE}              { return SPACE; }
  {ID}                 { return ID; }
  {NUMBER}             { return NUMBER; }
  {STRING}             { return STRING; }

}

[^] { return BAD_CHARACTER; }
