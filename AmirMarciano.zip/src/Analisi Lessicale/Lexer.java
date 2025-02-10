import java.io.*; 


public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';  // "consumo" il carattere , assegno ' ' a peek così può leggere il prossimo carattere
                return Token.not;

// ... gestire i casi di ( ) [ ] { } + - * / ; , ... //

            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;

            case '[':
                peek = ' ';
                return Token.lpq;

            case ']':
                peek = ' ';
                return Token.rpq;

            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            // case '/':
            //      peek = ' ';
            //      return Token.div;

            case '/':
                readch(br); //leggo il carattere successivo
                if (peek=='*'){ // se il carattere successivo è *, è l'inizio di un commento
                    readch(br); 
                    while (true){ //continuo a leggere finche non trovo la chiusura del commento
                        if (peek == (char)-1){ // se l'input non contiene più niente
                            System.err.println("Error: Unclosed multiline comment "); //segnalo un errore
                            return null; // termino la scansione 
                        }
                    
                        if (peek== '*'){ // se il carattere successivo è un * , potrebbe essere la fine del commento
                            readch(br); 
                            if (peek== '/'){ // se il carattere è / è la fine del commento
                                readch(br); 
                                break; // esco dal ciclo

                            }   
                        }else{
                            if (peek == '\n') line++; // senno incremento line per ogni ritorno a capo ( un commento con /* può essere si piu linee)
                            readch(br); 
                        }
                    }
                return lexical_scan(br); // continuo la scansione dopo il commento

                }else if (peek == '/'){ // se invece dopo il / abbiamo un altro / siamo in un commento su riga
                    while (peek != '\n' && peek != (char)-1){ // continua a leggere fino alla fine della riga o finchè non trova più caratteri nel file
                        readch(br); 
                    }
                    return lexical_scan(br); // continuo la scansione dopo il commento

                }else {
                    return Token.div; // se dopo il / non c'è ne / ne * allora lo gestisco come il simbolo di divisione
                }

            case ';':
                peek = ' ';
                return Token.semicolon;

            case ',':
                peek = ' ';
                return Token.comma;

        
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
	// ... gestire i casi di || < > <= >= == <> ... //
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : "  + peek );
                    return null;
                }

            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    return Word.lt;
                }

            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    return Word.gt;
                }

            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    peek =' ';
                    return Token.assign;
                }

            case ':':
                readch(br);
                if (peek == '='){
                    peek = ' ';
                    return Word.init;
                }else {
                    System.err.println("Erroneous character"
                    + " after ':' : "  + peek );
            return null;
                }

          
            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek) || peek == '_') { //con la nuova definizione di identificatore un identificatore può contenere il simbolo di underscore

	// ... gestire il caso degli identificatori e delle parole chiave //

                 String s = ""; //inizializzo una stringa vuota
                 do {
                    s += peek; //aggiungo il carattere attuale (peek) a s
                    readch(br); // leggo il prossimo carattere
                 } while (Character.isLetterOrDigit(peek)|| peek == '_'); //continuo finche il carattere è una lettera o un numero o un _

                if(s.matches("_+")){ // controllo che la stringa non sia composta solo da _ o più
                    System.err.println("Erroneous identifier composed onlu of _: " +s);
                    return null;
                }

                 //controllo se la stringa corrisponde a una parola chiave
                switch (s){
                    case "assign":
                        return Word.assign;
                    case "to":
                        return Word.to;
                    case "if":
                        return Word.iftok;
                    case "else":
                        return Word.elsetok;
                    case "do":
                        return Word.dotok;
                    case "for":
                        return Word.fortok;
                    case "begin":
                        return Word.begin;
                    case "end":
                        return Word.end;
                    case "print":
                        return Word.print;
                    case "read":
                        return Word.read;
                    default:
                    return new Word(Tag.ID, s); // se non è una parola chiave allora è un identificatore ( lettera seguita da lettere e cifre)
                }
                //  if(s.equals("to")) return Word.to;
                //  if(s.equals("if")) return Word.iftok;
                //  if(s.equals("else")) return Word.elsetok;
                //  if(s.equals("do")) return Word.dotok;
                //  if(s.equals("for")) return Word.fortok;
                //  if(s.equals("begin")) return Word.begin;
                //  if(s.equals("end")) return Word.end;
                //  if(s.equals("print")) return Word.print;
                //  if(s.equals("read")) return Word.read;
                //  if(s.equals("assign")) return Word.assign;

                //  return new Word(Tag.ID, s); // se non è una parola chiave allora è un identificatore ( lettera seguita da lettere e cifre)



                } else if (Character.isDigit(peek)) {

	// ... gestire il caso dei numeri ... //


                String num= "";
                do {
                    num += peek; //aggiungo il carattare attuale alla stringa
                    readch(br); // leggo il prossimo carattere

                    if(Character.isLetter(peek)){ // Controllo che un numero non sia seguito subito dopo da una lettera
                        System.err.println("Erroneous identifier starting with a number: " +num +peek);
                        return null;
                    }
   
                } while (Character.isDigit(peek)); // continuo finchè il carattere è una cifra
                
                
                int value = Integer.parseInt(num); // Converto la stringa in un numero intero
                return new NumberTok(Tag.NUM, value);// ritorno il token numerico <256, v>
                } else {
                        System.err.println("Erroneous character: " 
                                + peek );
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
     
        Lexer lex = new Lexer();
       String path = "test/testparser.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}



