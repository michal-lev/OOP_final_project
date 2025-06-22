package michal_liora;

import java.io.*;
import java.util.ArrayList;

public class College implements Serializable {
    private final String name;
    private final ArrayList<Lecturer> lecturers;
    private final ArrayList<Committee> committees;
    private final ArrayList<Department> departments;
    private final static String collegeBackupPath = "collegeBackup.bin";

    public College(String name) {
        this.name = name;
        this.lecturers = new ArrayList<>();
        this.committees = new ArrayList<>();
        this.departments = new ArrayList<>();
    }

    public static void uploadBackupFile() throws IOException, ClassNotFoundException {
        College collegeBackup;
        File file = new File(collegeBackupPath);

        if (file.exists()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            collegeBackup = (College) objectInputStream.readObject();
            Main.printMessage("Loaded college from backup.");
        }
        else{
            Main.printMessage("No backup found, please create your college :)");
        }
    }
    public void saveBeforeExit() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(collegeBackupPath);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this);
        Main.printMessage("All college info saved.");

        objectOutputStream.close();
        fileOutputStream.close();
    }

    public static String lecturerNamesToString(ArrayList<Lecturer> lecturersArr) {
        ArrayList<String> lecturersNames = new ArrayList<>();
        for(int i = 0 ; i< lecturersArr.size();i++){
            lecturersNames.add(lecturersArr.get(i).getName());
        }
        return lecturersNames.toString();
    }


    public static boolean LecturerArrEqualsByName(ArrayList<Lecturer> lecturers1, ArrayList<Lecturer>  lecturers2){
        if (lecturers1.size() == lecturers2.size()){
            return false;
        }
        for (int i = 0; i < lecturers1.size(); i++){
            if (!lecturers1.get(i).getName().equals(lecturers2.get(i).getName())){
                return false;
            }
        }
        return true;
    }

    public void testCreateCommitteeClone(Committee committee) throws NotExistException {
        if (committee == null){
            throw new NotExistException(Enums.errorMessage.COMMITTEE_NOT_EXIST.getMessage());
        }
    }
    public void createCommitteeClone() throws CollegeException {
        String committeeName = Main.getStringFromUser("Enter committee name: ");
        Committee committeeToClone = getCommitteeByName(committeeName);
        testCreateCommitteeClone(committeeToClone);
        Committee newCommittee = new Committee(committeeToClone);
        addCommittee(newCommittee);
    }

    public String getName(String className){
        String name;
        boolean nameExists;
        do{
            name = Main.getNameFromUser(className);
            nameExists = (getLecturerByName(name) != null);
            if(nameExists){
                Main.printMessage("Name already exists, try again");
            }
        }while(nameExists);
        return name;
    }

    public void createNewLecturer() throws CollegeException {
        String name = getName(Lecturer.class.getSimpleName());
        String id = Main.getStringFromUser("Enter ID number: ");
        String degreeLevel = Main.getStringFromUser("Enter degree (Bachelor/Master/Doctorate/Professor): ");
        String degreeTitle = Main.getStringFromUser("Enter degree Title: ");
        double salary = Main.getDoubleFromUser("Enter Salary: ");
        String departmentName = Main.getStringFromUser("Enter department name (or press Enter to skip): ");
        Department department = getDepartmentByName(departmentName);
        boolean departmentNameEmpty = departmentName.isEmpty();
        String lecturerType = testLecturerDetails(name, id,degreeLevel, degreeTitle, salary, departmentNameEmpty, department);
        Lecturer newLecturer;
        if(lecturerType.equals("regular")){
            newLecturer = new Lecturer(name, id, degreeLevel, degreeTitle, salary, department);
        }
        else{
            int numArticles = Main.getIntFromUser("Enter number of articles: ");
            ArrayList<String> articles = getArticles(numArticles);
            if(lecturerType.equals(Enums.degreeLevel.PROFESSOR.toString())){
                String grantingInstitution = Main.getStringFromUser("Enter the professor's granting institution : ");
                if (grantingInstitution.isEmpty()){
                    throw new InvalidUserInputException(Enums.errorMessage.LECTURER_DETAIL_EMPTY.getMessage());
                }
                newLecturer = new Professor(name, id, degreeLevel, degreeTitle, salary, department,articles,grantingInstitution);
            }
            else{
                newLecturer = new Doctor(name, id, degreeLevel, degreeTitle, salary, department, articles);
            }
        }
        lecturers.add(newLecturer);
        if (!departmentNameEmpty) {
            department.addLecturer(newLecturer);
        }
    }

    public String testLecturerDetails(String name, String id, String degreeLevel, String degreeTitle, double salary, boolean departmentNameEmpty, Department department) throws CollegeException {
        boolean validDepartmentName = (departmentNameEmpty || department != null);
        if(name.isEmpty() || id.isEmpty() || degreeTitle.isEmpty()){
            throw new InvalidUserInputException(Enums.errorMessage.LECTURER_DETAIL_EMPTY.getMessage());
        }
        String validDegreeLevel = getValidDegreeLevel(degreeLevel); //could throw
        if (!validDepartmentName){
            throw new NotExistException(Enums.errorMessage.DEPARTMENT_NOT_EXIST.getMessage());
        }
        checkValidSalary(salary);
        if(validDegreeLevel.equals(Enums.degreeLevel.PROFESSOR.toString())) {
            return validDegreeLevel;
        }
        if(validDegreeLevel.equals(Enums.degreeLevel.DOCTORATE.toString())) {
            return validDegreeLevel;
        }
        return "regular";
    }

    public ArrayList<String> getArticles(int numArticles) throws InvalidUserInputException {
        ArrayList<String> articles = new ArrayList<>();
        String articleName;
        for (int i = 0; i < numArticles; i++){
            articleName = Main.getStringFromUser("Article " + (i+1) + " : ");
            if (articleName.isEmpty()){
                throw new InvalidUserInputException(Enums.errorMessage.ARTICLE_NAME_EMPTY.getMessage());
            }
            articles.add(articleName);
        }
        return articles;
    }

    public String testCommitteeDetails(String name, Lecturer chair, String memberType) throws CollegeException{
        Committee existingCommittee = getCommitteeByName(name);
        if (existingCommittee != null){
            throw new NoDuplicatesException(Enums.errorMessage.COMMITTEE_EXISTS.getMessage());
        }
        if (chair == null){
            throw new NotExistException(Enums.errorMessage.LECTURER_NOT_EXIST.getMessage());
        }
        if(!(chair instanceof Doctor)){
            throw new InvalidOperationValueException(Enums.errorMessage.INVALID_CHAIR_DEGREE.getMessage());
        }
        String validMemberType = getValidDegreeLevel(memberType); //could throw
        return validMemberType;
    }

    public void createNewCommittee() throws CollegeException{
        String name = Main.getNameFromUser(Committee.class.getSimpleName());
        String chairName = Main.getStringFromUser("Enter chair name: ");
        Lecturer chair = getLecturerByName(chairName);
        String memberType = Main.getStringFromUser("Enter members degree level (bachelor/master/doctorate/professor): ");

        String validMemberType = testCommitteeDetails(name, chair,memberType); // might throw

        Committee newCommittee = new Committee(name, chair, validMemberType);
        addCommittee(newCommittee);
    }

    public void testCreateNewDepartment(String name, int studentCount) throws InvalidUserInputException{
        if (name.isEmpty())
            throw new InvalidUserInputException(Enums.errorMessage.NAME_EMPTY.getMessage());
        if (!checkValidStudentCount(studentCount))
            throw new InvalidUserInputException(Enums.errorMessage.INVALID_STUDENT_COUNT.getMessage());
    }

    public void createNewDepartment() throws CollegeException{
        String name = getName(Department.class.getSimpleName());
        int studentCount = Main.getIntFromUser("Enter number of students in department: ");

        testCreateNewDepartment(name, studentCount);

        Department newDepartment = new Department(name,studentCount);
        departments.add(newDepartment);
    }

    public static String getValidDegreeLevel(String degreeLevel) throws CollegeException{
        for (Enums.degreeLevel degLvl : Enums.degreeLevel.values()){
            if (degLvl.toString().equalsIgnoreCase(degreeLevel)){
                return degLvl.toString();
            }
        }
        throw new InvalidUserInputException(Enums.errorMessage.INVALID_DEGREE_LEVEL.getMessage());
    }

    public static void checkValidSalary(double salary) throws InvalidUserInputException {
        if (salary < 0) {
            throw new InvalidUserInputException(Enums.errorMessage.INVALID_SALARY.getMessage());
        }
    }

    public static boolean checkValidStudentCount(int studentCount){
        if (studentCount >= 0) {
            return true;
        }
        return false;
    }

    public void addCommittee(Committee committee) {
        committees.add(committee);
    }

    public Committee getCommitteeByName(String committeeName){
        for(int i = 0; i < committees.size(); i++){
            Committee committee = committees.get(i);
            if (committee.getName().equalsIgnoreCase(committeeName)){
                return committee;
            }
        }
        return null;
    }

    public void testChangeCommitteeHead(Committee committee, Lecturer newChair) throws CollegeException{
        if (committee == null){
            throw new NotExistException(Enums.errorMessage.COMMITTEE_NOT_EXIST.getMessage());
        }
        if (newChair == null){
            throw new NotExistException(Enums.errorMessage.LECTURER_NOT_EXIST.getMessage());
        }
        if (!(newChair instanceof Doctor)){
            throw new InvalidOperationValueException(Enums.errorMessage.INVALID_CHAIR_DEGREE.getMessage());
        }
        if (checkIfLecturerInCommittee(newChair,committee)){
            throw new InvalidOperationValueException(Enums.errorMessage.CHAIR_CANT_BE_MEMBER.getMessage());
        }

    }
    public void changeCommitteeHead() throws CollegeException {
        String committeeName = Main.getNameFromUser(Committee.class.getSimpleName());
        String chairName = Main.getStringFromUser("Enter chair name: ");
        Committee committee = getCommitteeByName(committeeName);
        Lecturer newChair = getLecturerByName(chairName);
        testChangeCommitteeHead(committee,newChair);
        committee.setChair(newChair);
    }


    public Lecturer getLecturerByName(String lecturerName){
        for(int i = 0; i < lecturers.size(); i++){
            Lecturer lecturer = lecturers.get(i);
            if (lecturer.getName().equalsIgnoreCase(lecturerName)){
                return lecturer;
            }
        }
        return null;
    }

    public Department getDepartmentByName(String departmentName){
        for(int i = 0; i < departments.size(); i++){
            Department department = departments.get(i);
            if (department.getName().equalsIgnoreCase(departmentName)){
                return department;
            }
        }
        return null;
    }

    public void testAddLecturerToCommittee(Committee committee, Lecturer lecturer) throws CollegeException{
        if (committee == null){
            throw new NotExistException(Enums.errorMessage.COMMITTEE_NOT_EXIST.getMessage());
        }
        if (lecturer == null){
            throw new NotExistException(Enums.errorMessage.LECTURER_NOT_EXIST.getMessage());
        }
        if (checkIfLecturerInCommittee(lecturer,committee)){
            throw new NoDuplicatesException(Enums.errorMessage.LECTURER_ALREADY_IN_COMMITTEE.name());
        }
        if (committee.getChair().getName().equals(lecturer.getName())){
            throw new InvalidOperationValueException(Enums.errorMessage.CHAIR_CANT_BE_MEMBER.getMessage());
        }
        if (committee.getMemberType().equals(lecturer.getDegreeLevel())){
            throw new InvalidOperationValueException(Enums.errorMessage.MEMBER_TYPE_MISMATCH.getMessage());
        }
    }

    public void addLecturerToCommittee() throws CollegeException {
        String committeeName = Main.getStringFromUser("Enter committee name: ");
        String lecturerName = Main.getStringFromUser("Enter lecturer name: ");
        Committee committee = getCommitteeByName(committeeName);
        Lecturer lecturer = getLecturerByName(lecturerName);

        testAddLecturerToCommittee(committee, lecturer); // could throw

        committee.addMember(lecturer);
        lecturer.updateCommittees(committee);
    }

    public void testRemoveMemberFromCommittee(Committee committee, Lecturer lecturer) throws CollegeException {
        if (committee == null){
            throw new NotExistException(Enums.errorMessage.COMMITTEE_NOT_EXIST.getMessage());
        }
        if (lecturer == null){
            throw new NotExistException(Enums.errorMessage.LECTURER_NOT_EXIST.getMessage());
        }
        if (!checkIfLecturerInCommittee(lecturer,committee)){
            throw new InvalidOperationValueException(Enums.errorMessage.LECTURER_NOT_IN_COMMITTEE.getMessage());
        }

    }

    public void removeMemberFromCommittee() throws CollegeException{
        String committeeName = Main.getStringFromUser("Enter committee name: ");
        String lecturerName = Main.getStringFromUser("Enter lecturer name: ");
        Committee committee = getCommitteeByName(committeeName);
        Lecturer member = getLecturerByName(lecturerName);

        testRemoveMemberFromCommittee(committee,member);

        committee.removeMember(member); // remove member from member arr in committee
        member.removeCommittee(committee); // remove committee from committees arr in lecturer
    }

    public boolean checkIfLecturerInCommittee(Lecturer lecturer, Committee committee){
        ArrayList<Lecturer> members = committee.getMembers();
        for( int i = 0; i < members.size(); i++) {
            if (members.get(i).equals(lecturer)){
                return true;
            }
        }
        return false;
    }

    public double getSalaryAvg(ArrayList<Lecturer> lecturersArr){
        double salarySum = 0, avg;
        int arrSize = lecturersArr.size();
        for (int i = 0; i < arrSize; i++) {
            salarySum += lecturersArr.get(i).getSalary();
        }
        avg = salarySum / arrSize;
        avg = (double) ((int) (avg * 100)) / 100;
        return avg;
    }

    public void getLecturersSalaryAvg() {
        double salaryAvg = getSalaryAvg(lecturers);
        Main.printMessage("The salary average is : " + salaryAvg);
    }

    public void testGetDepartmentMembersSalaryAvg(Department department) throws NotExistException{
        if(department == null){
            throw new NotExistException(Enums.errorMessage.DEPARTMENT_NOT_EXIST.getMessage());
        }
    }
    public void getDepartmentMembersSalaryAvg() throws CollegeException {
        String name = Main.getStringFromUser("Enter department Name: ");
        Department department = getDepartmentByName(name);
        testGetDepartmentMembersSalaryAvg(department);

        double salaryAvg = getSalaryAvg(department.getLecturers());

        Main.printMessage("The salary average is : " + salaryAvg);
    }

    public void testAddLecturerToDepartment(Department department, Lecturer lecturer) throws CollegeException {
        if(department == null){
            throw new NotExistException(Enums.errorMessage.DEPARTMENT_NOT_EXIST.getMessage());
        }
        if(lecturer == null){
            throw new NotExistException(Enums.errorMessage.LECTURER_NOT_EXIST.getMessage());
        }
        if(lecturer.getDepartment() != null){
            throw new NoDuplicatesException(Enums.errorMessage.LECTURER_HAS_DEPARTMENT.getMessage());
        }
    }

    public void addLecturerToDepartment() throws CollegeException {
        String departmentName = Main.getStringFromUser("Enter department name: ");
        String lecturerName = Main.getStringFromUser("Enter lecturer name: ");

        Department department = getDepartmentByName(departmentName);
        Lecturer lecturer = getLecturerByName(lecturerName);
        testAddLecturerToDepartment(department, lecturer); //could throw

        department.addLecturer(lecturer);
        lecturer.setDepartment(department);
    }

    public String lecturerArrToString(ArrayList<Lecturer> lecturerArr){
        String toReturn = "";
        for(int i = 0; i < lecturerArr.size(); i++){
            toReturn += lecturerArr.get(i).toString() + "\n";
        }
        return toReturn;
    }

    public String committeesArrToString(ArrayList<Committee> committeesArr){
        String toReturn = "";
        for(int i = 0; i < committeesArr.size(); i++){
            toReturn += committeesArr.get(i).toString() + "\n";
        }
        return toReturn;
    }

    public void getDetailsOfAllLecturers(){
        Main.printMessage(lecturerArrToString(lecturers));
    }

    public void getDetailsOfAllCommittees(){
        Main.printMessage(committeesArrToString(committees));
    }

    public void testCompareDoctorsAndProfessors(Lecturer lecturer1, Lecturer lecturer2) throws CollegeException {
        if (lecturer1 == null || lecturer2 == null){
            throw new NotExistException(Enums.errorMessage.LECTURER_NOT_EXIST.getMessage());
        }
        if (!(lecturer1 instanceof Doctor && lecturer2 instanceof Doctor)){
            throw new InvalidOperationValueException(Enums.errorMessage.NOT_DOCTOR.getMessage());
        }
    }

    public void compareDoctorsAndProfessors() throws CollegeException {
        String lecturerName1 = Main.getStringFromUser("Enter first lecturer name: ");
        String lecturerName2 = Main.getStringFromUser("Enter second lecturer name: ");
        Lecturer lecturer1 = getLecturerByName(lecturerName1);
        Lecturer lecturer2 = getLecturerByName(lecturerName2);

        testCompareDoctorsAndProfessors(lecturer1,lecturer2);

        int compareResult = ((Doctor) lecturer1).compareTo((Doctor) lecturer2);
        Main.printMessage(getCompareString(compareResult, Lecturer.class.getSimpleName()));
    }

    public void testCompareCommittees(Committee committee1, Committee committee2) throws NotExistException {
        if (committee1 == null || committee2 == null){
            throw new NotExistException(Enums.errorMessage.COMMITTEE_NOT_EXIST.getMessage());
        }
    }

    public void compareCommittees() throws CollegeException{
        String committeeName1 = Main.getStringFromUser("Enter first committee name: ");
        String committeeName2 = Main.getStringFromUser("Enter second committee name: ");
        Committee committee1 = getCommitteeByName(committeeName1);
        Committee committee2 = getCommitteeByName(committeeName2);

        testCompareCommittees(committee1,committee2);

        int compareChoice = Main.getIntFromUser("Choose a filter:\n  1) By number of members\n  2) By total number of members' articles\n");
        int compareResult;
        SortCommitteeByNumMembers numMembersComparator = new SortCommitteeByNumMembers();
        SortCommitteeByTotalNumArticles totalNumArticles = new SortCommitteeByTotalNumArticles();

        switch (compareChoice){
            case 1:
                compareResult = numMembersComparator.compare(committee1,committee2);
                break;
            case 2:
                compareResult = totalNumArticles.compare(committee1,committee2);
                break;
            default:
                throw new InvalidUserInputException(Enums.errorMessage.INVALID_CHOICE.getMessage());
        }

        Main.printMessage(getCompareString(compareResult,Committee.class.getSimpleName()));
    }

    public String getCompareString(int compareResult, String className){
        if (compareResult == 0){
            return "Both have the same amount.";
        }
        if (compareResult < 0){
            return "The second " + className + " has more";
        }
        return "The first " + className + " has more";
    }
}