public class Identificatori {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        boolean notOnlyUnderscore = false; // controllo se c'è almeno un carattere che non è un underscore 

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch(state){
                case 0:
                if (Character.isLetter(ch) || ch == '_'){
                    state = 1;
                    if (Character.isLetter(ch)){
                        notOnlyUnderscore = true; // se il carattere è una lettera --> la mia stringa non è formata solo da underscore --> quindi true
                    }
                    } else if ( Character.isDigit(ch)){
                        state = -1; // la mia stringa non può iniziare con un numero
                    } else {
                        state = -1; // se è un carattere non valido --> carattere che non è una lettere/numero/underscore
                    }
                    break;

                case 1:
                if (Character.isLetterOrDigit(ch) ){
                    state = 1;
                    notOnlyUnderscore = true; // se il carattere è una lettera o un numero -->  la mia stringa non è formata solo da underscore --> quindi true
                    } else if ( ch == '_'){
                        state = 1; // underscore è sempre un carattere accettato ma non viene modificato il booleano noUnderscore, poichè se sono presenti tutti underscore rimane false
                    } else {
                        state = -1; // se è un carattere non valido --> carattere che non è una lettere/numero/underscore
                    }
                    break;

                }
            }
            return state ==1 && notOnlyUnderscore; // Stringa accettata se lo stato è 1, notOnlyUnderscore è true (non sono presenti solo underscore) e se la stringa contiene caratteri validi
        }

        public static void main (String[] args ){
            //System.out.println(scan(args[0]) ? "OK" : "NOPE");
            String[] testInputs = { "x", "flag1", "x2y2", "x_1", "_temp",
            "x___","__5", "x_1_y_2", "5", "221B", "123", "9_to_5", "___"};

        for (String input : testInputs) {
            System.out.println("Input: " + input + " -> " + (scan(input) ? "OK" : "NOPE"));
        }
        }
    }
