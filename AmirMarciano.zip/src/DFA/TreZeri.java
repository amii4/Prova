public class TreZeri {
    public static boolean scan(String s) {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch == '0')
                        state = 1;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;

                case 1:
                    if (ch == '0')
                        state = 2;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;

                case 2:
                    if (ch == '0')
                        state = 3;
                    else if (ch == '1')
                        state = 0;
                    else
                        state = -1;
                    break;

                case 3:
                    if (ch == '0' || ch == '1')
                        state = 3;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 3; //se state = 3 vuol dire che ci sono 3 zeri consecutivi
    }

    public static void main(String[] args) {
       // System.out.println(scan(args[0]) ? "OK" : "NOPE");
       String[] testInputs = { "1100011001", "1000011001","010101","10214", "1001001011"};

   for (String input : testInputs) {
       System.out.println("Input: " + input + " -> " + (scan(input) ? "OK" : "NOPE"));
   }
    }
}