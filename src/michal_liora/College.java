package michal_liora;

import michal_liora.Enums.degreeLevel;
import java.io.*;
import java.util.*;

public class College implements Serializable {
    private final String name;
    private final Set<Lecturer> lecturers;
    private final Set<Committee> committees;
    private final Set<Department> departments;

    public College(String name) {
        this.name = name;
        this.lecturers = new HashSet<>();
        this.committees = new HashSet<>();
        this.departments = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public static String namesToString(Set<? extends HasName> setWithNames) {
        Iterator<? extends HasName> it = setWithNames.iterator();
        StringBuilder names = new StringBuilder("[");

        while(it.hasNext()) {
            names.append(it.next().getName());
            if (it.hasNext())
                names.append(", ");
        }
        names.append("]");
        return names.toString();
    }

    public static <T extends HasName> boolean equalsByName(Set<T> set1, Set<T> set2){
        Set<String> names1 = new HashSet<>();
        Set<String> names2 = new HashSet<>();
        if (set1.size() != set2.size()){
            return false;
        }
        for(T item:set1){
            names1.add(item.getName());
        }
        for(T item:set2){
            names2.add(item.getName());
        }
        return names1.equals(names2);
    }

    public void testCreateCommitteeClone(Committee committee) throws NotExistException {
        if (committee == null){
            throw new NotExistException(Enums.errorMessage.COMMITTEE_NOT_EXIST.getMessage());
        }
    }
    
    public void createCommitteeClone() throws CollegeException {
        String committeeName = Main.getStringFromUser("Enter committee name: ");
        Committee committeeToClone = getByName(committees, committeeName);
        testCreateCommitteeClone(committeeToClone);
        Committee newCommittee = new Committee(committeeToClone);
        addCommittee(newCommittee);
    }

    public String getName(Set<? extends HasName> set, String className){
        String name;
        boolean nameExists;
        do{
            name = Main.getNameFromUser(className);
            nameExists = (getByName(set,name) != null);
            if(nameExists){
                Main.printMessage("Name already exists, try again");
            }
        }while(nameExists);
        return name;
    }

    public void createNewLecturer(String name, String id, String degreeLevel, String degreeTitle, double salary, String departmentName, Set<String> articles, String grantingInstitution) throws CollegeException {
        Department department = getByName(departments, departmentName);
        boolean departmentNameEmpty = departmentName.isEmpty();
        String lecturerType = testLecturerDetails(name, id,degreeLevel, degreeTitle, salary, departmentNameEmpty, department);

        Lecturer newLecturer;
        if(lecturerType.equals("regular")){
            newLecturer = new Lecturer(name, id, degreeLevel, degreeTitle, salary, department);
        }
        else if (lecturerType.equals(Enums.degreeLevel.PROFESSOR.toString())) {
            if (grantingInstitution == null || grantingInstitution.isEmpty()) {
                throw new InvalidUserInputException(Enums.errorMessage.LECTURER_DETAIL_EMPTY.getMessage());
            }
            newLecturer = new Professor(name, id, degreeLevel, degreeTitle, salary, department, articles, grantingInstitution);
        }
        else {
            newLecturer = new Doctor(name, id, degreeLevel, degreeTitle, salary, department, articles);
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

    public Set<String> getArticlesNames(int numArticles) throws InvalidUserInputException {
        Set<String> articles = new HashSet<>();
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
        Committee existingCommittee = getByName(committees, name);
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

    public void createNewCommittee(String name, String chairName, String memberType) throws CollegeException {
        Lecturer chair = getByName(lecturers, chairName);
        String validMemberType = testCommitteeDetails(name, chair,memberType); // might throw
        Committee newCommittee = new Committee(name, chair, validMemberType);
        addCommittee(newCommittee);
    }

    public void testCreateNewDepartment(Department existingDept,String name, int studentCount) throws CollegeException {
        if (existingDept != null) {
            throw new CollegeException(Enums.errorMessage.DEPARTMENT_EXISTS.getMessage());
        }
        if (name.isEmpty())
            throw new InvalidUserInputException(Enums.errorMessage.NAME_EMPTY.getMessage());
        if (!checkValidStudentCount(studentCount))
            throw new InvalidUserInputException(Enums.errorMessage.INVALID_STUDENT_COUNT.getMessage());
    }

    public void createNewDepartment(String name, int studentCount) throws CollegeException {
        Department existingDept = getByName(departments, name);
        testCreateNewDepartment(existingDept, name, studentCount);
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

    public void changeCommitteeHead(String committeeName, String chairName) throws CollegeException {
        Committee committee = getByName(committees, committeeName);
        Lecturer newChair = getByName(lecturers, chairName);
        testChangeCommitteeHead(committee,newChair);
        committee.setChair(newChair);
    }

        public <T extends HasName> T getByName(Set<T> set, String name){
        for(T item : set){
            if (item.getName().equalsIgnoreCase(name)){
                return item;
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
        if (!(committee.getMemberType().equals(lecturer.getDegreeLevel()))){
            throw new InvalidOperationValueException(Enums.errorMessage.MEMBER_TYPE_MISMATCH.getMessage());
        }
    }

    public void addLecturerToCommittee(String committeeName, String lecturerName) throws CollegeException {
        Committee committee = getByName(committees,committeeName);
        Lecturer lecturer = getByName(lecturers, lecturerName);
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

    public void removeMemberFromCommittee(String committeeName,String lecturerName) throws CollegeException{
        Committee committee = getByName(committees, committeeName);
        Lecturer member = getByName(lecturers, lecturerName);
        testRemoveMemberFromCommittee(committee,member);
        committee.removeMember(member); // remove member from member arr in committee
        member.removeCommittee(committee); // remove committee from committees arr in lecturer
    }

    public boolean checkIfLecturerInCommittee(Lecturer lecturer, Committee committee){
        return committee.getMembers().contains(lecturer);
    }

    public double calculateAvgSalary(Set<Lecturer> lecturersSet){
        if (lecturersSet.isEmpty()) {
            return 0.0;
        }
        double salarySum = 0, avg;
        int arrSize = lecturersSet.size();
        for (Lecturer lecturer : lecturersSet) {
            salarySum += lecturer.getSalary();
        }
        avg = salarySum / arrSize;
        avg = (double) ((int) (avg * 100)) / 100;
        return avg;
    }

    public double getLecturersSalaryAvg() {
       return calculateAvgSalary(lecturers);
    }

    public void testGetDepartmentMembersSalaryAvg(Department department) throws NotExistException{
        if(department == null){
            throw new NotExistException(Enums.errorMessage.DEPARTMENT_NOT_EXIST.getMessage());
        }
    }
    public void getDepartmentMembersSalaryAvg() throws CollegeException {
        String name = Main.getStringFromUser("Enter department Name: ");
        Department department = getByName(departments, name);
        testGetDepartmentMembersSalaryAvg(department);

        double salaryAvg = calculateAvgSalary(department.getLecturers());

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

        Department department = getByName(departments,departmentName);
        Lecturer lecturer = getByName(lecturers, lecturerName);
        testAddLecturerToDepartment(department, lecturer); //could throw

        department.addLecturer(lecturer);
        lecturer.setDepartment(department);
    }

    public String SetToString(Set<?> set){
        Iterator<?> it = set.iterator();
        StringBuilder toReturn = new StringBuilder();
        while(it.hasNext()) {
            toReturn.append(it.next().toString());
            toReturn.append("\n");
        }
        return toReturn.toString();
    }

    public void getDetailsOfAllLecturers() throws CollegeException {
        Comparator<Lecturer> LecturerComparator;
        int orderBy = Main.getIntFromUser("Please select a sorting criterion:\n  1 - by Name\n  2 - by Degree Level\n  3 - by Salary\n");
        switch (orderBy) {
            case 1:
                LecturerComparator =(o1, o2) -> o1.getName().compareTo(o2.getName());
                break;
            case 2:
                LecturerComparator = (o1, o2) -> {
                    degreeLevel dl1 = degreeLevel.valueOf(o1.getDegreeLevel());
                    degreeLevel dl2 = degreeLevel.valueOf(o2.getDegreeLevel());
                    int x = dl1.compareTo(dl2);
                    if(x == 0)
                        return o1.getName().compareTo(o2.getName());
                    return x;
                };
                break;
            case 3:
                LecturerComparator = (o1, o2) -> {
                    int x = Double.compare(o1.getSalary(), o2.getSalary());
                    if(x == 0)
                        return o1.getName().compareTo(o2.getName());
                    return x;
                };
                break;
            default:
                throw new InvalidUserInputException(Enums.errorMessage.INVALID_CHOICE.getMessage());
        }
        Set<Lecturer> orderedLecturers = new TreeSet<>(LecturerComparator);
        orderedLecturers.addAll(lecturers);
        Main.printMessage(SetToString(orderedLecturers));
    }

    public void getDetailsOfAllCommittees() throws CollegeException {
        Comparator<Committee> CommitteeComparator;
        int orderBy = Main.getIntFromUser("Please select a sorting criterion:\n  1 - by Name\n  2 - by Number of Members\n  3 - by Member Type\n");
        switch (orderBy) {
            case 1:
                CommitteeComparator =(o1, o2) ->
                        o1.getName().compareTo(o2.getName());
                break;
            case 2:
                CommitteeComparator = (o1, o2) -> {
                    //Integer instead of int because we wanted to use compareTo()
                    Integer numMembers1 = o1.getMembers().size();
                    Integer numMembers2 = o2.getMembers().size();
                    int x =  numMembers1.compareTo(numMembers2);
                    if(x == 0)
                        return o1.getName().compareTo(o2.getName());
                    return x;
                };
                break;
            case 3:
                CommitteeComparator = (o1, o2) -> {
                    degreeLevel mt1 = degreeLevel.valueOf(o1.getMemberType());
                    degreeLevel mt2 = degreeLevel.valueOf(o2.getMemberType());
                    int x = mt1.compareTo(mt2);
                    if(x == 0)
                        return o1.getName().compareTo(o2.getName());
                    return x;
                };
                break;
            default:
                throw new InvalidUserInputException(Enums.errorMessage.INVALID_CHOICE.getMessage());
        }
        Set<Committee> orderedCommittees = new TreeSet<>(CommitteeComparator);
        orderedCommittees.addAll(committees);
        Main.printMessage(SetToString(orderedCommittees));
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
        Lecturer lecturer1 = getByName(lecturers, lecturerName1);
        Lecturer lecturer2 = getByName(lecturers, lecturerName2);

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
        Committee committee1 = getByName(committees, committeeName1);
        Committee committee2 = getByName(committees, committeeName2);

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
