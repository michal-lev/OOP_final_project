package michal_liora;

import java.util.ArrayList;

public class Lecturer {
    protected String name;
    protected String id;
    protected Enums.degreeLevel degreeLevel;
    protected String degreeTitle;
    protected double salary;
    protected Department department;
    protected ArrayList<Committee> committees;

    public Lecturer(String name, String id, String degreeLevel, String degreeTitle, double salary, Department department) {
        setName(name);
        setId(id);
        setDegreeLevel(degreeLevel);
        setDegreeTitle(degreeTitle);
        setSalary(salary);
        setDepartment(department);
        setCommittees(new ArrayList<>());
        setCommitteesCount(0);
    }

    public void setId(String id) {
        if (id != null && !id.isEmpty()) {
            this.id = id;
        }
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    public void setDegreeLevel(String degreeLevel) {
        this.degreeLevel = Enums.degreeLevel.valueOf(degreeLevel.toUpperCase());
    }

    public void setDegreeTitle(String degreeTitle) {
        if (degreeTitle != null && !degreeTitle.isEmpty()) {
            this.degreeTitle = degreeTitle;
        }
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setCommittees(ArrayList<Committee> committees) {
        this.committees = committees;
    }

    public void setCommitteesCount(int committeesCount) {
        this.committeesCount = committeesCount;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDegreeLevel() {
        return degreeLevel.toString();
    }

    public String getDegreeTitle() {
        return degreeTitle;
    }

    public double getSalary() {
        return salary;
    }

    public Department getDepartment() {
        return department;
    }

    public ArrayList<Committee> getCommittees() {
        return committees;
    }

    public void updateCommittees(Committee committee){
        committees.add(committee);
    }

    public String committeesNamesToString() {
        String committesStr = "[";
        int i = 0, committeesCount = committees.size();
        for (; i < (committeesCount - 1); i++){
            committesStr += committees.get(i).getName() + ", " ;
        }
        if(committeesCount != 0) {
            committesStr += committees.get(i).getName();
        }
        committesStr += "]";
        return committesStr;
    }

    public void removeCommittee(Committee committee){
        committees.remove(committee);
    }

    @Override
    public String toString() {
        return "{" +
                "name=" + name +
                ", id=" + id +
                ", degreeLevel=" + degreeLevel +
                ", degreeTitle=" + degreeTitle +
                ", salary=" + salary +
                ", department=" + ((department != null) ? department.getName() : "(None)") +
                ", committees=" + committeesNamesToString() +
                "}";
    }

    public int salaryEquals(Double otherSalary){

        if (salary > otherSalary) {
            return 1;
        }
        if (otherSalary > salary) {
            return -1;
        }
        return 0;
    }

    public boolean committeesArrEqualsByName(ArrayList<Committee> otherCommittees){
        for (int i = 0; i < committees.size(); i++){
            if (!committees.get(i).getName().equals(otherCommittees.get(i).getName())){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object toCompare) {
        if (toCompare == null || toCompare.getClass() != getClass())
            return false;
        Lecturer otherLecturer = (Lecturer) toCompare;
        return  salaryEquals(otherLecturer.salary) == 0 &&
                name.equals(otherLecturer.name) &&
                id.equals(otherLecturer.id) &&
                degreeLevel.equals(otherLecturer.degreeLevel) &&
                degreeTitle.equals(otherLecturer.degreeTitle) &&
                (department == null ? (otherLecturer.department == null) : department.equals(otherLecturer.department)) &&
                committeesArrEqualsByName(otherLecturer.committees);
    }
}
