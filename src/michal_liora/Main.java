package michal_liora;

import java.util.Scanner;

public class Main {
    /*
    Michal Lev 322812421
    Liora Grinshpul 211666557
    */
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

    public static void main(String[] args){
        String collegeName = getNameFromUser(College.class.getSimpleName());
        College college = new College(collegeName);
        boolean showMenu = true;
        do {
            System.out.println("0 - exit\n" +
                    "1 - Add lecturer to college\n" +
                    "2 - Add committee to college\n" +
                    "3 - Add committee member\n" +
                    "4 - Update committee Chair\n" +
                    "5 - Remove committee member\n" +
                    "6 - Add new department\n" +
                    "7 - Show average salary of all lecturers\n" +
                    "8 - Show average salary of lecturers in department\n" +
                    "9 - Show full details of all lecturers\n" +
                    "10 - Show full details of all committees\n" +
                    "11 - Add lecturer to department\n" +
                    "12 - Compare Professors/Doctors (by number of articles)\n" +
                    "13 - Compare committees\n" +
                    "14 - Create a committee clone");

            System.out.print("Enter your choice: ");
            int choice = scan.nextInt();
            scan.nextLine();
            try {
                switch (choice) {
                    case 0:
                        showMenu = false;
                        break;
                    case 1:
                        college.createNewLecturer();
                        break;
                    case 2:
                        college.createNewCommittee();
                        break;
                    case 3:
                        college.addLecturerToCommittee();
                        break;
                    case 4:
                        college.changeCommitteeHead();
                        break;
                    case 5:
                        college.removeMemberFromCommittee();
                        break;
                    case 6:
                        college.createNewDepartment();
                        break;
                    case 7:
                        college.getLecturersSalaryAvg();
                        break;
                    case 8:
                        college.getDepartmentMembersSalaryAvg();
                        break;
                    case 9:
                        college.getDetailsOfAllLecturers();
                        break;
                    case 10:
                        college.getDetailsOfAllCommittees();
                        break;
                    case 11:
                        college.addLecturerToDepartment();
                        break;
                    case 12:
                        college.compareDoctorsAndProfessors();
                        break;
                    case 13:
                        college.compareCommittees();
                        break;
                    case 14:
                        college.createCommitteeClone();
                        break;
                    default:
                        throw new InvalidUserInputException(Enums.errorMessage.INVALID_CHOICE.getMessage());
                }
            }
            catch (CollegeException e){
                System.out.println("Error : " + e.getMessage());
            }
        }while (showMenu);

        scan.close();
    }
}
