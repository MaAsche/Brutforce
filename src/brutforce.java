import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class brutforce {
    private static volatile boolean foundPw;
    private static ExecutorService executor = null;


    public static void main(String[] args) {
        String hashedPW = gethashPW(args[0]);
        System.out.println(hashedPW);
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B',
                'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                'W', 'X', 'Y', 'Z'};

        hackThisShit(hashedPW, chars);
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
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(pw.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return e.toString();
        }

    }

    private static boolean checkPW(String pw1, String pw2) {
        if (pw1.equals(gethashPW(pw2))) {
            System.out.println("Password found: " + pw2);
            return true;
        }
        return false;
    }


    private static void hackThisShit(String hashedPW, char[] chars) {
        executor = Executors.newFixedThreadPool(4);
        double startTime = System.nanoTime();

        executor.execute(new hackerman(hashedPW, chars, 1, 3));
        executor.execute(new hackerman(hashedPW, chars, 4, 5));
        executor.execute(new hackerman(hashedPW, chars, 5, 6));
        executor.execute(new hackerman(hashedPW, chars, 6, 7));
        while (!foundPw) {
        }


        executor.shutdownNow();
        System.out.printf("Time: %f s\n", (System.nanoTime() - startTime) * Math.pow(10, -9));
        System.exit(0);


    }


}
