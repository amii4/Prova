import java.io.*;
public class Parser2 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser2(Lexer l, BufferedReader br) {  //costruttore
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

    public void prog(){
        statlist();
        match(Tag.EOF);
    }

    private void statlist(){
        switch(look.tag){
            case Tag.ASSIGN, Tag.PRINT, Tag.READ, Tag.FOR, Tag.IF, '{' : 
                stat();
                statlistp();
                break;
            default: 
                error("Error in Statlist");
        }
    }

    private void statlistp(){
        switch(look.tag){
            case ';':
                match(';');
                stat();
                statlistp();
                break;

            case Tag.EOF:
            case '}':
                break;

            default:
                error("Error in statlistp");
        }
    }

    private void stat(){
        switch( look.tag){
            case Tag.ASSIGN:
               match(Tag.ASSIGN);
               assignlist();
               break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist();
                match(')');
                break;

            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist();
                match(')');
                break;

            case Tag.FOR:
                match(Tag.FOR);
                match('(');
                if (look.tag == Tag.ID){
                    match(Tag.ID);
                    match(Tag.INIT);
                    expr();
                    match(';');  
                }
                bexpr();
                match(')');
                match(Tag.DO);
                stat();
                break;

            case Tag.IF:
                match(Tag.IF);
                match('(');
                bexpr();
                match(')');
                stat();
                if(look.tag == Tag.ELSE){
                    match(Tag.ELSE);
                    stat();
                }
                match(Tag.END);
                break;

            case '{':
                match('{');
                statlist();
                match('}');
                break;
 
            default:
                error("Unknown statment");
        }
    }
    
    private void assignlist(){
        switch(look.tag){
            case '[':
                match('[');
                expr();
                match(Tag.TO);
                idlist();
                match(']');
                assignlistp();
                break;
            
            default:
                error("error in assignlist");

        }
    }

    private void assignlistp(){
        switch(look.tag){
            case '[':
                match('[');
                expr();
                match(Tag.TO);
                idlist();
                match(']');
                assignlistp();
                break;
            
            case ')',']','}',';',Tag.EOF:
                break;

            default:
                error("Error in assignlistp");
        }
    }

    private void idlist(){
        if (look.tag == Tag.ID){
            match(Tag.ID);
            idlistp();
        }else {
            error("Error in idlist");
        }
    }

    private void idlistp(){
        switch(look.tag ) {
            case ',':
                match(',');
                match(Tag.ID);
                idlistp();
                break;
            
            case Tag.EOF, ';', ')', Tag.ELSE, Tag.END, '}', ']':
                break;
            
            default:
                error("Error in idlistp");
        }

    }

    public void bexpr(){
        if(look.tag== Tag.RELOP){
            match(Tag.RELOP);
            expr();
            expr();
        }else{
            error("Error in bexpr");
        }
    }

    public void expr(){
        switch(look.tag){
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;

            case'-':
                match('-');
                expr();
                expr();
                break;

            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                break;
            
            case '/':
                match('/');
                expr();
                expr();
                break;
            
            case Tag.NUM:
                match(Tag.NUM);
                break;
            
            case Tag.ID:
                match(Tag.ID);
                break;

            default:
                error("Error in expr");
        }
    }

    public void exprlist(){
        switch(look.tag){
            case '+', '-', '*', '/', Tag.NUM, Tag.ID:
                expr();
                exprlistp();
                break;

            default:
            error("Error in exprlist");
        }
    }

    public void exprlistp(){
        switch(look.tag){
            case ',':
                match(',');
                expr();
                exprlistp();
                break;
            
            case ')':
                break;

            default:
                error("Error in exprlistp");
        }

    }



    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "test/testparser2.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser2 parser = new Parser2(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
