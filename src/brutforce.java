import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;


/**
 * about 3,2 million combinations per second
 * First password: X42a0 - 48sec
 * Second password: :( - stopped after 6h -> worst case: 433.665 weeks
 * Third password: not even tried -> worst case 440775,0351 weeks -> ≈ 765 years
 *
 */

public class brutforce {
    private static volatile boolean foundPw;
    public static AtomicLong counter = new AtomicLong(0);



    public static void main(String[] args) {

        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z'};

        char[] chars2 = {'Z' ,'Y' ,'X' ,'W' ,'V' ,'U' ,'T' ,'S' ,'R' ,'Q' ,'P' ,'O' ,'N' ,'M' ,
                'L' ,'K' ,'J' ,'I' ,'H' ,'G' ,'F' ,'E' ,'D' ,'C' ,'B' ,'A' ,'9' ,'8' ,'7' ,'6' ,'5'
                ,'4' ,'3' ,'2' ,'1' ,'0' ,'z' ,'y' ,'x' ,'w' ,'v' ,'u' ,'t' ,'s' ,'r' ,'q' ,'p' ,'o'
                ,'n' ,'m' ,'l' ,'k' ,'j' ,'i' ,'h' ,'g' ,'f' ,'e' ,'d' ,'c' ,'b' ,'a'};

        char[] chars3 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
                'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z'};

        char[] chars4 = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

        char[] chars5 = {'A', 'B',
                'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', };

        char[] chars6 = {'A', 'B',
                'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z','0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };



        nsaStuff(args[0], chars, chars2, chars3, chars4, chars5, chars6);

    }

    private static class hackerman implements Runnable {

        String pw;
        char[] chars;
        int min;
        int max;

        private hackerman(String pw, char[] chars, int min, int max) {
            this.pw = pw;
            this.chars = chars;
            this.min = min;
            this.max = max;
        }

        @Override
        public void run() {
            for (int i = min; i <= max; i++) {
                generate("", i, chars, pw);
            }

        }

        private static void generate(String current, int len, char[] chars, String hashedPw) {

            if (foundPw) {
                return;
            }
            if (current.length() == len) {
                if (checkPW(hashedPw, current)) {
                    foundPw = true;

                    return;
                }
            }
            if (current.length() < len) {
                for (char k : chars) {
                    generate(current + k, len, chars, hashedPw);
                }
            }
        }

    }

    private static String gethashPW(String pw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = md.digest(pw.getBytes());
            BigInteger no = new BigInteger(1, bytes);
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;

        } catch (NoSuchAlgorithmException e) {
            return e.toString();
        }

    }

    private static boolean checkPW(String pw1, String pw2) {
        if (pw1.equals(gethashPW(pw2))) {
            System.out.println("Password found: " + pw2);
            return true;
        }
        counter.incrementAndGet();
        return false;
    }


    private static void nsaStuff(String hashedPW, char[] chars, char[] chars2, char[] chars3, char[] chars4, char[] chars5, char[] chars6) {
        ExecutorService executor = Executors.newFixedThreadPool(6);
        double startTime = System.nanoTime();

        executor.execute(new hackerman(hashedPW, chars, 5, 5));
        executor.execute(new hackerman(hashedPW, chars2, 5, 5));
        executor.execute(new hackerman(hashedPW, chars3, 5, 5));
        executor.execute(new hackerman(hashedPW, chars4, 5, 5));
        executor.execute(new hackerman(hashedPW, chars5, 5, 5));
        executor.execute(new hackerman(hashedPW, chars6, 5, 5));
        while (!foundPw) {
        }


        executor.shutdownNow();
        System.out.printf("Time: %f s\nTried %d passwords", (System.nanoTime() - startTime) * Math.pow(10, -9), counter.get());
        System.exit(0);


    }


}
