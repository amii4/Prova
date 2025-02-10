public class NumeriVirgolaMobile {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state){
                case 0: //stato iniziale --> controllo del primo carattere
                    if( ch == '+' || ch == '-'){
                        state = 1; // se è un segno 
                    } else if( Character.isDigit(ch)) {
                        state = 2; // se è un numero
                    } else if( ch == '.'){
                        state = 3; // se è il punto
                    } else {
                        state = -1;
                    }
                    break;

                case 1: // controllo caratteri dopo il segno
                    if (Character.isDigit(ch)){
                        state = 2; //se dopo il segno ho un numero
                    } else if( ch == '.'){
                        state = 3; // se dopo il segno ho il punto
                    } else {
                        state = -1;
                    }
                    break;

                case 2: // controllo caratteri dopo numeri ( è uno stato finale )
                    if (Character.isDigit(ch)){
                        //se ho altri numeri rimango in questo stato
                    } else if (ch == '.'){
                        state = 3; // se ho il punto
                    } else if (ch == 'e'){
                        state = 5; // se ho il simbolo esponenziale e non c'è il punto 
                    } else {
                        state = -1;
                    }
                    break;

                case 3: // controllo caratteri dopo il punto
                    if (Character.isDigit(ch)){
                        state = 4; // posso avere solo numeri subito dopo il punto
                    } else {
                        state = -1;
                    }
                    break;

                case 4: // n dopo il punto ( è uno stato finale)
                    if (Character.isDigit(ch)) {
                        // rimango in questo stato se ho altri numeri
                    } else if ( ch== 'e'){
                        state = 6; // se ho simbolo esponenziale nella parte a destra del punto 
                    } else {
                        state = -1;
                    }
                    break;

                case 5: // simbolo esponenziale a sx di un ipotetico punto
                    if ( ch == '+' || ch == '-'){
                        state = 7; // se ho un segno
                    } else if ( Character.isDigit(ch)) {
                        state = 8; // se ho un numero
                    } else {
                        state = -1;
                    }
                    break;

                case 6: // simbolo esponenziale a dx del punto
                    if ( ch == '+' || ch == '-'){
                        state = 9; // se ho un segno
                    } else if ( Character.isDigit(ch)) {
                        state = 10; // se ho un numero
                    } else {
                        state = -1;
                    }
                    break;

                case 7: // segno dopo simbolo e a sx di un ipotico punto 
                    if (Character.isDigit(ch)) {
                        state = 8; // devo avere un numero
                    } else {
                        state = -1;
                    }
                    break;

                case 8: // numero dopo e o dopo segno dopo e a sx nel punto (stato finale)
                    if (Character.isDigit(ch)) {
                        //rimango in questo stato
                    } else if ( ch == '.') {
                        state = 10; // se ho il punto
                    } else {
                        state = -1;
                    }
                    break;

                case 9: // segno dopo simbolo e a dx del punto
                    if  (Character.isDigit(ch)){
                        state = 10; // se ho un numero
                    }else {
                        state = -1;
                    }
                    break;

                case 10: // numero dopo e o dopo segno di e a dx del punto
                if  (Character.isDigit(ch)){
                    //se ho un numero rimango
                }else {
                    state = -1;
                }
                break;

            }
        }

        //accetto solo se termino negli stati validi 
        return state == 2 || state == 4 || state == 8 || state == 10;
            
    }

    public static void main (String[] args ){
        //System.out.println(scan(args[0]) ? "OK" : "NOPE");
        String[] testInputs = {"123", "123.5", ".567", "+7.5", "-.7", "67e10", "1e-2", "-.7e2", "1e2.3","999999999",".", "e3", "123.", "+e6", "1.2.3" , "4e5e6", "++3"};

        for (String input : testInputs) {
            System.out.println("Input: " + input + " -> " + (scan(input) ? "OK" : "NOPE"));
        }
    }
}
