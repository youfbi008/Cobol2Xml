lexer grammar KG;

@header {
  package koopa.grammars.generator;
}

T__38 : 'def' ;
T__39 : 'returns' ;
T__40 : 'end' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 130
COMMENT : '#' (~('\n' | '\r'))* { $channel = HIDDEN; } ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 132
NEWLINE : ( ('\r\n') => '\r\n' | '\r' | '\n' ) { $channel = HIDDEN; } ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 134
IDENTIFIER : LETTER ( LETTER | DIGIT | '-' | '_' )* ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 136
LITERAL : '\'' (~('\'' | '\n' | '\r'))+ '\'';

// $ANTLR src "src/koopa/grammars/generator/KG.g" 138
NUMBER : DIGIT+ ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 140
WHITESPACE : (' ' | '\t')+ { $channel = HIDDEN; } ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 142
EQUALS : '=' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 144
OPEN_PAREN : '(' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 146
CLOSE_PAREN : ')' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 148
OPEN_BRACKET : '[' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 150
CLOSE_BRACKET : ']' ;

// TODO Balanced braces ?
// TODO Newlines.
// $ANTLR src "src/koopa/grammars/generator/KG.g" 154
NATIVE_CODE : '{' (~ '}')* '}' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 156
STAR : '*' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 158
PLUS : '+' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 160
SKIP_TO : '-->' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 162
DOT : '.' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 164
PIPE : '|' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 166
COMMA : ',' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 168
BANG : '!' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 170
NOT : '-' ;

// $ANTLR src "src/koopa/grammars/generator/KG.g" 172
fragment LETTER : 'a'..'z' | 'A'..'Z' ;
// $ANTLR src "src/koopa/grammars/generator/KG.g" 173
fragment DIGIT : '0'..'9' ;
