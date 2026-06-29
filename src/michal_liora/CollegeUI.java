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
                        addLecturer();
                        break;

                    case 2:
                        createNewCommittee();
                        break;

                    case 3:
                        addLecturerToCommittee();
                        break;

                    case 4:
                        changeCommitteeHead();
                        break;

                    case 5:
                        removeMemberFromCommittee();
                        break;

                    case 6:
                        createNewDepartment();
                        break;

                    case 7:
                        getLecturersSalaryAvg();
                        break;

                    case 8:
                        getDepartmentMembersSalaryAvg();
                        break;
                    case 9:
                        getDetailsOfAllLecturers();
                        break;
                    case 10:
                        getDetailsOfAllCommittees();
                        break;
                    case 11:
                        addLecturerToDepartment();
                        break;
                    case 12:
                        compareDoctorsAndProfessors();
                        break;
                    case 13:
                        compareCommittees();
                        break;
                    case 14:
                        createCommitteeClone();
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

    private void addLecturer() throws CollegeException {
        Set<Lecturer> lecturers = college.getLecturers();
        String lecturerName = getUniqueName(lecturers, Lecturer.class.getSimpleName());
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

            if (degreeLevel.equalsIgnoreCase(Enums.degreeLevel.PROFESSOR.toString())) {
                grantingInstitution = ConsoleIO.getStringFromUser("Enter granting institution: ");
            }
        }
        college.createNewLecturer(lecturerName, id, degreeLevel, degreeTitle, salary, departmentName, articles, grantingInstitution);
        ConsoleIO.printMessage("Lecturer added successfully.");
    }

    private <T extends HasName> String getUniqueName(Set<T> set, String className) {
        String name;
        do {
            name = ConsoleIO.getNameFromUser(className);
            if (!college.isUniqueName(set, name)) {
                ConsoleIO.printMessage("Name already exists, try again");
            }

        } while (!college.isUniqueName(set, name));

        return name;
    }

    private void createNewCommittee() throws CollegeException {
        Set<Committee> Committees = college.getCommittees();
        String name = getUniqueName(Committees, Committee.class.getSimpleName());
        String chairName = ConsoleIO.getStringFromUser("Enter chair name: ");
        String memberType = ConsoleIO.getStringFromUser("Enter members degree level (bachelor/master/doctorate/professor): ");
        college.createNewCommittee(name, chairName, memberType);
        ConsoleIO.printMessage("Committee created successfully.");
    }

    private void addLecturerToCommittee() throws CollegeException {
        String committeeName = ConsoleIO.getNameFromUser(Committee.class.getSimpleName());
        String memberName = ConsoleIO.getNameFromUser(Lecturer.class.getSimpleName());

        college.addLecturerToCommittee(committeeName, memberName);
        ConsoleIO.printMessage("Member added to committee successfully.");
    }

    private void changeCommitteeHead() throws CollegeException {
        String commName = ConsoleIO.getStringFromUser("Enter committee name: ");
        String chairName = ConsoleIO.getStringFromUser("Enter new chair name: ");
        college.changeCommitteeHead(commName, chairName);
        ConsoleIO.printMessage("Committee chair updated successfully.");
    }

    private void removeMemberFromCommittee() throws CollegeException{
        String commName = ConsoleIO.getStringFromUser("Enter committee name: ");
        String lecturerName = ConsoleIO.getStringFromUser("Enter lecturer name to remove: ");
        college.removeMemberFromCommittee(commName, lecturerName);
        ConsoleIO.printMessage("Lecturer removed from committee successfully.");

    }

    private void createNewDepartment() throws CollegeException{
        Set<Department> departments = college.getDepartments();
        String departmentName = getUniqueName(departments, Department.class.getSimpleName());
        int studentCount = ConsoleIO.getIntFromUser("Enter number of students in department: ");
        college.createNewDepartment(departmentName, studentCount);
        ConsoleIO.printMessage("Department added successfully.");
    }

    private void getLecturersSalaryAvg() throws CollegeException{
        double salaryAvg = college.getLecturersSalaryAvg();
        ConsoleIO.printMessage("The average salary of all lecturers is: " + salaryAvg);
    }

    private void getDepartmentMembersSalaryAvg() throws CollegeException{
        String departmentName = ConsoleIO.getStringFromUser("Enter department Name: ");
        double departmentSalaryAvg = college.getDepartmentMembersSalaryAvg(departmentName);
        ConsoleIO.printMessage("The salary average is : " + departmentSalaryAvg);

    }

    private void getDetailsOfAllLecturers() throws CollegeException{
        int orderBy = ConsoleIO.getIntFromUser("Please select a sorting criterion:\n  1 - by Name\n  2 - by Degree Level\n  3 - by Salary\n");
        String orderedLecturers =  college.getDetailsOfAllLecturers(orderBy);
        ConsoleIO.printMessage(orderedLecturers);
    }

    private void getDetailsOfAllCommittees() throws CollegeException{
        int orderBy = ConsoleIO.getIntFromUser("Please select a sorting criterion:\n  1 - by Name\n  2 - by Number of Members\n  3 - by Member Type\n");
        String orderedCommittees =  college.getDetailsOfAllCommittees(orderBy);
        ConsoleIO.printMessage(orderedCommittees);
    }

    private void addLecturerToDepartment() throws CollegeException{
        String departmentName = ConsoleIO.getStringFromUser("Enter department name: ");
        String lecturerName = ConsoleIO.getStringFromUser("Enter lecturer name: ");
        college.addLecturerToDepartment(departmentName, lecturerName);
    }

    private void compareDoctorsAndProfessors() throws CollegeException{
        String lecturerName1 = ConsoleIO.getStringFromUser("Enter first lecturer name: ");
        String lecturerName2 = ConsoleIO.getStringFromUser("Enter second lecturer name: ");
        String compareStr = college.compareDoctorsAndProfessors(lecturerName1, lecturerName2);
        ConsoleIO.printMessage(compareStr);
    }

    private void compareCommittees() throws CollegeException{
        String committeeName1 = ConsoleIO.getStringFromUser("Enter first committee name: ");
        String committeeName2 = ConsoleIO.getStringFromUser("Enter second committee name: ");
        int compareChoice = ConsoleIO.getIntFromUser("Choose a filter:\n  1) By number of members\n  2) By total number of members' articles\n");
        String compareStr = college.compareCommittees(committeeName1, committeeName2, compareChoice);
        ConsoleIO.printMessage(compareStr);

    }

    private void createCommitteeClone() throws CollegeException{
        String committeeName = ConsoleIO.getStringFromUser("Enter committee name: ");
        college.createCommitteeClone(committeeName);
    }

}
