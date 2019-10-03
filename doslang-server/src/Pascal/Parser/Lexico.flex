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

<YYINITIAL> "SALUDO" {return new Symbol(sym.SALUDO,yyline,yychar, yytext());}



"+" {return new Symbol(sym.MAS,yyline,yychar, yytext());}
"-" {return new Symbol(sym.MENOS,yyline,yychar, yytext());}
"*" {return new Symbol(sym.POR,yyline,yychar, yytext());}
"/" {return new Symbol(sym.DIVIDIDO,yyline,yychar, yytext());}


<YYINITIAL> {ID} {return new Symbol(sym.ID,yyline,yychar, yytext());}
<YYINITIAL> {CHAR} {return new Symbol(sym.CHAR,yyline,yychar, yytext());}
<YYINITIAL> {CADENA} {return new Symbol(sym.CADENA,yyline,yychar, yytext());}
{BLANCO} {}
{D} {return new Symbol(sym.ENTERO,yyline,yychar, yytext());}
{DD} {return new Symbol(sym.DECIMAL,yyline,yychar, yytext());}

. {
    System.err.println("Este es un error lexico: "+yytext()+", en la linea: "+yyline+", en la columna: "+yychar);
}
