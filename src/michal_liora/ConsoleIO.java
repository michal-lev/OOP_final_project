package michal_liora;

import java.util.Scanner;

public class ConsoleIO {
    private static final Scanner scan = new Scanner(System.in);
    public static String getNameFromUser(String title){
        return getStringFromUser("Enter " + title.toLowerCase() + " name: ");
    }

    public static String getStringFromUser(String message) {
        System.out.print(message);
        return scan.nextLine();
    }

    public static double getDoubleFromUser(String message) {
        System.out.print(message);
        double toReturn = scan.nextDouble();
        scan.nextLine();
        return toReturn;
    }

    public static int getIntFromUser(String message) {
        System.out.print(message);
        int toReturn = scan.nextInt();
        scan.nextLine();
        return toReturn;
    }

    public static void printMessage(String message){
        System.out.println(message);
    }

}
