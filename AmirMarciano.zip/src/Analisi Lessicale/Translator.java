import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        // come in Esercizio 3.1
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        // come in Esercizio 3.1
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        // come in Esercizio 3.1
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move(); // se non è EOF passa a controllare il prossimo token
        } else
            error("syntax error");
    }

    public void prog() {
        // ... completare ...
        int lnext_prog = code.newLabel();
        statlist(lnext_prog);
        code.emitLabel(lnext_prog);
        match(Tag.EOF);
        try {
            code.toJasmin(); //
        } catch (java.io.IOException e) {
            System.out.println("IO error\n");
        }
        ;
        // ... completare ...
    }

    private void statlist(int lnext) {
        switch (look.tag) {
            case Tag.ASSIGN, Tag.PRINT, Tag.READ, Tag.FOR, Tag.IF, '{':
                stat(lnext);
                statlistp(lnext);
                break;
            default:
                error("Error in Statlist");
        }
    }

    private void statlistp(int lnext) {
        switch (look.tag) {
            case ';':
                match(';');
                stat(lnext);
                statlistp(lnext);
                break;
            case -1:
            case '}':
                break;
            default:
                error("Error in statlistp");
        }
    }

    // public void stat(int lnext) {
    // switch(look.tag) {
    // // ... completare ...
    // case Tag.READ:
    // match(Tag.READ);
    // match('(');
    // idlist(/* completare */);
    // match(')');
    // // ... completare ...
    // }
    // }

    private void stat(int lnext) {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                assignlist(lnext);
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist(lnext);
                match(')');
                break;

            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist(lnext);
                match(')');
                break;

            case Tag.FOR:
                match(Tag.FOR);
                match('(');
                if (look.tag == Tag.ID) {
                    // int id_addr = st.lookupAddress(((Word) look).lexeme);
                    // if (id_addr == -1) {
                    // id_addr = count;
                    // st.insert(((Word) look).lexeme, count++);
                    // }

                    match(Tag.ID);
                    match(Tag.INIT);
                    expr(lnext);
                    // code.emit(OpCode.istore, id_addr);
                    match(';');
                }

                // int for_label = code.newLabel();
                // int exit_label = code.newLabel();
                // code.emitLabel(for_label);
                // bexpr(exit_label);
                bexpr(lnext);
                match(')');
                match(Tag.DO);
                stat(lnext);
                // code.emit(OpCode.GOto, for_label);
                // code.emitLabel(exit_label);
                break;

            case Tag.IF:
                match(Tag.IF);
                match('(');
                bexpr(lnext);
                match(')');
                stat(lnext);
                if (look.tag == Tag.ELSE) {
                    match(Tag.ELSE);
                    stat(lnext);
                }
                match(Tag.END);
                break;

            case '{':
                match('{');
                statlist(lnext);
                match('}');
                break;

            default:
                error("Unknown statment");
        }
    }

    private void assignlist(int lnext) {
        switch (look.tag) {
            case '[':
                match('[');
                expr(lnext);
                match(Tag.TO);
                idlist(lnext);
                match(']');
                assignlistp(lnext);
                break;

            default:
                error("error in assignlist");

        }
    }

    // private void assignlistp(int lnext) {
    // switch (look.tag) {
    // case '[':
    // match('[');
    // expr(lnext);
    // match(Tag.TO);
    // idlist(lnext);
    // match(']');
    // assignlistp(lnext);
    // break;

    // case ')', ']', '}', ';', Tag.EOF:
    // break;

    // default:
    // error("Error in assignlistp");
    // }
    // }
    private void assignlistp(int lnext) {
        switch (look.tag) {
            case '[':
                match('[');
                expr(lnext);
                match(Tag.TO);
                idlist(lnext);
                match(']');
                assignlistp(lnext);
                break;

            case ')', ']', '}', ';', Tag.EOF, Tag.ELSE, Tag.END:
                // Caso in cui l'assegnazione è terminata
                break;

            default:
                error("Error in assignlistp: unexpected token " + look);
        }
    }

    private void idlist(int lnext) {
        switch (look.tag) {
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                idlistp(lnext);
                break;
            // ... completare ...
            default:
                error("Error in idlist");

        }
    }

    private void idlistp(int lnext) {
        switch (look.tag) {
            case ',':
                match(',');
                match(Tag.ID);
                idlistp(lnext);
                break;

            case Tag.EOF, ';', ')', Tag.ELSE, Tag.END, '}', ']':
                break;

            default:
                error("Error in idlistp");
        }

    }

    public void bexpr(int lnext) {
        if (look.tag == Tag.RELOP) {
            match(Tag.RELOP);
            expr(lnext);
            expr(lnext);
        } else {
            error("Error in bexpr");
        }
    }

    private void expr(int lnext) {
        switch (look.tag) {
            // ... completare ...
            case '+':
                match('+');
                match('(');
                exprlist(lnext);
                match(')');
                code.emit(OpCode.iadd);
                break;

            case '-':
                match('-');
                expr(lnext);
                expr(lnext);
                code.emit(OpCode.isub);
                break;

            case '*':
                match('*');
                match('(');
                exprlist(lnext);
                match(')');
                code.emit(OpCode.imul);
                break;

            case '/':
                match('/');
                expr(lnext);
                expr(lnext);
                code.emit(OpCode.idiv);
                break;

            case Tag.NUM:
                code.emit(OpCode.ldc, ((NumberTok) look).value);
                match(Tag.NUM);
                break;

            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                // code.emit(OpCode.iload, id_addr);
                match(Tag.ID);
                break;

            default:
                error("Error in expr");

                // ... completare ...
        }
    }

    // ... completare ...

    public void exprlist(int lnext) {
        switch (look.tag) {
            case '+', '-', '*', '/', Tag.NUM, Tag.ID:
                expr(lnext);
                exprlistp(lnext);
                break;

            default:
                error("Error in exprlist");
        }
    }

    public void exprlistp(int lnext) {
        switch (look.tag) {
            case ',':
                match(',');
                expr(lnext);
                exprlistp(lnext);
                break;

            case ')':
                break;

            default:
                error("Error in exprlistp");
        }

    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "test/testtranslator.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
