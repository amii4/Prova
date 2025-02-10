public class Commenti {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state){
                case 0: // stato iniziale
                    if (ch == '/'){
                        state = 1; // se primo carattere è / --> state = 1
                    } else {
                        state =-1;
                    }
                    break;

                case 1: //secondo carattere deve essere *
                    if ( ch == '*'){
                        state = 2; // secondo carattere * --> state = 2
                    } else {
                        state =-1;
                    }
                    break;

                case 2: // il commento è stato aperto
                    if ( ch == '*'){
                        state = 3; // se dopo l'apertura del commento c'è un asterisco puo essere che sia l'inizio della chiusura
                    } else if ( ch == 'a' || ch == '/'){
                    // se invece ho un a o / passo al controllo del carattere successivo 
                    }  else {
                        state =-1;
                    }
                    break;
                
                case 3: // ho un asterisco, potrebbe essere la fine del commento
                    if( ch == '/'){
                        state = 4; // è chiusura del commento
                    } else if (ch == '*'){
                        // se dopo un asterisco ho un altro asterisco rimango in questo stato per controllare il prossimo carattere
                    } else if (ch == 'a'){
                        state = 2;
                    } else {
                        state =-1;
                    }
                    break;

                case 4: // fine del commento
                    state = -1; // se ho altri caratteri dopo la chiusura del commento 
                    break;
                    
            }

        }

        
        return state == 4;
            
    }

    public static void main (String[] args ){
        //System.out.println(scan(args[0]) ? "OK" : "NOPE");
        String[] testInputs = { "/****/", "/*a*a*/", "/*a/**/", "/**a///a/a**/", "/**/",
            "/*/*/","/*/", "/**/***/" };

        for (String input : testInputs) {
            System.out.println("Input: " + input + " -> " + (scan(input) ? "OK" : "NOPE"));
        }
    }
}
