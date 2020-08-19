/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025;

    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }

    private int comment_count = 0;
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

%state COMMENT

LOW_ALPHA=[a-z]
UP_ALPHA=[A-Z]
ALPHA=[A-Za-z]
DIGIT=[0-9]
NONNEWLINE_WHITE_SPACE_CHAR=[\ \f\t\v]
WHITE_SPACE_CHAR=[\n\ \f\t\v]

%%

<YYINITIAL>{NONNEWLINE_WHITE_SPACE_CHAR}+ { }

<YYINITIAL,COMMENT>\n {
  curr_lineno++;
}

<YYINITIAL>"(*" {
  yybegin(COMMENT);
  comment_count++;
}

<YYINITIAL>"*)" {
  return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}

<COMMENT>"(*" {
  comment_count++;
}

<COMMENT>"*)" {
  comment_count--;
  if (comment_count == 0) {
    yybegin(YYINITIAL);
  }
}

<COMMENT>. { }

<YYINITIAL> class { return new Symbol(TokenConstants.CLASS); }
<YYINITIAL> else { return new Symbol(TokenConstants.ELSE); }
<YYINITIAL> fi { return new Symbol(TokenConstants.FI); }
<YYINITIAL> if { return new Symbol(TokenConstants.IF); }
<YYINITIAL> in { return new Symbol(TokenConstants.IN); }
<YYINITIAL> inherits { return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL> isvoid { return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL> let { return new Symbol(TokenConstants.LET); }
<YYINITIAL> loop { return new Symbol(TokenConstants.LOOP); }
<YYINITIAL> pool { return new Symbol(TokenConstants.POOL); }
<YYINITIAL> then { return new Symbol(TokenConstants.THEN); }
<YYINITIAL> while { return new Symbol(TokenConstants.WHILE); }
<YYINITIAL> case { return new Symbol(TokenConstants.CASE); }
<YYINITIAL> esac { return new Symbol(TokenConstants.ESAC); }
<YYINITIAL> new { return new Symbol(TokenConstants.NEW); }
<YYINITIAL> of { return new Symbol(TokenConstants.OF); }
<YYINITIAL> not { return new Symbol(TokenConstants.NOT); }

<YYINITIAL> false { return new Symbol(TokenConstants.BOOL_CONST, Boolean.FALSE); }
<YYINITIAL> true { return new Symbol(TokenConstants.BOOL_CONST, Boolean.TRUE); }

<YYINITIAL> {LOW_ALPHA}({ALPHA}|{DIGIT}|_)* {
  return new Symbol(TokenConstants.OBJECTID, new IdSymbol(yytext(), yytext().length(), -1));
}

<YYINITIAL> {UP_ALPHA}({ALPHA}|{DIGIT}|_)* {
  return new Symbol(TokenConstants.TYPEID, new IdSymbol(yytext(), yytext().length(), -1));
}

<YYINITIAL> {DIGIT}+ {
  return new Symbol(TokenConstants.INT_CONST, new IdSymbol(yytext(), yytext().length(), -1));
}

<YYINITIAL> \"[^\n\"]*\" {
  String str = yytext().substring(1,yytext().length() - 1);
  return new Symbol(TokenConstants.STR_CONST, new StringSymbol(str, str.length(), -1));
}

<YYINITIAL>"{" { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL>"}" { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>"(" { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>")" { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL>";" { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>":" { return new Symbol(TokenConstants.COLON); }

<YYINITIAL>"+" { return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"/" { return new Symbol(TokenConstants.DIV); }
<YYINITIAL>"-" { return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"*" { return new Symbol(TokenConstants.MULT); }

<YYINITIAL>"=" { return new Symbol(TokenConstants.EQ); }
<YYINITIAL>"<" { return new Symbol(TokenConstants.LT); }
<YYINITIAL>"." { return new Symbol(TokenConstants.DOT); }
<YYINITIAL>"~" { return new Symbol(TokenConstants.NEG); }
<YYINITIAL>"," { return new Symbol(TokenConstants.COMMA); }

<YYINITIAL>"<-" { return new Symbol(TokenConstants.ASSIGN); }

<YYINITIAL>"=>"			{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }

.                               { /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
                                  //System.err.println("LEXER BUG - UNMATCHED: " + yytext()); 
  return new Symbol(TokenConstants.ERROR, yytext());
}
