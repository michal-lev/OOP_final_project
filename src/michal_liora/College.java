package michal_liora;

public class College {
    private final String name;
    private Lecturer[] lecturers;
    private Committee[] committees;
    private Department[] departments;

    private int lecturerCount;
    private int committeeCount;
    private int departmentCount;

    final int EXPAND_ARR_MULTI = 2;

    public College(String name) {
        this.name = name;
        this.lecturers = new Lecturer[1];
        this.committees = new Committee[1];
        this.departments = new Department[1];

        this.lecturerCount = 0;
        this.committeeCount = 0;
        this.departmentCount = 0;
    }

    public static String lecturerNamesToString(Lecturer[] lecturersArr, int lecturersCount) {
        String lecturersStr = "[";
        int i = 0;
        for (; i < (lecturersCount - 1); i++) {
            lecturersStr += lecturersArr[i].getName() + ", ";
        }
        if (lecturersCount != 0) {
            lecturersStr += lecturersArr[i].getName();
        }
        lecturersStr += "]";
        return lecturersStr;
    }


    public static boolean LecturerArrEqualsByName(Lecturer[] lecturers1, Lecturer[] lecturers2, int lecturersCount){
        for (int i = 0; i < lecturersCount; i++){
            if (!lecturers1[i].getName().equals(lecturers2[i].getName())){
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
            String[] articles = getArticles(numArticles);
            if(lecturerType.equals(Enums.degreeLevel.PROFESSOR.toString())){
                String grantingInstitution = Main.getStringFromUser("Enter the professor's granting institution : ");
                if (grantingInstitution.isEmpty()){
                    throw new InvalidUserInputException(Enums.errorMessage.LECTURER_DETAIL_EMPTY.getMessage());
                }
                newLecturer = new Professor(name, id, degreeLevel, degreeTitle, salary, department, numArticles,articles,grantingInstitution);
            }
            else{
                newLecturer = new Doctor(name, id, degreeLevel, degreeTitle, salary, department, numArticles,articles);
            }
        }
        addLecturer(newLecturer);
        if (!departmentNameEmpty) {
            addLecturerToDepartmentInCollege(newLecturer, department);
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

    public String[] getArticles(int numArticles) throws InvalidUserInputException {
        String[] articles = new String[numArticles];
        for (int i = 0; i < numArticles; i++){
            articles[i] = Main.getStringFromUser("Article " + (i+1) + " : ");
            if (articles[i].isEmpty()){
                throw new InvalidUserInputException(Enums.errorMessage.ARTICLE_NAME_EMPTY.getMessage());
            }
        }
        return articles;
    }

    public void testCommitteeDetails(String name, Lecturer chair) throws CollegeException{
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
    }

    public void createNewCommittee() throws CollegeException{
        String name = Main.getNameFromUser(Committee.class.getSimpleName());
        String chairName = Main.getStringFromUser("Enter chair name: ");
        Lecturer chair = getLecturerByName(chairName);
        testCommitteeDetails(name, chair); // might throw
        Committee newCommittee = new Committee(name, chair);
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
        addDepartment(newDepartment);
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

    public Lecturer[] addLecturerToArray(Lecturer[] lecturerArr,int arrCount, Lecturer newLecturer){
        if (lecturerArr.length == arrCount) {
            Lecturer[] newArray = new Lecturer[lecturerArr.length * EXPAND_ARR_MULTI];
            for (int i = 0; i < arrCount; i++) {
                newArray[i] = lecturerArr[i];
            }
            lecturerArr = newArray;
        }
        lecturerArr[arrCount] = newLecturer;
        return lecturerArr;
    }

    public void addLecturer(Lecturer lecturer) {
        lecturers = addLecturerToArray(lecturers,lecturerCount,lecturer);
        lecturerCount++;
    }

    public Committee[] addCommitteeToArray(Committee[] committeeArr,int arrCount, Committee newCommittee){
        if (committeeArr.length == arrCount) {
            Committee[] newArray = new Committee[committeeArr.length * EXPAND_ARR_MULTI];
            for (int i = 0; i < arrCount; i++) {
                newArray[i] = committeeArr[i];
            }
            committeeArr = newArray;
        }
        committeeArr[arrCount] = newCommittee;
        return committeeArr;
    }

    public void addCommittee(Committee committee) {
        committees = addCommitteeToArray(committees,committeeCount,committee);
        committeeCount++;
    }

    public Committee getCommitteeByName(String committeeName){
        for(int i = 0; i < committeeCount; i++){
            if (committees[i].getName().equalsIgnoreCase(committeeName)){
                return committees[i];
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
        for(int i = 0; i < lecturerCount; i++){
            if (lecturers[i].getName().equalsIgnoreCase(lecturerName)){
                return lecturers[i];
            }
        }
        return null;
    }

    public Department getDepartmentByName(String departmentName){
        for(int i = 0; i < departmentCount; i++){
            if (departments[i].getName().equalsIgnoreCase(departmentName)){
                return departments[i];
            }
        }
        return null;
    }

    public void addDepartment(Department department) {
        if (departments.length == departmentCount) {
            Department[] newArray = new Department[departments.length * EXPAND_ARR_MULTI];
            for (int i = 0; i < departmentCount; i++) {
                newArray[i] = departments[i];
            }
            departments = newArray;
        }
        departments[departmentCount++] = department;
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
    }

    public void addLecturerToCommittee() throws CollegeException {
        String committeeName = Main.getStringFromUser("Enter committee name: ");
        String lecturerName = Main.getStringFromUser("Enter lecturer name: ");
        Committee committee = getCommitteeByName(committeeName);
        Lecturer lecturer = getLecturerByName(lecturerName);

        testAddLecturerToCommittee(committee, lecturer);

        int currentMemberCount = committee.getMemberCount();
        Lecturer[] updatedMembers = addLecturerToArray(committee.getMembers(),currentMemberCount, lecturer);
        committee.setMembers(updatedMembers);
        committee.setMemberCount(currentMemberCount + 1);
        updateLecturerNewCommittee(lecturer,committee);
    }

    public void updateLecturerNewCommittee(Lecturer lecturer, Committee committee){
        int currentCommitteesCount = lecturer.getCommitteesCount();
        Committee[] updatedCommittees = addCommitteeToArray(lecturer.getCommittees(),currentCommitteesCount, committee);
        lecturer.setCommittees(updatedCommittees);
        lecturer.setCommitteesCount(currentCommitteesCount + 1);
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
        member.updateRemovedFromCommittee(committee); // remove committee from committees arr in lecturer
    }

    public boolean checkIfLecturerInCommittee(Lecturer lecturer, Committee committee){
        Lecturer[] members = committee.getMembers();
        for( int i = 0; i < committee.getMemberCount(); i++) {
            if (members[i].equals(lecturer)){
                return true;
            }
        }
        return false;
    }

    public double getSalaryAvg(Lecturer[] lecturersArr, int lecturersArrCount){
        double salarySum = 0, avg;
        for (int i = 0; i < lecturersArrCount; i++) {
            salarySum += lecturersArr[i].getSalary();
        }
        avg = salarySum / lecturersArrCount;
        avg = (double) ((int) (avg * 100)) / 100;
        return avg;
    }

    public void getLecturersSalaryAvg() {
        double salaryAvg = getSalaryAvg(lecturers, lecturerCount);
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

        Lecturer[] departmentLecturers = department.getLecturers();
        int departmentLecturersCount = department.getLecturerCount();
        double salaryAvg = getSalaryAvg(departmentLecturers, departmentLecturersCount);

        Main.printMessage("The salary average is : " + salaryAvg);
    }

    public void addLecturerToDepartmentInCollege(Lecturer lecturer,Department department){
        int currentLecturerCount = department.getLecturerCount();
        Lecturer[] updatedLecturers = addLecturerToArray(department.getLecturers(),currentLecturerCount, lecturer);
        department.setLecturers(updatedLecturers);
        department.setLecturerCount(currentLecturerCount + 1);
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

        addLecturerToDepartmentInCollege(lecturer,department);
        lecturer.setDepartment(department);
    }

    public String lecturerArrToString(Lecturer[] lecturerArr, int lecturerArrCount){
        String toReturn = "";
        for(int i = 0; i < lecturerArrCount; i++){
            toReturn += lecturerArr[i].toString() + "\n";
        }
        return toReturn;
    }

    public String committeesArrToString(Committee[] committeesArr, int committeesArrCount){
        String toReturn = "";
        for(int i = 0; i < committeesArrCount; i++){
            toReturn += committeesArr[i].toString() + "\n";
        }
        return toReturn;
    }

    public void getDetailsOfAllLecturers(){
        Main.printMessage(lecturerArrToString(lecturers,lecturerCount));
    }

    public void getDetailsOfAllCommittees(){
        Main.printMessage(committeesArrToString(committees,committeeCount));
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