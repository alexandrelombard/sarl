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

DOCUMENTATION_BLOCK="/"\*\*(.|\n)*\*"/"
LINE_COMMENT=("//".*|"/"\*.*\*"/")
BLOCK_COMMENT="/"\*(.|\n)*\*"/"
SPACE=[ \t\n\x0B\f\r]+
ID=[:letter:][a-zA-Z_0-9]*
NUMBER=[0-9]+(\.[0-9]*)?
STRING=('([^'\\]|\\.)*'|\"([^\"\\]|\\.)*\")

%%
<YYINITIAL> {
  {WHITE_SPACE}              { return WHITE_SPACE; }

  "{"                        { return LBRACE; }
  "}"                        { return RBRACE; }
  "["                        { return LBRACK; }
  "]"                        { return RBRACK; }
  "("                        { return LPAREN; }
  ")"                        { return RPAREN; }
  ":"                        { return COLON; }
  "::"                       { return COLONCOLON; }
  ";"                        { return SEMICOLON; }
  ","                        { return COMMA; }
  "="                        { return EQ; }
  "!="                       { return EXCLEQ; }
  "=="                       { return EQEQ; }
  "#"                        { return SHA; }
  "!"                        { return EXCL; }
  "+="                       { return PLUSEQ; }
  "+"                        { return PLUS; }
  "-="                       { return MINUSEQ; }
  "-"                        { return MINUS; }
  "|="                       { return OREQ; }
  "&&"                       { return ANDAND; }
  "&="                       { return ANDEQ; }
  "&"                        { return AND; }
  "|"                        { return OR; }
  "<"                        { return LT; }
  "^="                       { return XOREQ; }
  "^"                        { return XOR; }
  "*="                       { return MULEQ; }
  "*"                        { return MUL; }
  "/="                       { return DIVEQ; }
  "/"                        { return DIV; }
  "%="                       { return REMEQ; }
  "%"                        { return REM; }
  ">"                        { return GT; }
  "."                        { return DOT; }
  ".."                       { return DOTDOT; }
  "..."                      { return DOTDOTDOT; }
  "..="                      { return DOTDOTEQ; }
  "=>"                       { return FAT_ARROW; }
  "->"                       { return ARROW; }
  "?"                        { return Q; }
  "@"                        { return AT; }
  "_"                        { return UNDERSCORE; }
  "$"                        { return DOLLAR; }
  "float"                    { return FLOAT; }

  {DOCUMENTATION_BLOCK}      { return DOCUMENTATION_BLOCK; }
  {LINE_COMMENT}             { return LINE_COMMENT; }
  {BLOCK_COMMENT}            { return BLOCK_COMMENT; }
  {SPACE}                    { return SPACE; }
  {ID}                       { return ID; }
  {NUMBER}                   { return NUMBER; }
  {STRING}                   { return STRING; }

}

[^] { return BAD_CHARACTER; }
