import java.io.*;

class ReadlineFunction {
    public static void main(String args[])
            throws IOException {
        // create a BufferedReader using System.in
        BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));
        String str;

        System.out.println("Enter lines of text.");
        System.out.println("Enter 'stop' to quit.");
        do {

            str = obj.readLine();
            System.err.println("Your input: " + str);
        } while (!str.equals("exit"));
        System.out.println("End of Program");
    }
}