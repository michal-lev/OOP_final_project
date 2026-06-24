package michal_liora;

import java.io.IOException;

public class Main {
    /*
    Michal Lev 322812421
    Liora Grinshpul 211666557
    */

    public static void main(String[] args) {
        College college = initializeCollege();
        CollegeUI ui = new CollegeUI(college);
        ui.startMenu();
    }

    private static College initializeCollege() {
        try {
            return BackupService.loadCollege();
        } catch (Exception e) {
            ConsoleIO.printMessage("No backup found. Creating new College.");
            String name = ConsoleIO.getNameFromUser(College.class.getSimpleName());
            return new College(name);
        }
    }
}
