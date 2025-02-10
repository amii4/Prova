public class Matricola {
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        boolean even = false; //controllo se numero di matricola è pari

        while (state >=0 && i< s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                case 0:
                    if(Character.isDigit(ch)){
                        even = (ch - '0') % 2 == 0; // calcolo se la cifra è pari (true), converto prima il carattere numerico ch in un numero intero (ch - '0') e poi calcolo il modulo 2 ( numero è pari se il resto della divisione per 2 è 0 ) per capire se è pari o dispari 
                        state = 1; // se priimo carattere è un numero, state = 1
                    } else {
                        state = -1; //se primo carattere non è un numero, è un carattere non valido
                    }
                    break;

                case 1:
                    if(Character.isDigit(ch)){ // se il secondo carattere è un numero, controllo la parità e non aumento state: state sarà ancora 1 e si rientrerà in questo ramo dello switch
                        even = (ch - '0') % 2 == 0; // controllo la parità
                    } else if (Character.isLetter(ch)) { //se il secondo carattere è una lettera controllo, e se appartiene al T2 e T3 allora state = 2 , senno state = -1 quindi non appartiene
                        if(( even && ch >= 'A' && ch <= 'K') || ( !even && ch >= 'L' && ch <= 'Z' ) ){ // il mio carattere deve appartenere o al T2 --> matricola pari e iniziale cognome compresa tra A e K, o T3 --> matricola dispari e iniziale cognome compresa tra L e Z
                            state = 2; // aumento state perche la prima lettera appartiene a T2 o T3
                        } else {
                            state = -1; // sennò non aumento state perche il cognome non appartiene al turno
                        }
                    } else {
                        state = -1;
                    }
                    break;

                case 2:
                    if(Character.isLetter(ch)){ // l'iniziale del cognome è valida e appartiene al T2 o T3, ora in caso ci siano altre lettere le leggo e rimango nello stato 2, condizione necessaria poichè se alla fine del cognome c'è un numero è sbagliato 
                    } else {
                        state = -1; // se cifra o carattere diverso da una lettera --> sbagliato
                    }
                    break;
            }

        }

        return state == 2;
    

    }

    public static void main (String[] args ){
        //ystem.out.println(scan(args[0]) ? "OK" : "NOPE");
        String[] testInputs = { "123456Bianchi", "654321Rossi", "2Bianchi", "122B", "\n 654321Bianchi",
        "123456Rossi","654322", "Rossi"};

    for (String input : testInputs) {
        System.out.println("Input: " + input + " -> " + (scan(input) ? "OK" : "NOPE"));
    }
    }
}