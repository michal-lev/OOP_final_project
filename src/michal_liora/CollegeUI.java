package michal_liora;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class CollegeUI {
    private final College college;
    public CollegeUI(College college) {
        this.college = college;
    }

    public void startMenu() {
        boolean showMenu = true;

        do {
            ConsoleIO.printMessage("\n--- College Management System ---");
            ConsoleIO.printMessage("0 - Exit\n" +
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

            int choice = ConsoleIO.getIntFromUser("Enter your choice: ");
            try {
                switch (choice) {
                    case 0:
                        showMenu = false;
                        BackupService.saveCollege(college);
                        ConsoleIO.printMessage("All changes saved. Exiting system...");
                        break;

                    case 1:
                        addLecturer(college);
                        break;

                    case 2:
                        String name = ConsoleIO.getStringFromUser("Enter committee name: ");
                        String chairName = ConsoleIO.getStringFromUser("Enter chair name: ");
                        String memberType = ConsoleIO.getStringFromUser("Enter members degree level (bachelor/master/doctorate/professor): ");
                        college.createNewCommittee(name, chairName, memberType);
                        ConsoleIO.printMessage("Committee created successfully.");
                        break;

                    case 3:
                        String committeeName = ConsoleIO.getNameFromUser(Committee.class.getSimpleName());
                        String memberName = ConsoleIO.getNameFromUser(Lecturer.class.getSimpleName());

                        college.addLecturerToCommittee(committeeName, memberName);
                        ConsoleIO.printMessage("Member added to committee successfully.");
                        break;

                    case 4:
                        String commName = ConsoleIO.getStringFromUser("Enter committee name: ");
                        String chair = ConsoleIO.getStringFromUser("Enter new chair name: ");
                        college.changeCommitteeHead(commName, chair);
                        ConsoleIO.printMessage("Committee chair updated successfully.");
                        break;

                    case 5:
                        String nameComm = ConsoleIO.getStringFromUser("Enter committee name: ");
                        String lecturerName = ConsoleIO.getStringFromUser("Enter lecturer name to remove: ");
                        college.removeMemberFromCommittee(nameComm, lecturerName);
                        ConsoleIO.printMessage("Lecturer removed from committee successfully.");
                        break;

                    case 6:
                        String deptName = ConsoleIO.getStringFromUser("Enter department name: ");
                        int studentCount = ConsoleIO.getIntFromUser("Enter number of students in department: ");
                        college.createNewDepartment(deptName, studentCount);
                        ConsoleIO.printMessage("Department added successfully.");
                        break;

                    case 7:
                        double salaryAvg = college.getLecturersSalaryAvg();
                        ConsoleIO.printMessage("The average salary of all lecturers is: " + salaryAvg);
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
                ConsoleIO.printMessage("Error: " + e.getMessage());
            }
            catch (IOException e){
                ConsoleIO.printMessage("File Backup Error: " + e.getMessage());
            }
        } while (showMenu);
    }

    private static void addLecturer(College college) throws CollegeException {
        String lecturerName = ConsoleIO.getStringFromUser("Enter name: ");
        String id = ConsoleIO.getStringFromUser("Enter ID number: ");
        String degreeLevel = ConsoleIO.getStringFromUser("Enter degree (Bachelor/Master/Doctorate/Professor): ");
        String degreeTitle = ConsoleIO.getStringFromUser("Enter degree Title: ");
        double salary = ConsoleIO.getDoubleFromUser("Enter Salary: ");
        String departmentName = ConsoleIO.getStringFromUser("Enter department name (or press Enter to skip): ");

        int numArticles = 0;
        Set<String> articles = new HashSet<>();
        String grantingInstitution = null;

        if (degreeLevel.equalsIgnoreCase("Doctorate") ||
                degreeLevel.equalsIgnoreCase("Professor")) {

            numArticles = ConsoleIO.getIntFromUser("Enter number of articles: ");
            articles = new HashSet<>();

            for (int i = 0; i < numArticles; i++) {
                String article = ConsoleIO.getStringFromUser("Article " + (i + 1) + ": ");
                articles.add(article);
            }

            if (degreeLevel.equals(Enums.degreeLevel.PROFESSOR.toString())) {
                grantingInstitution = ConsoleIO.getStringFromUser("Enter granting institution: ");
            }
        }
        college.createNewLecturer(lecturerName, id, degreeLevel, degreeTitle, salary, departmentName, articles, grantingInstitution);
        ConsoleIO.printMessage("Lecturer added successfully.");
    }
}
