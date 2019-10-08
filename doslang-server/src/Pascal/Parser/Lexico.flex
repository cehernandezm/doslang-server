package Pascal.Parser;
import java_cup.runtime.Symbol;

%%
%class Lexico
%public
%line
%char
%caseless
%cup
%unicode
%ignorecase
%x TAG ATTR DOC
%init{
	yyline=1;
	yychar=1;
%init}
BLANCO=[ \r\t]+
D=[0-9]+

DD=[0-9]+("."[ |0-9]+)?
ID=[A-Za-z]+["_""-"0-9A-Za-z]*
CHAR = [\']([^\'\n]|(\\\'))*[\']
CADENA = [\"]([^\"\n]|(\\\"))*[\"]
COMENTARIOUNA=("##".*\r\n)|("##".*\n)|("##".*\r)
COMENTARIOMULTI="#$""#"*([^$]"#"|"$"[^#])*"$"*"$#"
%%
{COMENTARIOUNA} {}
{COMENTARIOMULTI} {}





\n {yychar=1;}




"+" 		{return new Symbol(sym.MAS,yyline,yychar, yytext());}
";" 		{return new Symbol(sym.PNTCOMA,yyline,yychar, yytext());}
":" 		{return new Symbol(sym.DSPUNTOS,yyline,yychar, yytext());}
"," 		{return new Symbol(sym.COMA,yyline,yychar, yytext());}
"(" 		{return new Symbol(sym.PARIZQ,yyline,yychar, yytext());}
")" 		{return new Symbol(sym.PARDER,yyline,yychar, yytext());}
"-" 		{return new Symbol(sym.MENOS,yyline,yychar, yytext());}
"*" 		{return new Symbol(sym.POR,yyline,yychar, yytext());}
"/" 		{return new Symbol(sym.DIVIDIDO,yyline,yychar, yytext());}
"%" 		{return new Symbol(sym.MODULO,yyline,yychar, yytext());}
"^"	    	{return new Symbol(sym.POTENCIA,yyline,yychar, yytext());}
"TRUE"		{return new Symbol(sym.TRUE,yyline,yychar, yytext());}
"<>" 		{return new Symbol(sym.DIFERENTE,yyline,yychar, yytext());}
"FALSE"		{return new Symbol(sym.FALSE,yyline,yychar, yytext());}
">"			{return new Symbol(sym.MAYOR,yyline,yychar, yytext());}
"<" 		{return new Symbol(sym.MENOR,yyline,yychar, yytext());}
">=" 		{return new Symbol(sym.MAYORIGUAL,yyline,yychar, yytext());}
"<=" 		{return new Symbol(sym.MENORIGUAL,yyline,yychar, yytext());}
"="	    	{return new Symbol(sym.IGUAL,yyline,yychar, yytext());}
"AND"		{return new Symbol(sym.AND,yyline,yychar, yytext());}
"OR"		{return new Symbol(sym.OR,yyline,yychar, yytext());}
"NAND"		{return new Symbol(sym.NAND,yyline,yychar, yytext());}
"NOR"		{return new Symbol(sym.NOR,yyline,yychar, yytext());}
"NOT"		{return new Symbol(sym.NOT,yyline,yychar, yytext());}
"NOT"		{return new Symbol(sym.NOT,yyline,yychar, yytext());}
"CONST"		{return new Symbol(sym.CONST,yyline,yychar, yytext());}
"PROGRAM"	{return new Symbol(sym.PROGRAM,yyline,yychar, yytext());}
"INTEGER"	{return new Symbol(sym.INTEGER,yyline,yychar, yytext());}


<YYINITIAL> {ID} {return new Symbol(sym.ID,yyline,yychar, yytext());}
<YYINITIAL> {CHAR} {return new Symbol(sym.CHAR,yyline,yychar, yytext());}
<YYINITIAL> {CADENA} {return new Symbol(sym.CADENA,yyline,yychar, yytext());}
{BLANCO} 	{}
{D} 		{return new Symbol(sym.ENTERO,yyline,yychar, yytext());}
{DD} 		{return new Symbol(sym.DECIMAL,yyline,yychar, yytext());}

. {
    System.err.println("Este es un error lexico: "+yytext()+", en la linea: "+yyline+", en la columna: "+yychar);
}
