/* TAKEN FROM https://cswiki.cs.byu.edu/cs-236/datalog-parser */

grammar Datalog;

@header {
package comp6591.antlr;
}

/********************************************************
 * Productions
 ********************************************************/
datalogProgram:	/*SCHEMES COLON scheme schemeList   RULES COLON*/ ruleList   /* FACTS COLON */ factList   /*QUERIES COLON query queryList*/;
	

/*scheme:	ID LEFT_PAREN ID idList RIGHT_PAREN;
	

schemeList:	scheme schemeList | lambda;*/
	

idList:	COMMA ID idList | lambda;

			
fact:	PREDICATE_NAME LEFT_PAREN STRING stringList RIGHT_PAREN PERIOD;


factList:	fact factList | lambda;

	
aRule:	headPredicate COLON_DASH predicate predicateList PERIOD;


ruleList:	aRule ruleList | lambda	;

	
headPredicate:	PREDICATE_NAME LEFT_PAREN ID idList RIGHT_PAREN;

	
builtInPredicate:	parameter operator parameter;

	
predicate:	builtInPredicate | PREDICATE_NAME LEFT_PAREN parameter parameterList RIGHT_PAREN;

	
predicateList:	COMMA predicate predicateList | lambda;

	
parameter:	STRING | ID;

	
parameterList:	COMMA parameter parameterList | lambda;

	
operator:	EQUAL | NONEQUAL | GREATER | LESS | GREATERSTRICT | LESSSTRICT;


query:      Q_MARK COLON_DASH predicate;


queryList:	query queryList | lambda;


stringList:	COMMA STRING stringList | lambda ;

lambda	:	;


/********************************************************
 * Token definitions from the lexer project
 * IGNORE: INCLUDED FOR COMPLETENESS FOR ANTLR
 ********************************************************/
		
COMMA	:	','
	;

PERIOD	:	'.'
	;

Q_MARK	:	'?'
	;

LEFT_PAREN	: '('
		;

RIGHT_PAREN	: ')'
		;

COLON		: ':'
		;

COLON_DASH	: ':-'
		;

EQUAL	: '='
		;

NONEQUAL	: '<>'
		;

GREATER	: '>'
		;

GREATERSTRICT	: '>='
		;

LESS	: '<'
		;

LESSSTRICT	: '<='
		;

/*SCHEMES		: 'Schemes'
		;
FACTS		: 'Facts'
		;
RULES		: 'Rules'
		;
/*QUERIES		: 'Queries'
		;*/
    
PREDICATE_NAME  :	('a'..'z')('a'..'z'|'A'..'Z'|'0'..'9')*
    ;

ID  :	('a'..'z'|'A'..'Z') ('a'..'z'|'A'..'Z'|'0'..'9')*
    ;

STRING
    :  '\'' ( ESC_SEQ | ~('\\'|'\'') )* '\''
    ;

COMMENT
    :   ('#' ~('\n'|'\r')* '\r'? '\n'
    |   '#|' .*? '|#') -> skip
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) -> skip
    ;

fragment
HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment
ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment
OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
		