import java.io.*;
public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {  //costruttore
        lex = l;
        pbr = br;
        move();
    }

    void move() { //metodo per leggere il token chiamando lexical scan e aggiornando look
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) { //metodo che controlla se il token attuale corrisponde al token atteso
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move(); // se non è EOF passa a controllare il prossimo token
        } else error("syntax error");
    }

    public void start() {
// ... completare ...

        if (look.tag == '(' || look.tag == Tag.NUM){
            expr();
            match(Tag.EOF);
// ... completare ...
        } else {
            error("Error in Start, Expected '(' or NUM, found: " +look);
        }
    }

    private void expr() {
// ... completare ...
        if (look.tag == Tag.NUM || look.tag == '('){
            term();
            exprp();
        } else {
            error("Error in Expr, Expected '(' or NUM, found: " +look);
        }

    }

    private void exprp() {
        switch (look.tag) {
        case '+':
            match('+');
            term();
            exprp();
            break;

        case '-':
            match('-');
            term();
            exprp();
            break;

        case ')':
            break;
        
        case Tag.EOF:
            break;

        default:
            error("Error in Exprp, unexpected token: " +look );
// ... completare ...
        }
    }

    private void term() {
    // ... completare ...
        if(look.tag == Tag.NUM || look.tag == '('){
            fact();
            termp();
        }else {
            error("Error in Term, Expected '(' or NUM, found: " +look);
        }
    }

    private void termp() {
    // ... completare ...
        switch(look.tag){
            case '*':
                match('*');
                fact();
                termp();
                break;

            case '/':
                match('/');
                fact();
                termp();
                break;

            case'+':
            case '-':
            case ')':
            case Tag.EOF:
                break;
            
            default:
                error("Error in Termp, unexpected token: " + look);
        }
    }

    private void fact() {     
    // ... completare ...

        if(look.tag == Tag.NUM){ 
            match(Tag.NUM);

        } else if (look.tag == '('){

            match('(');
            expr();

            if (look.tag == ')'){
                    match(')');
            } else {
                error("Error in Fact, Expected ')' found: " +look);
            }

        } else {
            error("Error in Fact,, Expected '(' or NUM, found: " +look);
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "test/testparser.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}